package net.minecraft.network.protocol.common.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record PoiTicketCountDebugPayload(BlockPos pos, int freeTicketCount) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/poi_ticket_count");

   public PoiTicketCountDebugPayload(FriendlyByteBuf p_299616_) {
      this(p_299616_.readBlockPos(), p_299616_.readInt());
   }

   public void write(FriendlyByteBuf p_299042_) {
      p_299042_.writeBlockPos(this.pos);
      p_299042_.writeInt(this.freeTicketCount);
   }

   public ResourceLocation id() {
      return ID;
   }
}