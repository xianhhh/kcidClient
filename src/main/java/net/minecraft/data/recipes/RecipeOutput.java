package net.minecraft.data.recipes;

import net.minecraft.advancements.Advancement;

public interface RecipeOutput {
   void accept(FinishedRecipe p_301214_);

   Advancement.Builder advancement();
}