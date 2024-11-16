package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.Level;

public class SmithingTrimRecipe implements SmithingRecipe {
   final Ingredient template;
   final Ingredient base;
   final Ingredient addition;

   SmithingTrimRecipe(Ingredient p_267298_, Ingredient p_266862_, Ingredient p_267050_) {
      this.template = p_267298_;
      this.base = p_266862_;
      this.addition = p_267050_;
   }

   public boolean matches(Container p_267224_, Level p_266798_) {
      return this.template.test(p_267224_.getItem(0)) && this.base.test(p_267224_.getItem(1)) && this.addition.test(p_267224_.getItem(2));
   }

   public ItemStack assemble(Container p_267320_, RegistryAccess p_267280_) {
      ItemStack itemstack = p_267320_.getItem(1);
      if (this.base.test(itemstack)) {
         Optional<Holder.Reference<TrimMaterial>> optional = TrimMaterials.getFromIngredient(p_267280_, p_267320_.getItem(2));
         Optional<Holder.Reference<TrimPattern>> optional1 = TrimPatterns.getFromTemplate(p_267280_, p_267320_.getItem(0));
         if (optional.isPresent() && optional1.isPresent()) {
            Optional<ArmorTrim> optional2 = ArmorTrim.getTrim(p_267280_, itemstack, false);
            if (optional2.isPresent() && optional2.get().hasPatternAndMaterial(optional1.get(), optional.get())) {
               return ItemStack.EMPTY;
            }

            ItemStack itemstack1 = itemstack.copy();
            itemstack1.setCount(1);
            ArmorTrim armortrim = new ArmorTrim(optional.get(), optional1.get());
            if (ArmorTrim.setTrim(p_267280_, itemstack1, armortrim)) {
               return itemstack1;
            }
         }
      }

      return ItemStack.EMPTY;
   }

   public ItemStack getResultItem(RegistryAccess p_266948_) {
      ItemStack itemstack = new ItemStack(Items.IRON_CHESTPLATE);
      Optional<Holder.Reference<TrimPattern>> optional = p_266948_.registryOrThrow(Registries.TRIM_PATTERN).holders().findFirst();
      if (optional.isPresent()) {
         Optional<Holder.Reference<TrimMaterial>> optional1 = p_266948_.registryOrThrow(Registries.TRIM_MATERIAL).getHolder(TrimMaterials.REDSTONE);
         if (optional1.isPresent()) {
            ArmorTrim armortrim = new ArmorTrim(optional1.get(), optional.get());
            ArmorTrim.setTrim(p_266948_, itemstack, armortrim);
         }
      }

      return itemstack;
   }

   public boolean isTemplateIngredient(ItemStack p_266762_) {
      return this.template.test(p_266762_);
   }

   public boolean isBaseIngredient(ItemStack p_266795_) {
      return this.base.test(p_266795_);
   }

   public boolean isAdditionIngredient(ItemStack p_266922_) {
      return this.addition.test(p_266922_);
   }

   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializer.SMITHING_TRIM;
   }

   public boolean isIncomplete() {
      return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::isEmpty);
   }

   public static class Serializer implements RecipeSerializer<SmithingTrimRecipe> {
      private static final Codec<SmithingTrimRecipe> CODEC = RecordCodecBuilder.create((p_297394_) -> {
         return p_297394_.group(Ingredient.CODEC.fieldOf("template").forGetter((p_298441_) -> {
            return p_298441_.template;
         }), Ingredient.CODEC.fieldOf("base").forGetter((p_297838_) -> {
            return p_297838_.base;
         }), Ingredient.CODEC.fieldOf("addition").forGetter((p_299309_) -> {
            return p_299309_.addition;
         })).apply(p_297394_, SmithingTrimRecipe::new);
      });

      public Codec<SmithingTrimRecipe> codec() {
         return CODEC;
      }

      public SmithingTrimRecipe fromNetwork(FriendlyByteBuf p_266888_) {
         Ingredient ingredient = Ingredient.fromNetwork(p_266888_);
         Ingredient ingredient1 = Ingredient.fromNetwork(p_266888_);
         Ingredient ingredient2 = Ingredient.fromNetwork(p_266888_);
         return new SmithingTrimRecipe(ingredient, ingredient1, ingredient2);
      }

      public void toNetwork(FriendlyByteBuf p_266901_, SmithingTrimRecipe p_266893_) {
         p_266893_.template.toNetwork(p_266901_);
         p_266893_.base.toNetwork(p_266901_);
         p_266893_.addition.toNetwork(p_266901_);
      }
   }
}