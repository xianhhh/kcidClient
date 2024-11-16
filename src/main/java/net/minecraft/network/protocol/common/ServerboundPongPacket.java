package net.minecraft.network.protocol.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPongPacket implements Packet<ServerCommonPacketListener> {
   private final int id;

   public ServerboundPongPacket(int p_300898_) {
      this.id = p_300898_;
   }

   public ServerboundPongPacket(FriendlyByteBuf p_297786_) {
      this.id = p_297786_.readInt();
   }

   public void write(FriendlyByteBuf p_299986_) {
      p_299986_.writeInt(this.id);
   }

   public void handle(ServerCommonPacketListener p_298626_) {
      p_298626_.handlePong(this);
   }

   public int getId() {
      return this.id;
   }
}