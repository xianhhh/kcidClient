package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.levelgen.structure.Structure;

public record LocationPredicate(Optional<LocationPredicate.PositionPredicate> position, Optional<ResourceKey<Biome>> biome, Optional<ResourceKey<Structure>> structure, Optional<ResourceKey<Level>> dimension, Optional<Boolean> smokey, Optional<LightPredicate> light, Optional<BlockPredicate> block, Optional<FluidPredicate> fluid) {
   public static final Codec<LocationPredicate> CODEC = RecordCodecBuilder.create((p_296137_) -> {
      return p_296137_.group(ExtraCodecs.strictOptionalField(LocationPredicate.PositionPredicate.CODEC, "position").forGetter(LocationPredicate::position), ExtraCodecs.strictOptionalField(ResourceKey.codec(Registries.BIOME), "biome").forGetter(LocationPredicate::biome), ExtraCodecs.strictOptionalField(ResourceKey.codec(Registries.STRUCTURE), "structure").forGetter(LocationPredicate::structure), ExtraCodecs.strictOptionalField(ResourceKey.codec(Registries.DIMENSION), "dimension").forGetter(LocationPredicate::dimension), ExtraCodecs.strictOptionalField(Codec.BOOL, "smokey").forGetter(LocationPredicate::smokey), ExtraCodecs.strictOptionalField(LightPredicate.CODEC, "light").forGetter(LocationPredicate::light), ExtraCodecs.strictOptionalField(BlockPredicate.CODEC, "block").forGetter(LocationPredicate::block), ExtraCodecs.strictOptionalField(FluidPredicate.CODEC, "fluid").forGetter(LocationPredicate::fluid)).apply(p_296137_, LocationPredicate::new);
   });

   private static Optional<LocationPredicate> of(Optional<LocationPredicate.PositionPredicate> p_298583_, Optional<ResourceKey<Biome>> p_299235_, Optional<ResourceKey<Structure>> p_300788_, Optional<ResourceKey<Level>> p_299843_, Optional<Boolean> p_299039_, Optional<LightPredicate> p_297467_, Optional<BlockPredicate> p_298254_, Optional<FluidPredicate> p_297912_) {
      return p_298583_.isEmpty() && p_299235_.isEmpty() && p_300788_.isEmpty() && p_299843_.isEmpty() && p_299039_.isEmpty() && p_297467_.isEmpty() && p_298254_.isEmpty() && p_297912_.isEmpty() ? Optional.empty() : Optional.of(new LocationPredicate(p_298583_, p_299235_, p_300788_, p_299843_, p_299039_, p_297467_, p_298254_, p_297912_));
   }

   public boolean matches(ServerLevel p_52618_, double p_52619_, double p_52620_, double p_52621_) {
      if (this.position.isPresent() && !this.position.get().matches(p_52619_, p_52620_, p_52621_)) {
         return false;
      } else if (this.dimension.isPresent() && this.dimension.get() != p_52618_.dimension()) {
         return false;
      } else {
         BlockPos blockpos = BlockPos.containing(p_52619_, p_52620_, p_52621_);
         boolean flag = p_52618_.isLoaded(blockpos);
         if (!this.biome.isPresent() || flag && p_52618_.getBiome(blockpos).is(this.biome.get())) {
            if (!this.structure.isPresent() || flag && p_52618_.structureManager().getStructureWithPieceAt(blockpos, this.structure.get()).isValid()) {
               if (!this.smokey.isPresent() || flag && this.smokey.get() == CampfireBlock.isSmokeyPos(p_52618_, blockpos)) {
                  if (this.light.isPresent() && !this.light.get().matches(p_52618_, blockpos)) {
                     return false;
                  } else if (this.block.isPresent() && !this.block.get().matches(p_52618_, blockpos)) {
                     return false;
                  } else {
                     return !this.fluid.isPresent() || this.fluid.get().matches(p_52618_, blockpos);
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public JsonElement serializeToJson() {
      return Util.getOrThrow(CODEC.encodeStart(JsonOps.INSTANCE, this), IllegalStateException::new);
   }

   public static Optional<LocationPredicate> fromJson(@Nullable JsonElement p_52630_) {
      return p_52630_ != null && !p_52630_.isJsonNull() ? Optional.of(Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, p_52630_), JsonParseException::new)) : Optional.empty();
   }

   public static class Builder {
      private MinMaxBounds.Doubles x = MinMaxBounds.Doubles.ANY;
      private MinMaxBounds.Doubles y = MinMaxBounds.Doubles.ANY;
      private MinMaxBounds.Doubles z = MinMaxBounds.Doubles.ANY;
      private Optional<ResourceKey<Biome>> biome = Optional.empty();
      private Optional<ResourceKey<Structure>> structure = Optional.empty();
      private Optional<ResourceKey<Level>> dimension = Optional.empty();
      private Optional<Boolean> smokey = Optional.empty();
      private Optional<LightPredicate> light = Optional.empty();
      private Optional<BlockPredicate> block = Optional.empty();
      private Optional<FluidPredicate> fluid = Optional.empty();

      public static LocationPredicate.Builder location() {
         return new LocationPredicate.Builder();
      }

      public static LocationPredicate.Builder inBiome(ResourceKey<Biome> p_300086_) {
         return location().setBiome(p_300086_);
      }

      public static LocationPredicate.Builder inDimension(ResourceKey<Level> p_300753_) {
         return location().setDimension(p_300753_);
      }

      public static LocationPredicate.Builder inStructure(ResourceKey<Structure> p_301072_) {
         return location().setStructure(p_301072_);
      }

      public static LocationPredicate.Builder atYLocation(MinMaxBounds.Doubles p_297662_) {
         return location().setY(p_297662_);
      }

      public LocationPredicate.Builder setX(MinMaxBounds.Doubles p_153971_) {
         this.x = p_153971_;
         return this;
      }

      public LocationPredicate.Builder setY(MinMaxBounds.Doubles p_153975_) {
         this.y = p_153975_;
         return this;
      }

      public LocationPredicate.Builder setZ(MinMaxBounds.Doubles p_153979_) {
         this.z = p_153979_;
         return this;
      }

      public LocationPredicate.Builder setBiome(ResourceKey<Biome> p_52657_) {
         this.biome = Optional.of(p_52657_);
         return this;
      }

      public LocationPredicate.Builder setStructure(ResourceKey<Structure> p_220593_) {
         this.structure = Optional.of(p_220593_);
         return this;
      }

      public LocationPredicate.Builder setDimension(ResourceKey<Level> p_153977_) {
         this.dimension = Optional.of(p_153977_);
         return this;
      }

      public LocationPredicate.Builder setLight(LightPredicate.Builder p_298990_) {
         this.light = Optional.of(p_298990_.build());
         return this;
      }

      public LocationPredicate.Builder setBlock(BlockPredicate.Builder p_298525_) {
         this.block = Optional.of(p_298525_.build());
         return this;
      }

      public LocationPredicate.Builder setFluid(FluidPredicate.Builder p_298614_) {
         this.fluid = Optional.of(p_298614_.build());
         return this;
      }

      public LocationPredicate.Builder setSmokey(boolean p_299005_) {
         this.smokey = Optional.of(p_299005_);
         return this;
      }

      public LocationPredicate build() {
         Optional<LocationPredicate.PositionPredicate> optional = LocationPredicate.PositionPredicate.of(this.x, this.y, this.z);
         return new LocationPredicate(optional, this.biome, this.structure, this.dimension, this.smokey, this.light, this.block, this.fluid);
      }
   }

   static record PositionPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
      public static final Codec<LocationPredicate.PositionPredicate> CODEC = RecordCodecBuilder.create((p_301119_) -> {
         return p_301119_.group(ExtraCodecs.strictOptionalField(MinMaxBounds.Doubles.CODEC, "x", MinMaxBounds.Doubles.ANY).forGetter(LocationPredicate.PositionPredicate::x), ExtraCodecs.strictOptionalField(MinMaxBounds.Doubles.CODEC, "y", MinMaxBounds.Doubles.ANY).forGetter(LocationPredicate.PositionPredicate::y), ExtraCodecs.strictOptionalField(MinMaxBounds.Doubles.CODEC, "z", MinMaxBounds.Doubles.ANY).forGetter(LocationPredicate.PositionPredicate::z)).apply(p_301119_, LocationPredicate.PositionPredicate::new);
      });

      static Optional<LocationPredicate.PositionPredicate> of(MinMaxBounds.Doubles p_300563_, MinMaxBounds.Doubles p_301250_, MinMaxBounds.Doubles p_299764_) {
         return p_300563_.isAny() && p_301250_.isAny() && p_299764_.isAny() ? Optional.empty() : Optional.of(new LocationPredicate.PositionPredicate(p_300563_, p_301250_, p_299764_));
      }

      public boolean matches(double p_299909_, double p_298621_, double p_299854_) {
         return this.x.matches(p_299909_) && this.y.matches(p_298621_) && this.z.matches(p_299854_);
      }
   }
}