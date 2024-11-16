package net.minecraft.client.multiplayer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.telemetry.WorldSessionTelemetryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.ServerboundPacketListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPacket;
import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.common.custom.BrandPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public abstract class ClientCommonPacketListenerImpl implements ClientCommonPacketListener {
   private static final Component GENERIC_DISCONNECT_MESSAGE = Component.translatable("disconnect.lost");
   private static final Logger LOGGER = LogUtils.getLogger();
   protected final Minecraft minecraft;
   protected final Connection connection;
   @Nullable
   protected final ServerData serverData;
   @Nullable
   protected String serverBrand;
   protected final WorldSessionTelemetryManager telemetryManager;
   @Nullable
   protected final Screen postDisconnectScreen;
   private final List<ClientCommonPacketListenerImpl.DeferredPacket> deferredPackets = new ArrayList<>();

   protected ClientCommonPacketListenerImpl(Minecraft p_300051_, Connection p_300688_, CommonListenerCookie p_300429_) {
      this.minecraft = p_300051_;
      this.connection = p_300688_;
      this.serverData = p_300429_.serverData();
      this.serverBrand = p_300429_.serverBrand();
      this.telemetryManager = p_300429_.telemetryManager();
      this.postDisconnectScreen = p_300429_.postDisconnectScreen();
   }

   public void handleKeepAlive(ClientboundKeepAlivePacket p_301155_) {
      this.sendWhen(new ServerboundKeepAlivePacket(p_301155_.getId()), () -> {
         return !RenderSystem.isFrozenAtPollEvents();
      }, Duration.ofMinutes(1L));
   }

   public void handlePing(ClientboundPingPacket p_300922_) {
      PacketUtils.ensureRunningOnSameThread(p_300922_, this, this.minecraft);
      this.send(new ServerboundPongPacket(p_300922_.getId()));
   }

   public void handleCustomPayload(ClientboundCustomPayloadPacket p_298103_) {
      CustomPacketPayload custompacketpayload = p_298103_.payload();
      if (!(custompacketpayload instanceof DiscardedPayload)) {
         PacketUtils.ensureRunningOnSameThread(p_298103_, this, this.minecraft);
         if (custompacketpayload instanceof BrandPayload) {
            BrandPayload brandpayload = (BrandPayload)custompacketpayload;
            this.serverBrand = brandpayload.brand();
            this.telemetryManager.onServerBrandReceived(brandpayload.brand());
         } else {
            this.handleCustomPayload(custompacketpayload);
         }

      }
   }

   protected abstract void handleCustomPayload(CustomPacketPayload p_297976_);

   protected abstract RegistryAccess.Frozen registryAccess();

   public void handleResourcePack(ClientboundResourcePackPacket p_300555_) {
      URL url = parseResourcePackUrl(p_300555_.getUrl());
      if (url == null) {
         this.send(ServerboundResourcePackPacket.Action.FAILED_DOWNLOAD);
      } else {
         String s = p_300555_.getHash();
         boolean flag = p_300555_.isRequired();
         if (this.serverData != null && this.serverData.getResourcePackStatus() == ServerData.ServerPackStatus.ENABLED) {
            this.send(ServerboundResourcePackPacket.Action.ACCEPTED);
            this.packApplicationCallback(this.minecraft.getDownloadedPackSource().downloadAndSelectResourcePack(url, s, true));
         } else if (this.serverData != null && this.serverData.getResourcePackStatus() != ServerData.ServerPackStatus.PROMPT && (!flag || this.serverData.getResourcePackStatus() != ServerData.ServerPackStatus.DISABLED)) {
            this.send(ServerboundResourcePackPacket.Action.DECLINED);
            if (flag) {
               this.connection.disconnect(Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
            }
         } else {
            this.minecraft.execute(() -> {
               this.showServerPackPrompt(url, s, flag, p_300555_.getPrompt());
            });
         }

      }
   }

   private void showServerPackPrompt(URL p_299293_, String p_297795_, boolean p_297484_, @Nullable Component p_300186_) {
      Screen screen = this.minecraft.screen;
      this.minecraft.setScreen(new ConfirmScreen((p_298595_) -> {
         this.minecraft.setScreen(screen);
         if (p_298595_) {
            if (this.serverData != null) {
               this.serverData.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);
            }

            this.send(ServerboundResourcePackPacket.Action.ACCEPTED);
            this.packApplicationCallback(this.minecraft.getDownloadedPackSource().downloadAndSelectResourcePack(p_299293_, p_297795_, true));
         } else {
            this.send(ServerboundResourcePackPacket.Action.DECLINED);
            if (p_297484_) {
               this.connection.disconnect(Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
            } else if (this.serverData != null) {
               this.serverData.setResourcePackStatus(ServerData.ServerPackStatus.DISABLED);
            }
         }

         if (this.serverData != null) {
            ServerList.saveSingleServer(this.serverData);
         }

      }, p_297484_ ? Component.translatable("multiplayer.requiredTexturePrompt.line1") : Component.translatable("multiplayer.texturePrompt.line1"), preparePackPrompt(p_297484_ ? Component.translatable("multiplayer.requiredTexturePrompt.line2").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD) : Component.translatable("multiplayer.texturePrompt.line2"), p_300186_), p_297484_ ? CommonComponents.GUI_PROCEED : CommonComponents.GUI_YES, (Component)(p_297484_ ? Component.translatable("menu.disconnect") : CommonComponents.GUI_NO)));
   }

   private static Component preparePackPrompt(Component p_299226_, @Nullable Component p_298885_) {
      return (Component)(p_298885_ == null ? p_299226_ : Component.translatable("multiplayer.texturePrompt.serverPrompt", p_299226_, p_298885_));
   }

   @Nullable
   private static URL parseResourcePackUrl(String p_298850_) {
      try {
         URL url = new URL(p_298850_);
         String s = url.getProtocol();
         return !"http".equals(s) && !"https".equals(s) ? null : url;
      } catch (MalformedURLException malformedurlexception) {
         return null;
      }
   }

   private void packApplicationCallback(CompletableFuture<?> p_297539_) {
      p_297539_.thenRun(() -> {
         this.send(ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED);
      }).exceptionally((p_299077_) -> {
         this.send(ServerboundResourcePackPacket.Action.FAILED_DOWNLOAD);
         return null;
      });
   }

   public void handleUpdateTags(ClientboundUpdateTagsPacket p_299537_) {
      PacketUtils.ensureRunningOnSameThread(p_299537_, this, this.minecraft);
      p_299537_.getTags().forEach(this::updateTagsForRegistry);
   }

   private <T> void updateTagsForRegistry(ResourceKey<? extends Registry<? extends T>> p_301094_, TagNetworkSerialization.NetworkPayload p_297701_) {
      if (!p_297701_.isEmpty()) {
         Registry<T> registry = this.registryAccess().registry(p_301094_).orElseThrow(() -> {
            return new IllegalStateException("Unknown registry " + p_301094_);
         });
         Map<TagKey<T>, List<Holder<T>>> map = new HashMap<>();
         TagNetworkSerialization.deserializeTagsFromNetwork((ResourceKey<? extends Registry<T>>)p_301094_, registry, p_297701_, map::put);
         registry.bindTags(map);
      }
   }

   private void send(ServerboundResourcePackPacket.Action p_299758_) {
      this.connection.send(new ServerboundResourcePackPacket(p_299758_));
   }

   public void handleDisconnect(ClientboundDisconnectPacket p_298016_) {
      this.connection.disconnect(p_298016_.getReason());
   }

   protected void sendDeferredPackets() {
      Iterator<ClientCommonPacketListenerImpl.DeferredPacket> iterator = this.deferredPackets.iterator();

      while(iterator.hasNext()) {
         ClientCommonPacketListenerImpl.DeferredPacket clientcommonpacketlistenerimpl$deferredpacket = iterator.next();
         if (clientcommonpacketlistenerimpl$deferredpacket.sendCondition().getAsBoolean()) {
            this.send(clientcommonpacketlistenerimpl$deferredpacket.packet);
            iterator.remove();
         } else if (clientcommonpacketlistenerimpl$deferredpacket.expirationTime() <= Util.getMillis()) {
            iterator.remove();
         }
      }

   }

   public void send(Packet<?> p_300175_) {
      this.connection.send(p_300175_);
   }

   public void onDisconnect(Component p_298766_) {
      this.telemetryManager.onDisconnect();
      this.minecraft.disconnect(this.createDisconnectScreen(p_298766_));
      LOGGER.warn("Client disconnected with reason: {}", (Object)p_298766_.getString());
   }

   protected Screen createDisconnectScreen(Component p_299787_) {
      Screen screen = Objects.requireNonNullElseGet(this.postDisconnectScreen, () -> {
         return new JoinMultiplayerScreen(new TitleScreen());
      });
      return (Screen)(this.serverData != null && this.serverData.isRealm() ? new DisconnectedRealmsScreen(screen, GENERIC_DISCONNECT_MESSAGE, p_299787_) : new DisconnectedScreen(screen, GENERIC_DISCONNECT_MESSAGE, p_299787_));
   }

   @Nullable
   public String serverBrand() {
      return this.serverBrand;
   }

   private void sendWhen(Packet<? extends ServerboundPacketListener> p_300852_, BooleanSupplier p_299754_, Duration p_299011_) {
      if (p_299754_.getAsBoolean()) {
         this.send(p_300852_);
      } else {
         this.deferredPackets.add(new ClientCommonPacketListenerImpl.DeferredPacket(p_300852_, p_299754_, Util.getMillis() + p_299011_.toMillis()));
      }

   }

   @OnlyIn(Dist.CLIENT)
   static record DeferredPacket(Packet<? extends ServerboundPacketListener> packet, BooleanSupplier sendCondition, long expirationTime) {
   }
}
