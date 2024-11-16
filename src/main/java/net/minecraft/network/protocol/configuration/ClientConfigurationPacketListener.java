package net.minecraft.network.protocol.configuration;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;

public interface ClientConfigurationPacketListener extends ClientCommonPacketListener {
   default ConnectionProtocol protocol() {
      return ConnectionProtocol.CONFIGURATION;
   }

   void handleConfigurationFinished(ClientboundFinishConfigurationPacket p_301141_);

   void handleRegistryData(ClientboundRegistryDataPacket p_298669_);

   void handleEnabledFeatures(ClientboundUpdateEnabledFeaturesPacket p_298844_);
}