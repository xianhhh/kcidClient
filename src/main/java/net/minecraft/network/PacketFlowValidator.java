package net.minecraft.network;

import com.mojang.logging.LogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import org.slf4j.Logger;

public class PacketFlowValidator extends MessageToMessageCodec<Packet<?>, Packet<?>> {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final AttributeKey<ConnectionProtocol.CodecData<?>> decoderKey;
   private final AttributeKey<ConnectionProtocol.CodecData<?>> encoderKey;

   public PacketFlowValidator(AttributeKey<ConnectionProtocol.CodecData<?>> p_298489_, AttributeKey<ConnectionProtocol.CodecData<?>> p_298624_) {
      this.decoderKey = p_298489_;
      this.encoderKey = p_298624_;
   }

   private static void validatePacket(ChannelHandlerContext p_297391_, Packet<?> p_300731_, List<Object> p_301049_, AttributeKey<ConnectionProtocol.CodecData<?>> p_297339_) {
      Attribute<ConnectionProtocol.CodecData<?>> attribute = p_297391_.channel().attr(p_297339_);
      ConnectionProtocol.CodecData<?> codecdata = attribute.get();
      if (!codecdata.isValidPacketType(p_300731_)) {
         LOGGER.error("Unrecognized packet in pipeline {}:{} - {}", codecdata.protocol().id(), codecdata.flow(), p_300731_);
      }

      ReferenceCountUtil.retain(p_300731_);
      p_301049_.add(p_300731_);
      ProtocolSwapHandler.swapProtocolIfNeeded(attribute, p_300731_);
   }

   protected void decode(ChannelHandlerContext p_298853_, Packet<?> p_300545_, List<Object> p_299821_) throws Exception {
      validatePacket(p_298853_, p_300545_, p_299821_, this.decoderKey);
   }

   protected void encode(ChannelHandlerContext p_299696_, Packet<?> p_298941_, List<Object> p_300232_) throws Exception {
      validatePacket(p_299696_, p_298941_, p_300232_, this.encoderKey);
   }
}