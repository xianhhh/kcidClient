package net.minecraft.network.protocol.status;

import net.minecraft.network.ClientPongPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundPongResponsePacket implements Packet<ClientPongPacketListener> {
   private final long time;

   public ClientboundPongResponsePacket(long p_134876_) {
      this.time = p_134876_;
   }

   public ClientboundPongResponsePacket(FriendlyByteBuf p_179831_) {
      this.time = p_179831_.readLong();
   }

   public void write(FriendlyByteBuf p_134884_) {
      p_134884_.writeLong(this.time);
   }

   public void handle(ClientPongPacketListener p_298225_) {
      p_298225_.handlePongResponse(this);
   }

   public long getTime() {
      return this.time;
   }
}