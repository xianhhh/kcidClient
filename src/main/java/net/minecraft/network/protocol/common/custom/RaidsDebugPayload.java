package net.minecraft.network.protocol.common.custom;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record RaidsDebugPayload(List<BlockPos> raidCenters) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/raids");

   public RaidsDebugPayload(FriendlyByteBuf p_298262_) {
      this(p_298262_.readList(FriendlyByteBuf::readBlockPos));
   }

   public void write(FriendlyByteBuf p_298176_) {
      p_298176_.writeCollection(this.raidCenters, FriendlyByteBuf::writeBlockPos);
   }

   public ResourceLocation id() {
      return ID;
   }
}