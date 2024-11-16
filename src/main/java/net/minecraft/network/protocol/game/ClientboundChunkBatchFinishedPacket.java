package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundChunkBatchFinishedPacket(int batchSize) implements Packet<ClientGamePacketListener> {
   public ClientboundChunkBatchFinishedPacket(FriendlyByteBuf p_298630_) {
      this(p_298630_.readVarInt());
   }

   public void write(FriendlyByteBuf p_299639_) {
      p_299639_.writeVarInt(this.batchSize);
   }

   public void handle(ClientGamePacketListener p_297805_) {
      p_297805_.handleChunkBatchFinished(this);
   }
}