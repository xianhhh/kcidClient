package net.minecraft.world.item.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class RecipeManager extends SimpleJsonResourceReloadListener {
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
   private static final Logger LOGGER = LogUtils.getLogger();
   private Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> recipes = ImmutableMap.of();
   private Map<ResourceLocation, RecipeHolder<?>> byName = ImmutableMap.of();
   private boolean hasErrors;

   public RecipeManager() {
      super(GSON, "recipes");
   }

   protected void apply(Map<ResourceLocation, JsonElement> p_44037_, ResourceManager p_44038_, ProfilerFiller p_44039_) {
      this.hasErrors = false;
      Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>>> map = Maps.newHashMap();
      ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder = ImmutableMap.builder();

      for(Map.Entry<ResourceLocation, JsonElement> entry : p_44037_.entrySet()) {
         ResourceLocation resourcelocation = entry.getKey();

         try {
            RecipeHolder<?> recipeholder = fromJson(resourcelocation, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
            map.computeIfAbsent(recipeholder.value().getType(), (p_44075_) -> {
               return ImmutableMap.builder();
            }).put(resourcelocation, recipeholder);
            builder.put(resourcelocation, recipeholder);
         } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
            LOGGER.error("Parsing error loading recipe {}", resourcelocation, jsonparseexception);
         }
      }

      this.recipes = map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (p_44033_) -> {
         return p_44033_.getValue().build();
      }));
      this.byName = builder.build();
      LOGGER.info("Loaded {} recipes", (int)map.size());
   }

   public boolean hadErrorsLoading() {
      return this.hasErrors;
   }

   public <C extends Container, T extends Recipe<C>> Optional<RecipeHolder<T>> getRecipeFor(RecipeType<T> p_44016_, C p_44017_, Level p_44018_) {
      return this.byType(p_44016_).values().stream().filter((p_296918_) -> {
         return p_296918_.value().matches(p_44017_, p_44018_);
      }).findFirst();
   }

   public <C extends Container, T extends Recipe<C>> Optional<Pair<ResourceLocation, RecipeHolder<T>>> getRecipeFor(RecipeType<T> p_220249_, C p_220250_, Level p_220251_, @Nullable ResourceLocation p_220252_) {
      Map<ResourceLocation, RecipeHolder<T>> map = this.byType(p_220249_);
      if (p_220252_ != null) {
         RecipeHolder<T> recipeholder = map.get(p_220252_);
         if (recipeholder != null && recipeholder.value().matches(p_220250_, p_220251_)) {
            return Optional.of(Pair.of(p_220252_, recipeholder));
         }
      }

      return map.entrySet().stream().filter((p_296906_) -> {
         return p_296906_.getValue().value().matches(p_220250_, p_220251_);
      }).findFirst().map((p_296909_) -> {
         return Pair.of(p_296909_.getKey(), p_296909_.getValue());
      });
   }

   public <C extends Container, T extends Recipe<C>> List<RecipeHolder<T>> getAllRecipesFor(RecipeType<T> p_44014_) {
      return List.copyOf(this.byType(p_44014_).values());
   }

   public <C extends Container, T extends Recipe<C>> List<RecipeHolder<T>> getRecipesFor(RecipeType<T> p_44057_, C p_44058_, Level p_44059_) {
      return this.byType(p_44057_).values().stream().filter((p_296912_) -> {
         return p_296912_.value().matches(p_44058_, p_44059_);
      }).sorted(Comparator.comparing((p_296908_) -> {
         return p_296908_.value().getResultItem(p_44059_.registryAccess()).getDescriptionId();
      })).collect(Collectors.toList());
   }

   private <C extends Container, T extends Recipe<C>> Map<ResourceLocation, RecipeHolder<T>> byType(RecipeType<T> p_44055_) {
      return (Map<ResourceLocation, RecipeHolder<T>>)(Map)this.recipes.getOrDefault(p_44055_, Collections.emptyMap());
   }

   public <C extends Container, T extends Recipe<C>> NonNullList<ItemStack> getRemainingItemsFor(RecipeType<T> p_44070_, C p_44071_, Level p_44072_) {
      Optional<RecipeHolder<T>> optional = this.getRecipeFor(p_44070_, p_44071_, p_44072_);
      if (optional.isPresent()) {
         return optional.get().value().getRemainingItems(p_44071_);
      } else {
         NonNullList<ItemStack> nonnulllist = NonNullList.withSize(p_44071_.getContainerSize(), ItemStack.EMPTY);

         for(int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, p_44071_.getItem(i));
         }

         return nonnulllist;
      }
   }

   public Optional<RecipeHolder<?>> byKey(ResourceLocation p_44044_) {
      return Optional.ofNullable(this.byName.get(p_44044_));
   }

   public Collection<RecipeHolder<?>> getRecipes() {
      return this.recipes.values().stream().flatMap((p_220270_) -> {
         return p_220270_.values().stream();
      }).collect(Collectors.toSet());
   }

   public Stream<ResourceLocation> getRecipeIds() {
      return this.recipes.values().stream().flatMap((p_220258_) -> {
         return p_220258_.keySet().stream();
      });
   }

   protected static RecipeHolder<?> fromJson(ResourceLocation p_44046_, JsonObject p_44047_) {
      String s = GsonHelper.getAsString(p_44047_, "type");
      Codec<? extends Recipe<?>> codec = BuiltInRegistries.RECIPE_SERIALIZER.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
         return new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
      }).codec();
      Recipe<?> recipe = Util.getOrThrow(codec.parse(JsonOps.INSTANCE, p_44047_), JsonParseException::new);
      return new RecipeHolder<>(p_44046_, recipe);
   }

   public void replaceRecipes(Iterable<RecipeHolder<?>> p_44025_) {
      this.hasErrors = false;
      Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> map = Maps.newHashMap();
      ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder = ImmutableMap.builder();
      p_44025_.forEach((p_296915_) -> {
         Map<ResourceLocation, RecipeHolder<?>> map1 = map.computeIfAbsent(p_296915_.value().getType(), (p_220272_) -> {
            return Maps.newHashMap();
         });
         ResourceLocation resourcelocation = p_296915_.id();
         RecipeHolder<?> recipeholder = map1.put(resourcelocation, p_296915_);
         builder.put(resourcelocation, p_296915_);
         if (recipeholder != null) {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + resourcelocation);
         }
      });
      this.recipes = ImmutableMap.copyOf(map);
      this.byName = builder.build();
   }

   public static <C extends Container, T extends Recipe<C>> RecipeManager.CachedCheck<C, T> createCheck(final RecipeType<T> p_220268_) {
      return new RecipeManager.CachedCheck<C, T>() {
         @Nullable
         private ResourceLocation lastRecipe;

         public Optional<RecipeHolder<T>> getRecipeFor(C p_220278_, Level p_220279_) {
            RecipeManager recipemanager = p_220279_.getRecipeManager();
            Optional<Pair<ResourceLocation, RecipeHolder<T>>> optional = recipemanager.getRecipeFor(p_220268_, p_220278_, p_220279_, this.lastRecipe);
            if (optional.isPresent()) {
               Pair<ResourceLocation, RecipeHolder<T>> pair = optional.get();
               this.lastRecipe = pair.getFirst();
               return Optional.of(pair.getSecond());
            } else {
               return Optional.empty();
            }
         }
      };
   }

   public interface CachedCheck<C extends Container, T extends Recipe<C>> {
      Optional<RecipeHolder<T>> getRecipeFor(C p_220280_, Level p_220281_);
   }
}
