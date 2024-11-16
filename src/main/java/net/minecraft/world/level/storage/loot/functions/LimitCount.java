package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LimitCount extends LootItemConditionalFunction {
   public static final Codec<LimitCount> CODEC = RecordCodecBuilder.create((p_297107_) -> {
      return commonFields(p_297107_).and(IntRange.CODEC.fieldOf("limit").forGetter((p_297106_) -> {
         return p_297106_.limiter;
      })).apply(p_297107_, LimitCount::new);
   });
   private final IntRange limiter;

   private LimitCount(List<LootItemCondition> p_298546_, IntRange p_165214_) {
      super(p_298546_);
      this.limiter = p_165214_;
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.LIMIT_COUNT;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return this.limiter.getReferencedContextParams();
   }

   public ItemStack run(ItemStack p_80644_, LootContext p_80645_) {
      int i = this.limiter.clamp(p_80645_, p_80644_.getCount());
      p_80644_.setCount(i);
      return p_80644_;
   }

   public static LootItemConditionalFunction.Builder<?> limitCount(IntRange p_165216_) {
      return simpleBuilder((p_297105_) -> {
         return new LimitCount(p_297105_, p_165216_);
      });
   }
}