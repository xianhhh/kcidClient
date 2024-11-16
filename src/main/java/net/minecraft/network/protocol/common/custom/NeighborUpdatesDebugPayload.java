package net.minecraft.network.protocol.common.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record NeighborUpdatesDebugPayload(long time, BlockPos pos) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/neighbors_update");

   public NeighborUpdatesDebugPayload(FriendlyByteBuf p_301219_) {
      this(p_301219_.readVarLong(), p_301219_.readBlockPos());
   }

   public void write(FriendlyByteBuf p_300822_) {
      p_300822_.writeVarLong(this.time);
      p_300822_.writeBlockPos(this.pos);
   }

   public ResourceLocation id() {
      return ID;
   }
}