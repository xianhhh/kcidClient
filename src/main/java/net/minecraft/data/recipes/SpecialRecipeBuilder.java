package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SpecialRecipeBuilder extends CraftingRecipeBuilder {
   final RecipeSerializer<?> serializer;

   public SpecialRecipeBuilder(RecipeSerializer<?> p_250173_) {
      this.serializer = p_250173_;
   }

   public static SpecialRecipeBuilder special(RecipeSerializer<? extends CraftingRecipe> p_249458_) {
      return new SpecialRecipeBuilder(p_249458_);
   }

   public void save(RecipeOutput p_301326_, String p_299862_) {
      this.save(p_301326_, new ResourceLocation(p_299862_));
   }

   public void save(RecipeOutput p_301231_, final ResourceLocation p_297560_) {
      p_301231_.accept(new CraftingRecipeBuilder.CraftingResult(CraftingBookCategory.MISC) {
         public RecipeSerializer<?> type() {
            return SpecialRecipeBuilder.this.serializer;
         }

         public ResourceLocation id() {
            return p_297560_;
         }

         @Nullable
         public AdvancementHolder advancement() {
            return null;
         }
      });
   }
}