package net.minecraft.network.protocol.common.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record GameTestAddMarkerDebugPayload(BlockPos pos, int color, String text, int durationMs) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/game_test_add_marker");

   public GameTestAddMarkerDebugPayload(FriendlyByteBuf p_300441_) {
      this(p_300441_.readBlockPos(), p_300441_.readInt(), p_300441_.readUtf(), p_300441_.readInt());
   }

   public void write(FriendlyByteBuf p_300444_) {
      p_300444_.writeBlockPos(this.pos);
      p_300444_.writeInt(this.color);
      p_300444_.writeUtf(this.text);
      p_300444_.writeInt(this.durationMs);
   }

   public ResourceLocation id() {
      return ID;
   }
}