package net.minecraft.network.protocol.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundPingPacket implements Packet<ClientCommonPacketListener> {
   private final int id;

   public ClientboundPingPacket(int p_298858_) {
      this.id = p_298858_;
   }

   public ClientboundPingPacket(FriendlyByteBuf p_301364_) {
      this.id = p_301364_.readInt();
   }

   public void write(FriendlyByteBuf p_298056_) {
      p_298056_.writeInt(this.id);
   }

   public void handle(ClientCommonPacketListener p_299413_) {
      p_299413_.handlePing(this);
   }

   public int getId() {
      return this.id;
   }
}