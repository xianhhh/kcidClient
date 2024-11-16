package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;

public record ItemPredicate(Optional<TagKey<Item>> tag, Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, MinMaxBounds.Ints durability, List<EnchantmentPredicate> enchantments, List<EnchantmentPredicate> storedEnchantments, Optional<Holder<Potion>> potion, Optional<NbtPredicate> nbt) {
   private static final Codec<HolderSet<Item>> ITEMS_CODEC = BuiltInRegistries.ITEM.holderByNameCodec().listOf().xmap(HolderSet::direct, (p_296134_) -> {
      return p_296134_.stream().toList();
   });
   public static final Codec<ItemPredicate> CODEC = RecordCodecBuilder.create((p_296133_) -> {
      return p_296133_.group(ExtraCodecs.strictOptionalField(TagKey.codec(Registries.ITEM), "tag").forGetter(ItemPredicate::tag), ExtraCodecs.strictOptionalField(ITEMS_CODEC, "items").forGetter(ItemPredicate::items), ExtraCodecs.strictOptionalField(MinMaxBounds.Ints.CODEC, "count", MinMaxBounds.Ints.ANY).forGetter(ItemPredicate::count), ExtraCodecs.strictOptionalField(MinMaxBounds.Ints.CODEC, "durability", MinMaxBounds.Ints.ANY).forGetter(ItemPredicate::durability), ExtraCodecs.strictOptionalField(EnchantmentPredicate.CODEC.listOf(), "enchantments", List.of()).forGetter(ItemPredicate::enchantments), ExtraCodecs.strictOptionalField(EnchantmentPredicate.CODEC.listOf(), "stored_enchantments", List.of()).forGetter(ItemPredicate::storedEnchantments), ExtraCodecs.strictOptionalField(BuiltInRegistries.POTION.holderByNameCodec(), "potion").forGetter(ItemPredicate::potion), ExtraCodecs.strictOptionalField(NbtPredicate.CODEC, "nbt").forGetter(ItemPredicate::nbt)).apply(p_296133_, ItemPredicate::new);
   });

   public boolean matches(ItemStack p_45050_) {
      if (this.tag.isPresent() && !p_45050_.is(this.tag.get())) {
         return false;
      } else if (this.items.isPresent() && !p_45050_.is(this.items.get())) {
         return false;
      } else if (!this.count.matches(p_45050_.getCount())) {
         return false;
      } else if (!this.durability.isAny() && !p_45050_.isDamageableItem()) {
         return false;
      } else if (!this.durability.matches(p_45050_.getMaxDamage() - p_45050_.getDamageValue())) {
         return false;
      } else if (this.nbt.isPresent() && !this.nbt.get().matches(p_45050_)) {
         return false;
      } else {
         if (!this.enchantments.isEmpty()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.deserializeEnchantments(p_45050_.getEnchantmentTags());

            for(EnchantmentPredicate enchantmentpredicate : this.enchantments) {
               if (!enchantmentpredicate.containedIn(map)) {
                  return false;
               }
            }
         }

         if (!this.storedEnchantments.isEmpty()) {
            Map<Enchantment, Integer> map1 = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(p_45050_));

            for(EnchantmentPredicate enchantmentpredicate1 : this.storedEnchantments) {
               if (!enchantmentpredicate1.containedIn(map1)) {
                  return false;
               }
            }
         }

         return !this.potion.isPresent() || this.potion.get().value() == PotionUtils.getPotion(p_45050_);
      }
   }

   public static Optional<ItemPredicate> fromJson(@Nullable JsonElement p_45052_) {
      return p_45052_ != null && !p_45052_.isJsonNull() ? Optional.of(Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, p_45052_), JsonParseException::new)) : Optional.empty();
   }

   public JsonElement serializeToJson() {
      return Util.getOrThrow(CODEC.encodeStart(JsonOps.INSTANCE, this), IllegalStateException::new);
   }

   public static JsonElement serializeToJsonArray(List<ItemPredicate> p_298997_) {
      return Util.getOrThrow(CODEC.listOf().encodeStart(JsonOps.INSTANCE, p_298997_), IllegalStateException::new);
   }

   public static List<ItemPredicate> fromJsonArray(@Nullable JsonElement p_45056_) {
      return p_45056_ != null && !p_45056_.isJsonNull() ? Util.getOrThrow(CODEC.listOf().parse(JsonOps.INSTANCE, p_45056_), JsonParseException::new) : List.of();
   }

   public static class Builder {
      private final ImmutableList.Builder<EnchantmentPredicate> enchantments = ImmutableList.builder();
      private final ImmutableList.Builder<EnchantmentPredicate> storedEnchantments = ImmutableList.builder();
      private Optional<HolderSet<Item>> items = Optional.empty();
      private Optional<TagKey<Item>> tag = Optional.empty();
      private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
      private MinMaxBounds.Ints durability = MinMaxBounds.Ints.ANY;
      private Optional<Holder<Potion>> potion = Optional.empty();
      private Optional<NbtPredicate> nbt = Optional.empty();

      private Builder() {
      }

      public static ItemPredicate.Builder item() {
         return new ItemPredicate.Builder();
      }

      public ItemPredicate.Builder of(ItemLike... p_151446_) {
         this.items = Optional.of(HolderSet.direct((p_300947_) -> {
            return p_300947_.asItem().builtInRegistryHolder();
         }, p_151446_));
         return this;
      }

      public ItemPredicate.Builder of(TagKey<Item> p_204146_) {
         this.tag = Optional.of(p_204146_);
         return this;
      }

      public ItemPredicate.Builder withCount(MinMaxBounds.Ints p_151444_) {
         this.count = p_151444_;
         return this;
      }

      public ItemPredicate.Builder hasDurability(MinMaxBounds.Ints p_151450_) {
         this.durability = p_151450_;
         return this;
      }

      public ItemPredicate.Builder isPotion(Potion p_151442_) {
         this.potion = Optional.of(p_151442_.builtInRegistryHolder());
         return this;
      }

      public ItemPredicate.Builder hasNbt(CompoundTag p_45076_) {
         this.nbt = Optional.of(new NbtPredicate(p_45076_));
         return this;
      }

      public ItemPredicate.Builder hasEnchantment(EnchantmentPredicate p_45072_) {
         this.enchantments.add(p_45072_);
         return this;
      }

      public ItemPredicate.Builder hasStoredEnchantment(EnchantmentPredicate p_151448_) {
         this.storedEnchantments.add(p_151448_);
         return this;
      }

      public ItemPredicate build() {
         List<EnchantmentPredicate> list = this.enchantments.build();
         List<EnchantmentPredicate> list1 = this.storedEnchantments.build();
         return new ItemPredicate(this.tag, this.items, this.count, this.durability, list, list1, this.potion, this.nbt);
      }
   }
}