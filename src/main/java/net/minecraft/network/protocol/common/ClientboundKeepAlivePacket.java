package net.minecraft.network.protocol.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundKeepAlivePacket implements Packet<ClientCommonPacketListener> {
   private final long id;

   public ClientboundKeepAlivePacket(long p_300888_) {
      this.id = p_300888_;
   }

   public ClientboundKeepAlivePacket(FriendlyByteBuf p_300278_) {
      this.id = p_300278_.readLong();
   }

   public void write(FriendlyByteBuf p_299560_) {
      p_299560_.writeLong(this.id);
   }

   public void handle(ClientCommonPacketListener p_297897_) {
      p_297897_.handleKeepAlive(this);
   }

   public long getId() {
      return this.id;
   }
}