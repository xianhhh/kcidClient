package net.minecraft.network.protocol.game;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundStartConfigurationPacket() implements Packet<ClientGamePacketListener> {
   public ClientboundStartConfigurationPacket(FriendlyByteBuf p_300409_) {
      this();
   }

   public void write(FriendlyByteBuf p_298888_) {
   }

   public void handle(ClientGamePacketListener p_298066_) {
      p_298066_.handleConfigurationStart(this);
   }

   public ConnectionProtocol nextProtocol() {
      return ConnectionProtocol.CONFIGURATION;
   }
}