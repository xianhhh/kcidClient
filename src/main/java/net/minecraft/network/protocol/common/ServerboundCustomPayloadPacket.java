package net.minecraft.network.protocol.common;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.BrandPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.resources.ResourceLocation;

public record ServerboundCustomPayloadPacket(CustomPacketPayload payload) implements Packet<ServerCommonPacketListener> {
   private static final int MAX_PAYLOAD_SIZE = 32767;
   private static final Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> KNOWN_TYPES = ImmutableMap.<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>>builder().put(BrandPayload.ID, BrandPayload::new).build();

   public ServerboundCustomPayloadPacket(FriendlyByteBuf p_298896_) {
      this(readPayload(p_298896_.readResourceLocation(), p_298896_));
   }

   private static CustomPacketPayload readPayload(ResourceLocation p_301116_, FriendlyByteBuf p_298967_) {
      FriendlyByteBuf.Reader<? extends CustomPacketPayload> reader = KNOWN_TYPES.get(p_301116_);
      return (CustomPacketPayload)(reader != null ? reader.apply(p_298967_) : readUnknownPayload(p_301116_, p_298967_));
   }

   private static DiscardedPayload readUnknownPayload(ResourceLocation p_300234_, FriendlyByteBuf p_299925_) {
      int i = p_299925_.readableBytes();
      if (i >= 0 && i <= 32767) {
         p_299925_.skipBytes(i);
         return new DiscardedPayload(p_300234_);
      } else {
         throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
      }
   }

   public void write(FriendlyByteBuf p_299043_) {
      p_299043_.writeResourceLocation(this.payload.id());
      this.payload.write(p_299043_);
   }

   public void handle(ServerCommonPacketListener p_297991_) {
      p_297991_.handleCustomPayload(this);
   }
}