package net.minecraft.network.protocol.configuration;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundFinishConfigurationPacket() implements Packet<ClientConfigurationPacketListener> {
   public ClientboundFinishConfigurationPacket(FriendlyByteBuf p_300240_) {
      this();
   }

   public void write(FriendlyByteBuf p_298420_) {
   }

   public void handle(ClientConfigurationPacketListener p_299479_) {
      p_299479_.handleConfigurationFinished(this);
   }

   public ConnectionProtocol nextProtocol() {
      return ConnectionProtocol.PLAY;
   }
}