package net.minecraft.network.protocol.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundKeepAlivePacket implements Packet<ServerCommonPacketListener> {
   private final long id;

   public ServerboundKeepAlivePacket(long p_300615_) {
      this.id = p_300615_;
   }

   public void handle(ServerCommonPacketListener p_297247_) {
      p_297247_.handleKeepAlive(this);
   }

   public ServerboundKeepAlivePacket(FriendlyByteBuf p_299677_) {
      this.id = p_299677_.readLong();
   }

   public void write(FriendlyByteBuf p_299172_) {
      p_299172_.writeLong(this.id);
   }

   public long getId() {
      return this.id;
   }
}