package net.minecraft.network.protocol.common.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record WorldGenAttemptDebugPayload(BlockPos pos, float scale, float red, float green, float blue, float alpha) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/worldgen_attempt");

   public WorldGenAttemptDebugPayload(FriendlyByteBuf p_298227_) {
      this(p_298227_.readBlockPos(), p_298227_.readFloat(), p_298227_.readFloat(), p_298227_.readFloat(), p_298227_.readFloat(), p_298227_.readFloat());
   }

   public void write(FriendlyByteBuf p_301372_) {
      p_301372_.writeBlockPos(this.pos);
      p_301372_.writeFloat(this.scale);
      p_301372_.writeFloat(this.red);
      p_301372_.writeFloat(this.green);
      p_301372_.writeFloat(this.blue);
      p_301372_.writeFloat(this.alpha);
   }

   public ResourceLocation id() {
      return ID;
   }
}