package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;

public record GameEventListenerDebugPayload(PositionSource listenerPos, int listenerRange) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/game_event_listeners");

   public GameEventListenerDebugPayload(FriendlyByteBuf p_300927_) {
      this(PositionSourceType.fromNetwork(p_300927_), p_300927_.readVarInt());
   }

   public void write(FriendlyByteBuf p_299971_) {
      PositionSourceType.toNetwork(this.listenerPos, p_299971_);
      p_299971_.writeVarInt(this.listenerRange);
   }

   public ResourceLocation id() {
      return ID;
   }
}