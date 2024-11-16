package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class EnchantWithLevelsFunction extends LootItemConditionalFunction {
   public static final Codec<EnchantWithLevelsFunction> CODEC = RecordCodecBuilder.create((p_298285_) -> {
      return commonFields(p_298285_).and(p_298285_.group(NumberProviders.CODEC.fieldOf("levels").forGetter((p_298991_) -> {
         return p_298991_.levels;
      }), Codec.BOOL.fieldOf("treasure").orElse(false).forGetter((p_298792_) -> {
         return p_298792_.treasure;
      }))).apply(p_298285_, EnchantWithLevelsFunction::new);
   });
   private final NumberProvider levels;
   private final boolean treasure;

   EnchantWithLevelsFunction(List<LootItemCondition> p_300816_, NumberProvider p_165194_, boolean p_165195_) {
      super(p_300816_);
      this.levels = p_165194_;
      this.treasure = p_165195_;
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.ENCHANT_WITH_LEVELS;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return this.levels.getReferencedContextParams();
   }

   public ItemStack run(ItemStack p_80483_, LootContext p_80484_) {
      RandomSource randomsource = p_80484_.getRandom();
      return EnchantmentHelper.enchantItem(randomsource, p_80483_, this.levels.getInt(p_80484_), this.treasure);
   }

   public static EnchantWithLevelsFunction.Builder enchantWithLevels(NumberProvider p_165197_) {
      return new EnchantWithLevelsFunction.Builder(p_165197_);
   }

   public static class Builder extends LootItemConditionalFunction.Builder<EnchantWithLevelsFunction.Builder> {
      private final NumberProvider levels;
      private boolean treasure;

      public Builder(NumberProvider p_165200_) {
         this.levels = p_165200_;
      }

      protected EnchantWithLevelsFunction.Builder getThis() {
         return this;
      }

      public EnchantWithLevelsFunction.Builder allowTreasure() {
         this.treasure = true;
         return this;
      }

      public LootItemFunction build() {
         return new EnchantWithLevelsFunction(this.getConditions(), this.levels, this.treasure);
      }
   }
}