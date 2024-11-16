package net.minecraft.network;

import com.google.common.base.Suppliers;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.flow.FlowControlHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.handshake.ClientIntent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.util.Mth;
import net.minecraft.util.SampleLogger;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Connection extends SimpleChannelInboundHandler<Packet<?>> {
   private static final float AVERAGE_PACKETS_SMOOTHING = 0.75F;
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Marker ROOT_MARKER = MarkerFactory.getMarker("NETWORK");
   public static final Marker PACKET_MARKER = Util.make(MarkerFactory.getMarker("NETWORK_PACKETS"), (p_202569_) -> {
      p_202569_.add(ROOT_MARKER);
   });
   public static final Marker PACKET_RECEIVED_MARKER = Util.make(MarkerFactory.getMarker("PACKET_RECEIVED"), (p_202562_) -> {
      p_202562_.add(PACKET_MARKER);
   });
   public static final Marker PACKET_SENT_MARKER = Util.make(MarkerFactory.getMarker("PACKET_SENT"), (p_202557_) -> {
      p_202557_.add(PACKET_MARKER);
   });
   public static final AttributeKey<ConnectionProtocol.CodecData<?>> ATTRIBUTE_SERVERBOUND_PROTOCOL = AttributeKey.valueOf("serverbound_protocol");
   public static final AttributeKey<ConnectionProtocol.CodecData<?>> ATTRIBUTE_CLIENTBOUND_PROTOCOL = AttributeKey.valueOf("clientbound_protocol");
   public static final Supplier<NioEventLoopGroup> NETWORK_WORKER_GROUP = Suppliers.memoize(() -> {
      return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
   });
   public static final Supplier<EpollEventLoopGroup> NETWORK_EPOLL_WORKER_GROUP = Suppliers.memoize(() -> {
      return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
   });
   public static final Supplier<DefaultEventLoopGroup> LOCAL_WORKER_GROUP = Suppliers.memoize(() -> {
      return new DefaultEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
   });
   private final PacketFlow receiving;
   private final Queue<Consumer<Connection>> pendingActions = Queues.newConcurrentLinkedQueue();
   private Channel channel;
   private SocketAddress address;
   @Nullable
   private volatile PacketListener disconnectListener;
   @Nullable
   private volatile PacketListener packetListener;
   @Nullable
   private Component disconnectedReason;
   private boolean encrypted;
   private boolean disconnectionHandled;
   private int receivedPackets;
   private int sentPackets;
   private float averageReceivedPackets;
   private float averageSentPackets;
   private int tickCount;
   private boolean handlingFault;
   @Nullable
   private volatile Component delayedDisconnect;
   @Nullable
   BandwidthDebugMonitor bandwidthDebugMonitor;

   public Connection(PacketFlow p_129482_) {
      this.receiving = p_129482_;
   }

   public void channelActive(ChannelHandlerContext p_129525_) throws Exception {
      super.channelActive(p_129525_);
      this.channel = p_129525_.channel();
      this.address = this.channel.remoteAddress();
      if (this.delayedDisconnect != null) {
         this.disconnect(this.delayedDisconnect);
      }

   }

   public static void setInitialProtocolAttributes(Channel p_300821_) {
      p_300821_.attr(ATTRIBUTE_SERVERBOUND_PROTOCOL).set(ConnectionProtocol.HANDSHAKING.codec(PacketFlow.SERVERBOUND));
      p_300821_.attr(ATTRIBUTE_CLIENTBOUND_PROTOCOL).set(ConnectionProtocol.HANDSHAKING.codec(PacketFlow.CLIENTBOUND));
   }

   public void channelInactive(ChannelHandlerContext p_129527_) {
      this.disconnect(Component.translatable("disconnect.endOfStream"));
   }

   public void exceptionCaught(ChannelHandlerContext p_129533_, Throwable p_129534_) {
      if (p_129534_ instanceof SkipPacketException) {
         LOGGER.debug("Skipping packet due to errors", p_129534_.getCause());
      } else {
         boolean flag = !this.handlingFault;
         this.handlingFault = true;
         if (this.channel.isOpen()) {
            if (p_129534_ instanceof TimeoutException) {
               LOGGER.debug("Timeout", p_129534_);
               this.disconnect(Component.translatable("disconnect.timeout"));
            } else {
               Component component = Component.translatable("disconnect.genericReason", "Internal Exception: " + p_129534_);
               if (flag) {
                  LOGGER.debug("Failed to sent packet", p_129534_);
                  if (this.getSending() == PacketFlow.CLIENTBOUND) {
                     ConnectionProtocol connectionprotocol = this.channel.attr(ATTRIBUTE_CLIENTBOUND_PROTOCOL).get().protocol();
                     Packet<?> packet = (Packet<?>)(connectionprotocol == ConnectionProtocol.LOGIN ? new ClientboundLoginDisconnectPacket(component) : new ClientboundDisconnectPacket(component));
                     this.send(packet, PacketSendListener.thenRun(() -> {
                        this.disconnect(component);
                     }));
                  } else {
                     this.disconnect(component);
                  }

                  this.setReadOnly();
               } else {
                  LOGGER.debug("Double fault", p_129534_);
                  this.disconnect(component);
               }
            }

         }
      }
   }

   protected void channelRead0(ChannelHandlerContext p_129487_, Packet<?> p_129488_) {
      if (this.channel.isOpen()) {
         PacketListener packetlistener = this.packetListener;
         if (packetlistener == null) {
            throw new IllegalStateException("Received a packet before the packet listener was initialized");
         } else {
            if (packetlistener.shouldHandleMessage(p_129488_)) {
               try {
                  genericsFtw(p_129488_, packetlistener);
               } catch (RunningOnDifferentThreadException runningondifferentthreadexception) {
               } catch (RejectedExecutionException rejectedexecutionexception) {
                  this.disconnect(Component.translatable("multiplayer.disconnect.server_shutdown"));
               } catch (ClassCastException classcastexception) {
                  LOGGER.error("Received {} that couldn't be processed", p_129488_.getClass(), classcastexception);
                  this.disconnect(Component.translatable("multiplayer.disconnect.invalid_packet"));
               }

               ++this.receivedPackets;
            }

         }
      }
   }

   private static <T extends PacketListener> void genericsFtw(Packet<T> p_129518_, PacketListener p_129519_) {
      p_129518_.handle((T)p_129519_);
   }

   public void suspendInboundAfterProtocolChange() {
      this.channel.config().setAutoRead(false);
   }

   public void resumeInboundAfterProtocolChange() {
      this.channel.config().setAutoRead(true);
   }

   public void setListener(PacketListener p_129506_) {
      Validate.notNull(p_129506_, "packetListener");
      PacketFlow packetflow = p_129506_.flow();
      if (packetflow != this.receiving) {
         throw new IllegalStateException("Trying to set listener for wrong side: connection is " + this.receiving + ", but listener is " + packetflow);
      } else {
         ConnectionProtocol connectionprotocol = p_129506_.protocol();
         ConnectionProtocol connectionprotocol1 = this.channel.attr(getProtocolKey(packetflow)).get().protocol();
         if (connectionprotocol1 != connectionprotocol) {
            throw new IllegalStateException("Trying to set listener for protocol " + connectionprotocol.id() + ", but current " + packetflow + " protocol is " + connectionprotocol1.id());
         } else {
            this.packetListener = p_129506_;
            this.disconnectListener = null;
         }
      }
   }

   public void setListenerForServerboundHandshake(PacketListener p_299346_) {
      if (this.packetListener != null) {
         throw new IllegalStateException("Listener already set");
      } else if (this.receiving == PacketFlow.SERVERBOUND && p_299346_.flow() == PacketFlow.SERVERBOUND && p_299346_.protocol() == ConnectionProtocol.HANDSHAKING) {
         this.packetListener = p_299346_;
      } else {
         throw new IllegalStateException("Invalid initial listener");
      }
   }

   public void initiateServerboundStatusConnection(String p_297855_, int p_297423_, ClientStatusPacketListener p_300237_) {
      this.initiateServerboundConnection(p_297855_, p_297423_, p_300237_, ClientIntent.STATUS);
   }

   public void initiateServerboundPlayConnection(String p_300250_, int p_297906_, ClientLoginPacketListener p_297708_) {
      this.initiateServerboundConnection(p_300250_, p_297906_, p_297708_, ClientIntent.LOGIN);
   }

   private void initiateServerboundConnection(String p_300730_, int p_300598_, PacketListener p_298739_, ClientIntent p_297789_) {
      this.disconnectListener = p_298739_;
      this.runOnceConnected((p_296374_) -> {
         p_296374_.setClientboundProtocolAfterHandshake(p_297789_);
         this.setListener(p_298739_);
         p_296374_.sendPacket(new ClientIntentionPacket(SharedConstants.getCurrentVersion().getProtocolVersion(), p_300730_, p_300598_, p_297789_), (PacketSendListener)null, true);
      });
   }

   public void setClientboundProtocolAfterHandshake(ClientIntent p_300629_) {
      this.channel.attr(ATTRIBUTE_CLIENTBOUND_PROTOCOL).set(p_300629_.protocol().codec(PacketFlow.CLIENTBOUND));
   }

   public void send(Packet<?> p_129513_) {
      this.send(p_129513_, (PacketSendListener)null);
   }

   public void send(Packet<?> p_243248_, @Nullable PacketSendListener p_243316_) {
      this.send(p_243248_, p_243316_, true);
   }

   public void send(Packet<?> p_298754_, @Nullable PacketSendListener p_300685_, boolean p_298821_) {
      if (this.isConnected()) {
         this.flushQueue();
         this.sendPacket(p_298754_, p_300685_, p_298821_);
      } else {
         this.pendingActions.add((p_296381_) -> {
            p_296381_.sendPacket(p_298754_, p_300685_, p_298821_);
         });
      }

   }

   public void runOnceConnected(Consumer<Connection> p_297681_) {
      if (this.isConnected()) {
         this.flushQueue();
         p_297681_.accept(this);
      } else {
         this.pendingActions.add(p_297681_);
      }

   }

   private void sendPacket(Packet<?> p_129521_, @Nullable PacketSendListener p_243246_, boolean p_299777_) {
      ++this.sentPackets;
      if (this.channel.eventLoop().inEventLoop()) {
         this.doSendPacket(p_129521_, p_243246_, p_299777_);
      } else {
         this.channel.eventLoop().execute(() -> {
            this.doSendPacket(p_129521_, p_243246_, p_299777_);
         });
      }

   }

   private void doSendPacket(Packet<?> p_243260_, @Nullable PacketSendListener p_243290_, boolean p_299937_) {
      ChannelFuture channelfuture = p_299937_ ? this.channel.writeAndFlush(p_243260_) : this.channel.write(p_243260_);
      if (p_243290_ != null) {
         channelfuture.addListener((p_243167_) -> {
            if (p_243167_.isSuccess()) {
               p_243290_.onSuccess();
            } else {
               Packet<?> packet = p_243290_.onFailure();
               if (packet != null) {
                  ChannelFuture channelfuture1 = this.channel.writeAndFlush(packet);
                  channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
               }
            }

         });
      }

      channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
   }

   public void flushChannel() {
      if (this.isConnected()) {
         this.flush();
      } else {
         this.pendingActions.add(Connection::flush);
      }

   }

   private void flush() {
      if (this.channel.eventLoop().inEventLoop()) {
         this.channel.flush();
      } else {
         this.channel.eventLoop().execute(() -> {
            this.channel.flush();
         });
      }

   }

   private static AttributeKey<ConnectionProtocol.CodecData<?>> getProtocolKey(PacketFlow p_298298_) {
      AttributeKey attributekey;
      switch (p_298298_) {
         case CLIENTBOUND:
            attributekey = ATTRIBUTE_CLIENTBOUND_PROTOCOL;
            break;
         case SERVERBOUND:
            attributekey = ATTRIBUTE_SERVERBOUND_PROTOCOL;
            break;
         default:
            throw new IncompatibleClassChangeError();
      }

      return attributekey;
   }

   private void flushQueue() {
      if (this.channel != null && this.channel.isOpen()) {
         synchronized(this.pendingActions) {
            Consumer<Connection> consumer;
            while((consumer = this.pendingActions.poll()) != null) {
               consumer.accept(this);
            }

         }
      }
   }

   public void tick() {
      this.flushQueue();
      PacketListener packetlistener = this.packetListener;
      if (packetlistener instanceof TickablePacketListener tickablepacketlistener) {
         tickablepacketlistener.tick();
      }

      if (!this.isConnected() && !this.disconnectionHandled) {
         this.handleDisconnection();
      }

      if (this.channel != null) {
         this.channel.flush();
      }

      if (this.tickCount++ % 20 == 0) {
         this.tickSecond();
      }

      if (this.bandwidthDebugMonitor != null) {
         this.bandwidthDebugMonitor.tick();
      }

   }

   protected void tickSecond() {
      this.averageSentPackets = Mth.lerp(0.75F, (float)this.sentPackets, this.averageSentPackets);
      this.averageReceivedPackets = Mth.lerp(0.75F, (float)this.receivedPackets, this.averageReceivedPackets);
      this.sentPackets = 0;
      this.receivedPackets = 0;
   }

   public SocketAddress getRemoteAddress() {
      return this.address;
   }

   public String getLoggableAddress(boolean p_298740_) {
      if (this.address == null) {
         return "local";
      } else {
         return p_298740_ ? this.address.toString() : "IP hidden";
      }
   }

   public void disconnect(Component p_129508_) {
      if (this.channel == null) {
         this.delayedDisconnect = p_129508_;
      }

      if (this.isConnected()) {
         this.channel.close().awaitUninterruptibly();
         this.disconnectedReason = p_129508_;
      }

   }

   public boolean isMemoryConnection() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public PacketFlow getReceiving() {
      return this.receiving;
   }

   public PacketFlow getSending() {
      return this.receiving.getOpposite();
   }

   public static Connection connectToServer(InetSocketAddress p_178301_, boolean p_178302_, @Nullable SampleLogger p_300093_) {
      Connection connection = new Connection(PacketFlow.CLIENTBOUND);
      if (p_300093_ != null) {
         connection.setBandwidthLogger(p_300093_);
      }

      ChannelFuture channelfuture = connect(p_178301_, p_178302_, connection);
      channelfuture.syncUninterruptibly();
      return connection;
   }

   public static ChannelFuture connect(InetSocketAddress p_290034_, boolean p_290035_, final Connection p_290031_) {
      Class<? extends SocketChannel> oclass;
      EventLoopGroup eventloopgroup;
      if (Epoll.isAvailable() && p_290035_) {
         oclass = EpollSocketChannel.class;
         eventloopgroup = NETWORK_EPOLL_WORKER_GROUP.get();
      } else {
         oclass = NioSocketChannel.class;
         eventloopgroup = NETWORK_WORKER_GROUP.get();
      }

      return (new Bootstrap()).group(eventloopgroup).handler(new ChannelInitializer<Channel>() {
         protected void initChannel(Channel p_129552_) {
            Connection.setInitialProtocolAttributes(p_129552_);

            try {
               p_129552_.config().setOption(ChannelOption.TCP_NODELAY, true);
            } catch (ChannelException channelexception) {
            }

            ChannelPipeline channelpipeline = p_129552_.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
            Connection.configureSerialization(channelpipeline, PacketFlow.CLIENTBOUND, p_290031_.bandwidthDebugMonitor);
            p_290031_.configurePacketHandler(channelpipeline);
         }
      }).channel(oclass).connect(p_290034_.getAddress(), p_290034_.getPort());
   }

   public static void configureSerialization(ChannelPipeline p_265436_, PacketFlow p_265104_, @Nullable BandwidthDebugMonitor p_299297_) {
      PacketFlow packetflow = p_265104_.getOpposite();
      AttributeKey<ConnectionProtocol.CodecData<?>> attributekey = getProtocolKey(p_265104_);
      AttributeKey<ConnectionProtocol.CodecData<?>> attributekey1 = getProtocolKey(packetflow);
      p_265436_.addLast("splitter", new Varint21FrameDecoder(p_299297_)).addLast("decoder", new PacketDecoder(attributekey)).addLast("prepender", new Varint21LengthFieldPrepender()).addLast("encoder", new PacketEncoder(attributekey1)).addLast("unbundler", new PacketBundleUnpacker(attributekey1)).addLast("bundler", new PacketBundlePacker(attributekey));
   }

   public void configurePacketHandler(ChannelPipeline p_300754_) {
      p_300754_.addLast(new FlowControlHandler()).addLast("packet_handler", this);
   }

   private static void configureInMemoryPacketValidation(ChannelPipeline p_299383_, PacketFlow p_299305_) {
      PacketFlow packetflow = p_299305_.getOpposite();
      AttributeKey<ConnectionProtocol.CodecData<?>> attributekey = getProtocolKey(p_299305_);
      AttributeKey<ConnectionProtocol.CodecData<?>> attributekey1 = getProtocolKey(packetflow);
      p_299383_.addLast("validator", new PacketFlowValidator(attributekey, attributekey1));
   }

   public static void configureInMemoryPipeline(ChannelPipeline p_298130_, PacketFlow p_298133_) {
      configureInMemoryPacketValidation(p_298130_, p_298133_);
   }

   public static Connection connectToLocalServer(SocketAddress p_129494_) {
      final Connection connection = new Connection(PacketFlow.CLIENTBOUND);
      (new Bootstrap()).group(LOCAL_WORKER_GROUP.get()).handler(new ChannelInitializer<Channel>() {
         protected void initChannel(Channel p_129557_) {
            Connection.setInitialProtocolAttributes(p_129557_);
            ChannelPipeline channelpipeline = p_129557_.pipeline();
            Connection.configureInMemoryPipeline(channelpipeline, PacketFlow.CLIENTBOUND);
            connection.configurePacketHandler(channelpipeline);
         }
      }).channel(LocalChannel.class).connect(p_129494_).syncUninterruptibly();
      return connection;
   }

   public void setEncryptionKey(Cipher p_129496_, Cipher p_129497_) {
      this.encrypted = true;
      this.channel.pipeline().addBefore("splitter", "decrypt", new CipherDecoder(p_129496_));
      this.channel.pipeline().addBefore("prepender", "encrypt", new CipherEncoder(p_129497_));
   }

   public boolean isEncrypted() {
      return this.encrypted;
   }

   public boolean isConnected() {
      return this.channel != null && this.channel.isOpen();
   }

   public boolean isConnecting() {
      return this.channel == null;
   }

   @Nullable
   public PacketListener getPacketListener() {
      return this.packetListener;
   }

   @Nullable
   public Component getDisconnectedReason() {
      return this.disconnectedReason;
   }

   public void setReadOnly() {
      if (this.channel != null) {
         this.channel.config().setAutoRead(false);
      }

   }

   public void setupCompression(int p_129485_, boolean p_182682_) {
      if (p_129485_ >= 0) {
         if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder) {
            ((CompressionDecoder)this.channel.pipeline().get("decompress")).setThreshold(p_129485_, p_182682_);
         } else {
            this.channel.pipeline().addBefore("decoder", "decompress", new CompressionDecoder(p_129485_, p_182682_));
         }

         if (this.channel.pipeline().get("compress") instanceof CompressionEncoder) {
            ((CompressionEncoder)this.channel.pipeline().get("compress")).setThreshold(p_129485_);
         } else {
            this.channel.pipeline().addBefore("encoder", "compress", new CompressionEncoder(p_129485_));
         }
      } else {
         if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder) {
            this.channel.pipeline().remove("decompress");
         }

         if (this.channel.pipeline().get("compress") instanceof CompressionEncoder) {
            this.channel.pipeline().remove("compress");
         }
      }

   }

   public void handleDisconnection() {
      if (this.channel != null && !this.channel.isOpen()) {
         if (this.disconnectionHandled) {
            LOGGER.warn("handleDisconnection() called twice");
         } else {
            this.disconnectionHandled = true;
            PacketListener packetlistener = this.getPacketListener();
            PacketListener packetlistener1 = packetlistener != null ? packetlistener : this.disconnectListener;
            if (packetlistener1 != null) {
               Component component = Objects.requireNonNullElseGet(this.getDisconnectedReason(), () -> {
                  return Component.translatable("multiplayer.disconnect.generic");
               });
               packetlistener1.onDisconnect(component);
            }

         }
      }
   }

   public float getAverageReceivedPackets() {
      return this.averageReceivedPackets;
   }

   public float getAverageSentPackets() {
      return this.averageSentPackets;
   }

   public void setBandwidthLogger(SampleLogger p_300126_) {
      this.bandwidthDebugMonitor = new BandwidthDebugMonitor(p_300126_);
   }
}