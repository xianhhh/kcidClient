package net.minecraft.network.protocol.game;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ServerboundConfigurationAcknowledgedPacket() implements Packet<ServerGamePacketListener> {
   public ServerboundConfigurationAcknowledgedPacket(FriendlyByteBuf p_299378_) {
      this();
   }

   public void write(FriendlyByteBuf p_298084_) {
   }

   public void handle(ServerGamePacketListener p_297365_) {
      p_297365_.handleConfigurationAcknowledged(this);
   }

   public ConnectionProtocol nextProtocol() {
      return ConnectionProtocol.CONFIGURATION;
   }
}