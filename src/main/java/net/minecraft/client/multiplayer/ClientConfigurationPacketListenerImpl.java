package net.minecraft.client.multiplayer;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;
import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
import net.minecraft.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ClientConfigurationPacketListenerImpl extends ClientCommonPacketListenerImpl implements TickablePacketListener, ClientConfigurationPacketListener {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final GameProfile localGameProfile;
   private RegistryAccess.Frozen receivedRegistries;
   private FeatureFlagSet enabledFeatures;

   public ClientConfigurationPacketListenerImpl(Minecraft p_301278_, Connection p_299257_, CommonListenerCookie p_300907_) {
      super(p_301278_, p_299257_, p_300907_);
      this.localGameProfile = p_300907_.localGameProfile();
      this.receivedRegistries = p_300907_.receivedRegistries();
      this.enabledFeatures = p_300907_.enabledFeatures();
   }

   public boolean isAcceptingMessages() {
      return this.connection.isConnected();
   }

   protected RegistryAccess.Frozen registryAccess() {
      return this.receivedRegistries;
   }

   protected void handleCustomPayload(CustomPacketPayload p_301281_) {
      this.handleUnknownCustomPayload(p_301281_);
   }

   private void handleUnknownCustomPayload(CustomPacketPayload p_300719_) {
      LOGGER.warn("Unknown custom packet payload: {}", (Object)p_300719_.id());
   }

   public void handleRegistryData(ClientboundRegistryDataPacket p_299218_) {
      PacketUtils.ensureRunningOnSameThread(p_299218_, this, this.minecraft);
      RegistryAccess.Frozen registryaccess$frozen = ClientRegistryLayer.createRegistryAccess().replaceFrom(ClientRegistryLayer.REMOTE, p_299218_.registryHolder()).compositeAccess();
      if (!this.connection.isMemoryConnection()) {
         registryaccess$frozen.registries().forEach((p_299687_) -> {
            p_299687_.value().resetTags();
         });
      }

      this.receivedRegistries = registryaccess$frozen;
   }

   public void handleEnabledFeatures(ClientboundUpdateEnabledFeaturesPacket p_301158_) {
      this.enabledFeatures = FeatureFlags.REGISTRY.fromNames(p_301158_.features());
   }

   public void handleConfigurationFinished(ClientboundFinishConfigurationPacket p_299280_) {
      this.connection.suspendInboundAfterProtocolChange();
      PacketUtils.ensureRunningOnSameThread(p_299280_, this, this.minecraft);
      this.connection.setListener(new ClientPacketListener(this.minecraft, this.connection, new CommonListenerCookie(this.localGameProfile, this.telemetryManager, this.receivedRegistries, this.enabledFeatures, this.serverBrand, this.serverData, this.postDisconnectScreen)));
      this.connection.resumeInboundAfterProtocolChange();
      this.connection.send(new ServerboundFinishConfigurationPacket());
   }

   public void tick() {
      this.sendDeferredPackets();
   }
}