package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record BrandPayload(String brand) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("brand");

   public BrandPayload(FriendlyByteBuf p_299720_) {
      this(p_299720_.readUtf());
   }

   public void write(FriendlyByteBuf p_297362_) {
      p_297362_.writeUtf(this.brand);
   }

   public ResourceLocation id() {
      return ID;
   }
}