package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ServerboundChunkBatchReceivedPacket(float desiredChunksPerTick) implements Packet<ServerGamePacketListener> {
   public ServerboundChunkBatchReceivedPacket(FriendlyByteBuf p_297860_) {
      this(p_297860_.readFloat());
   }

   public void write(FriendlyByteBuf p_299711_) {
      p_299711_.writeFloat(this.desiredChunksPerTick);
   }

   public void handle(ServerGamePacketListener p_299816_) {
      p_299816_.handleChunkBatchReceived(this);
   }
}