package net.minecraft.network.protocol.common.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record PoiAddedDebugPayload(BlockPos pos, String type, int freeTicketCount) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/poi_added");

   public PoiAddedDebugPayload(FriendlyByteBuf p_300736_) {
      this(p_300736_.readBlockPos(), p_300736_.readUtf(), p_300736_.readInt());
   }

   public void write(FriendlyByteBuf p_298137_) {
      p_298137_.writeBlockPos(this.pos);
      p_298137_.writeUtf(this.type);
      p_298137_.writeInt(this.freeTicketCount);
   }

   public ResourceLocation id() {
      return ID;
   }
}