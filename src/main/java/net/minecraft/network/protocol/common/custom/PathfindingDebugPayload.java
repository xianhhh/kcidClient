package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.pathfinder.Path;

public record PathfindingDebugPayload(int entityId, Path path, float maxNodeDistance) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/path");

   public PathfindingDebugPayload(FriendlyByteBuf p_297728_) {
      this(p_297728_.readInt(), Path.createFromStream(p_297728_), p_297728_.readFloat());
   }

   public void write(FriendlyByteBuf p_298780_) {
      p_298780_.writeInt(this.entityId);
      this.path.writeToStream(p_298780_);
      p_298780_.writeFloat(this.maxNodeDistance);
   }

   public ResourceLocation id() {
      return ID;
   }
}