package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class SimpleCraftingRecipeSerializer<T extends CraftingRecipe> implements RecipeSerializer<T> {
   private final SimpleCraftingRecipeSerializer.Factory<T> constructor;
   private final Codec<T> codec;

   public SimpleCraftingRecipeSerializer(SimpleCraftingRecipeSerializer.Factory<T> p_250090_) {
      this.constructor = p_250090_;
      this.codec = RecordCodecBuilder.create((p_297778_) -> {
         return p_297778_.group(CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CraftingRecipe::category)).apply(p_297778_, p_250090_::create);
      });
   }

   public Codec<T> codec() {
      return this.codec;
   }

   public T fromNetwork(FriendlyByteBuf p_251882_) {
      CraftingBookCategory craftingbookcategory = p_251882_.readEnum(CraftingBookCategory.class);
      return this.constructor.create(craftingbookcategory);
   }

   public void toNetwork(FriendlyByteBuf p_248968_, T p_250179_) {
      p_248968_.writeEnum(p_250179_.category());
   }

   @FunctionalInterface
   public interface Factory<T extends CraftingRecipe> {
      T create(CraftingBookCategory p_249920_);
   }
}