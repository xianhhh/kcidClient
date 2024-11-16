package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundChunkBatchStartPacket() implements Packet<ClientGamePacketListener> {
   public ClientboundChunkBatchStartPacket(FriendlyByteBuf p_301003_) {
      this();
   }

   public void write(FriendlyByteBuf p_298384_) {
   }

   public void handle(ClientGamePacketListener p_301192_) {
      p_301192_.handleChunkBatchStart(this);
   }
}