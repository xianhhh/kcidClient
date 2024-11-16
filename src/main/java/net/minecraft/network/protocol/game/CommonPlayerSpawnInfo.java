package net.minecraft.network.protocol.game;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public record CommonPlayerSpawnInfo(ResourceKey<DimensionType> dimensionType, ResourceKey<Level> dimension, long seed, GameType gameType, @Nullable GameType previousGameType, boolean isDebug, boolean isFlat, Optional<GlobalPos> lastDeathLocation, int portalCooldown) {
   public CommonPlayerSpawnInfo(FriendlyByteBuf p_301001_) {
      this(p_301001_.readResourceKey(Registries.DIMENSION_TYPE), p_301001_.readResourceKey(Registries.DIMENSION), p_301001_.readLong(), GameType.byId(p_301001_.readByte()), GameType.byNullableId(p_301001_.readByte()), p_301001_.readBoolean(), p_301001_.readBoolean(), p_301001_.readOptional(FriendlyByteBuf::readGlobalPos), p_301001_.readVarInt());
   }

   public void write(FriendlyByteBuf p_298723_) {
      p_298723_.writeResourceKey(this.dimensionType);
      p_298723_.writeResourceKey(this.dimension);
      p_298723_.writeLong(this.seed);
      p_298723_.writeByte(this.gameType.getId());
      p_298723_.writeByte(GameType.getNullableId(this.previousGameType));
      p_298723_.writeBoolean(this.isDebug);
      p_298723_.writeBoolean(this.isFlat);
      p_298723_.writeOptional(this.lastDeathLocation, FriendlyByteBuf::writeGlobalPos);
      p_298723_.writeVarInt(this.portalCooldown);
   }
}