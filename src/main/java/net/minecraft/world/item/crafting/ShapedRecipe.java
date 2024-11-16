package net.minecraft.world.item.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;

public class ShapedRecipe implements CraftingRecipe {
   final int width;
   final int height;
   final NonNullList<Ingredient> recipeItems;
   final ItemStack result;
   final String group;
   final CraftingBookCategory category;
   final boolean showNotification;

   public ShapedRecipe(String p_272759_, CraftingBookCategory p_273506_, int p_272952_, int p_272920_, NonNullList<Ingredient> p_273650_, ItemStack p_272852_, boolean p_273122_) {
      this.group = p_272759_;
      this.category = p_273506_;
      this.width = p_272952_;
      this.height = p_272920_;
      this.recipeItems = p_273650_;
      this.result = p_272852_;
      this.showNotification = p_273122_;
   }

   public ShapedRecipe(String p_250221_, CraftingBookCategory p_250716_, int p_251480_, int p_251980_, NonNullList<Ingredient> p_252150_, ItemStack p_248581_) {
      this(p_250221_, p_250716_, p_251480_, p_251980_, p_252150_, p_248581_, true);
   }

   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializer.SHAPED_RECIPE;
   }

   public String getGroup() {
      return this.group;
   }

   public CraftingBookCategory category() {
      return this.category;
   }

   public ItemStack getResultItem(RegistryAccess p_266881_) {
      return this.result;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.recipeItems;
   }

   public boolean showNotification() {
      return this.showNotification;
   }

   public boolean canCraftInDimensions(int p_44161_, int p_44162_) {
      return p_44161_ >= this.width && p_44162_ >= this.height;
   }

   public boolean matches(CraftingContainer p_44176_, Level p_44177_) {
      for(int i = 0; i <= p_44176_.getWidth() - this.width; ++i) {
         for(int j = 0; j <= p_44176_.getHeight() - this.height; ++j) {
            if (this.matches(p_44176_, i, j, true)) {
               return true;
            }

            if (this.matches(p_44176_, i, j, false)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean matches(CraftingContainer p_44171_, int p_44172_, int p_44173_, boolean p_44174_) {
      for(int i = 0; i < p_44171_.getWidth(); ++i) {
         for(int j = 0; j < p_44171_.getHeight(); ++j) {
            int k = i - p_44172_;
            int l = j - p_44173_;
            Ingredient ingredient = Ingredient.EMPTY;
            if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
               if (p_44174_) {
                  ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
               } else {
                  ingredient = this.recipeItems.get(k + l * this.width);
               }
            }

            if (!ingredient.test(p_44171_.getItem(i + j * p_44171_.getWidth()))) {
               return false;
            }
         }
      }

      return true;
   }

   public ItemStack assemble(CraftingContainer p_266686_, RegistryAccess p_266725_) {
      return this.getResultItem(p_266725_).copy();
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   @VisibleForTesting
   static String[] shrink(List<String> p_299210_) {
      int i = Integer.MAX_VALUE;
      int j = 0;
      int k = 0;
      int l = 0;

      for(int i1 = 0; i1 < p_299210_.size(); ++i1) {
         String s = p_299210_.get(i1);
         i = Math.min(i, firstNonSpace(s));
         int j1 = lastNonSpace(s);
         j = Math.max(j, j1);
         if (j1 < 0) {
            if (k == i1) {
               ++k;
            }

            ++l;
         } else {
            l = 0;
         }
      }

      if (p_299210_.size() == l) {
         return new String[0];
      } else {
         String[] astring = new String[p_299210_.size() - l - k];

         for(int k1 = 0; k1 < astring.length; ++k1) {
            astring[k1] = p_299210_.get(k1 + k).substring(i, j + 1);
         }

         return astring;
      }
   }

   public boolean isIncomplete() {
      NonNullList<Ingredient> nonnulllist = this.getIngredients();
      return nonnulllist.isEmpty() || nonnulllist.stream().filter((p_151277_) -> {
         return !p_151277_.isEmpty();
      }).anyMatch((p_151273_) -> {
         return p_151273_.getItems().length == 0;
      });
   }

   private static int firstNonSpace(String p_44185_) {
      int i;
      for(i = 0; i < p_44185_.length() && p_44185_.charAt(i) == ' '; ++i) {
      }

      return i;
   }

   private static int lastNonSpace(String p_44201_) {
      int i;
      for(i = p_44201_.length() - 1; i >= 0 && p_44201_.charAt(i) == ' '; --i) {
      }

      return i;
   }

   public static class Serializer implements RecipeSerializer<ShapedRecipe> {
      static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().flatXmap((p_297814_) -> {
         if (p_297814_.size() > 3) {
            return DataResult.error(() -> {
               return "Invalid pattern: too many rows, 3 is maximum";
            });
         } else if (p_297814_.isEmpty()) {
            return DataResult.error(() -> {
               return "Invalid pattern: empty pattern not allowed";
            });
         } else {
            int i = p_297814_.get(0).length();

            for(String s : p_297814_) {
               if (s.length() > 3) {
                  return DataResult.error(() -> {
                     return "Invalid pattern: too many columns, 3 is maximum";
                  });
               }

               if (i != s.length()) {
                  return DataResult.error(() -> {
                     return "Invalid pattern: each row must be the same width";
                  });
               }
            }

            return DataResult.success(p_297814_);
         }
      }, DataResult::success);
      static final Codec<String> SINGLE_CHARACTER_STRING_CODEC = Codec.STRING.flatXmap((p_301277_) -> {
         if (p_301277_.length() != 1) {
            return DataResult.error(() -> {
               return "Invalid key entry: '" + p_301277_ + "' is an invalid symbol (must be 1 character only).";
            });
         } else {
            return " ".equals(p_301277_) ? DataResult.error(() -> {
               return "Invalid key entry: ' ' is a reserved symbol.";
            }) : DataResult.success(p_301277_);
         }
      }, DataResult::success);
      private static final Codec<ShapedRecipe> CODEC = ShapedRecipe.Serializer.RawShapedRecipe.CODEC.flatXmap((p_300056_) -> {
         String[] astring = ShapedRecipe.shrink(p_300056_.pattern);
         int i = astring[0].length();
         int j = astring.length;
         NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
         Set<String> set = Sets.newHashSet(p_300056_.key.keySet());

         for(int k = 0; k < astring.length; ++k) {
            String s = astring[k];

            for(int l = 0; l < s.length(); ++l) {
               String s1 = s.substring(l, l + 1);
               Ingredient ingredient = s1.equals(" ") ? Ingredient.EMPTY : p_300056_.key.get(s1);
               if (ingredient == null) {
                  return DataResult.error(() -> {
                     return "Pattern references symbol '" + s1 + "' but it's not defined in the key";
                  });
               }

               set.remove(s1);
               nonnulllist.set(l + i * k, ingredient);
            }
         }

         if (!set.isEmpty()) {
            return DataResult.error(() -> {
               return "Key defines symbols that aren't used in pattern: " + set;
            });
         } else {
            ShapedRecipe shapedrecipe = new ShapedRecipe(p_300056_.group, p_300056_.category, i, j, nonnulllist, p_300056_.result, p_300056_.showNotification);
            return DataResult.success(shapedrecipe);
         }
      }, (p_299463_) -> {
         throw new NotImplementedException("Serializing ShapedRecipe is not implemented yet.");
      });

      public Codec<ShapedRecipe> codec() {
         return CODEC;
      }

      public ShapedRecipe fromNetwork(FriendlyByteBuf p_44234_) {
         int i = p_44234_.readVarInt();
         int j = p_44234_.readVarInt();
         String s = p_44234_.readUtf();
         CraftingBookCategory craftingbookcategory = p_44234_.readEnum(CraftingBookCategory.class);
         NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

         for(int k = 0; k < nonnulllist.size(); ++k) {
            nonnulllist.set(k, Ingredient.fromNetwork(p_44234_));
         }

         ItemStack itemstack = p_44234_.readItem();
         boolean flag = p_44234_.readBoolean();
         return new ShapedRecipe(s, craftingbookcategory, i, j, nonnulllist, itemstack, flag);
      }

      public void toNetwork(FriendlyByteBuf p_44227_, ShapedRecipe p_44228_) {
         p_44227_.writeVarInt(p_44228_.width);
         p_44227_.writeVarInt(p_44228_.height);
         p_44227_.writeUtf(p_44228_.group);
         p_44227_.writeEnum(p_44228_.category);

         for(Ingredient ingredient : p_44228_.recipeItems) {
            ingredient.toNetwork(p_44227_);
         }

         p_44227_.writeItem(p_44228_.result);
         p_44227_.writeBoolean(p_44228_.showNotification);
      }

      static record RawShapedRecipe(String group, CraftingBookCategory category, Map<String, Ingredient> key, List<String> pattern, ItemStack result, boolean showNotification) {
         public static final Codec<ShapedRecipe.Serializer.RawShapedRecipe> CODEC = RecordCodecBuilder.create((p_298430_) -> {
            return p_298430_.group(ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter((p_300105_) -> {
               return p_300105_.group;
            }), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((p_301213_) -> {
               return p_301213_.category;
            }), ExtraCodecs.strictUnboundedMap(ShapedRecipe.Serializer.SINGLE_CHARACTER_STRING_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter((p_297983_) -> {
               return p_297983_.key;
            }), ShapedRecipe.Serializer.PATTERN_CODEC.fieldOf("pattern").forGetter((p_300956_) -> {
               return p_300956_.pattern;
            }), CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter((p_299535_) -> {
               return p_299535_.result;
            }), ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true).forGetter((p_297368_) -> {
               return p_297368_.showNotification;
            })).apply(p_298430_, ShapedRecipe.Serializer.RawShapedRecipe::new);
         });
      }
   }
}