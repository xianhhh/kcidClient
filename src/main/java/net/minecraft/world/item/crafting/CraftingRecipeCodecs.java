package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CraftingRecipeCodecs {
   private static final Codec<Item> ITEM_NONAIR_CODEC = ExtraCodecs.validate(BuiltInRegistries.ITEM.byNameCodec(), (p_297750_) -> {
      return p_297750_ == Items.AIR ? DataResult.error(() -> {
         return "Crafting result must not be minecraft:air";
      }) : DataResult.success(p_297750_);
   });
   public static final Codec<ItemStack> ITEMSTACK_OBJECT_CODEC = RecordCodecBuilder.create((p_298321_) -> {
      return p_298321_.group(ITEM_NONAIR_CODEC.fieldOf("item").forGetter(ItemStack::getItem), ExtraCodecs.strictOptionalField(ExtraCodecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount)).apply(p_298321_, ItemStack::new);
   });
   static final Codec<ItemStack> ITEMSTACK_NONAIR_CODEC = ExtraCodecs.validate(BuiltInRegistries.ITEM.byNameCodec(), (p_300046_) -> {
      return p_300046_ == Items.AIR ? DataResult.error(() -> {
         return "Empty ingredient not allowed here";
      }) : DataResult.success(p_300046_);
   }).xmap(ItemStack::new, ItemStack::getItem);
}