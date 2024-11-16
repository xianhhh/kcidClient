package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Set;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record MatchTool(Optional<ItemPredicate> predicate) implements LootItemCondition {
   public static final Codec<MatchTool> CODEC = RecordCodecBuilder.create((p_297207_) -> {
      return p_297207_.group(ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "predicate").forGetter(MatchTool::predicate)).apply(p_297207_, MatchTool::new);
   });

   public LootItemConditionType getType() {
      return LootItemConditions.MATCH_TOOL;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return ImmutableSet.of(LootContextParams.TOOL);
   }

   public boolean test(LootContext p_82000_) {
      ItemStack itemstack = p_82000_.getParamOrNull(LootContextParams.TOOL);
      return itemstack != null && (this.predicate.isEmpty() || this.predicate.get().matches(itemstack));
   }

   public static LootItemCondition.Builder toolMatches(ItemPredicate.Builder p_81998_) {
      return () -> {
         return new MatchTool(Optional.of(p_81998_.build()));
      };
   }
}