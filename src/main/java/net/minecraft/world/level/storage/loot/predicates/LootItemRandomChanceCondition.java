package net.minecraft.world.level.storage.loot.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

public record LootItemRandomChanceCondition(float probability) implements LootItemCondition {
   public static final Codec<LootItemRandomChanceCondition> CODEC = RecordCodecBuilder.create((p_297204_) -> {
      return p_297204_.group(Codec.FLOAT.fieldOf("chance").forGetter(LootItemRandomChanceCondition::probability)).apply(p_297204_, LootItemRandomChanceCondition::new);
   });

   public LootItemConditionType getType() {
      return LootItemConditions.RANDOM_CHANCE;
   }

   public boolean test(LootContext p_81930_) {
      return p_81930_.getRandom().nextFloat() < this.probability;
   }

   public static LootItemCondition.Builder randomChance(float p_81928_) {
      return () -> {
         return new LootItemRandomChanceCondition(p_81928_);
      };
   }
}