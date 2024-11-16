package net.minecraft.network.protocol.login;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ServerboundLoginAcknowledgedPacket() implements Packet<ServerLoginPacketListener> {
   public ServerboundLoginAcknowledgedPacket(FriendlyByteBuf p_300437_) {
      this();
   }

   public void write(FriendlyByteBuf p_300127_) {
   }

   public void handle(ServerLoginPacketListener p_298226_) {
      p_298226_.handleLoginAcknowledgement(this);
   }

   public ConnectionProtocol nextProtocol() {
      return ConnectionProtocol.CONFIGURATION;
   }
}