package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record DiscardedPayload(ResourceLocation id) implements CustomPacketPayload {
   public void write(FriendlyByteBuf p_301050_) {
   }

   public ResourceLocation id() {
      return this.id;
   }
}