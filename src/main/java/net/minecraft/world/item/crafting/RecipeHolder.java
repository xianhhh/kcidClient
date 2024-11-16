package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceLocation;

public record RecipeHolder<T extends Recipe<?>>(ResourceLocation id, T value) {
   public boolean equals(Object p_298053_) {
      if (this == p_298053_) {
         return true;
      } else {
         if (p_298053_ instanceof RecipeHolder) {
            RecipeHolder<?> recipeholder = (RecipeHolder)p_298053_;
            if (this.id.equals(recipeholder.id)) {
               return true;
            }
         }

         return false;
      }
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public String toString() {
      return this.id.toString();
   }
}