package net.minecraft.network.protocol.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundDisconnectPacket implements Packet<ClientCommonPacketListener> {
   private final Component reason;

   public ClientboundDisconnectPacket(Component p_300118_) {
      this.reason = p_300118_;
   }

   public ClientboundDisconnectPacket(FriendlyByteBuf p_301323_) {
      this.reason = p_301323_.readComponent();
   }

   public void write(FriendlyByteBuf p_300138_) {
      p_300138_.writeComponent(this.reason);
   }

   public void handle(ClientCommonPacketListener p_297710_) {
      p_297710_.handleDisconnect(this);
   }

   public Component getReason() {
      return this.reason;
   }
}