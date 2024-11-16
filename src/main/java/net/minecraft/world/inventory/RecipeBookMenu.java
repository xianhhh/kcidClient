package net.minecraft.world.inventory;

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public abstract class RecipeBookMenu<C extends Container> extends AbstractContainerMenu {
   public RecipeBookMenu(MenuType<?> p_40115_, int p_40116_) {
      super(p_40115_, p_40116_);
   }

   public void handlePlacement(boolean p_40119_, RecipeHolder<?> p_297420_, ServerPlayer p_40121_) {
      (new ServerPlaceRecipe(this)).recipeClicked(p_40121_, p_297420_, p_40119_);
   }

   public abstract void fillCraftSlotsStackedContents(StackedContents p_40117_);

   public abstract void clearCraftingContent();

   public abstract boolean recipeMatches(RecipeHolder<? extends Recipe<C>> p_297792_);

   public abstract int getResultSlotIndex();

   public abstract int getGridWidth();

   public abstract int getGridHeight();

   public abstract int getSize();

   public abstract RecipeBookType getRecipeBookType();

   public abstract boolean shouldMoveToInventory(int p_150635_);
}
