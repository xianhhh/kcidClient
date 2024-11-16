package net.minecraft.network.protocol.common.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record PoiRemovedDebugPayload(BlockPos pos) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/poi_removed");

   public PoiRemovedDebugPayload(FriendlyByteBuf p_300036_) {
      this(p_300036_.readBlockPos());
   }

   public void write(FriendlyByteBuf p_300931_) {
      p_300931_.writeBlockPos(this.pos);
   }

   public ResourceLocation id() {
      return ID;
   }
}