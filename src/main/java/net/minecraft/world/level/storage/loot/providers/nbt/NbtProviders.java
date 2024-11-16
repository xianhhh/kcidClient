package net.minecraft.world.level.storage.loot.providers.nbt;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public class NbtProviders {
   private static final Codec<NbtProvider> TYPED_CODEC = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.byNameCodec().dispatch(NbtProvider::getType, LootNbtProviderType::codec);
   public static final Codec<NbtProvider> CODEC = ExtraCodecs.lazyInitializedCodec(() -> {
      return Codec.either(ContextNbtProvider.INLINE_CODEC, TYPED_CODEC).xmap((p_298461_) -> {
         return p_298461_.map(Function.identity(), Function.identity());
      }, (p_298663_) -> {
         Either either;
         if (p_298663_ instanceof ContextNbtProvider contextnbtprovider) {
            either = Either.left(contextnbtprovider);
         } else {
            either = Either.right(p_298663_);
         }

         return either;
      });
   });
   public static final LootNbtProviderType STORAGE = register("storage", StorageNbtProvider.CODEC);
   public static final LootNbtProviderType CONTEXT = register("context", ContextNbtProvider.CODEC);

   private static LootNbtProviderType register(String p_165629_, Codec<? extends NbtProvider> p_300005_) {
      return Registry.register(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE, new ResourceLocation(p_165629_), new LootNbtProviderType(p_300005_));
   }
}