package net.minecraft.network.protocol.common;

import net.minecraft.network.ClientboundPacketListener;

public interface ClientCommonPacketListener extends ClientboundPacketListener {
   void handleKeepAlive(ClientboundKeepAlivePacket p_299456_);

   void handlePing(ClientboundPingPacket p_297871_);

   void handleCustomPayload(ClientboundCustomPayloadPacket p_299137_);

   void handleDisconnect(ClientboundDisconnectPacket p_300983_);

   void handleResourcePack(ClientboundResourcePackPacket p_301120_);

   void handleUpdateTags(ClientboundUpdateTagsPacket p_297352_);
}