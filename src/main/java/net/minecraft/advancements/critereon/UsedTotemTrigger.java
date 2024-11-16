package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class UsedTotemTrigger extends SimpleCriterionTrigger<UsedTotemTrigger.TriggerInstance> {
   public UsedTotemTrigger.TriggerInstance createInstance(JsonObject p_286841_, Optional<ContextAwarePredicate> p_299653_, DeserializationContext p_286414_) {
      Optional<ItemPredicate> optional = ItemPredicate.fromJson(p_286841_.get("item"));
      return new UsedTotemTrigger.TriggerInstance(p_299653_, optional);
   }

   public void trigger(ServerPlayer p_74432_, ItemStack p_74433_) {
      this.trigger(p_74432_, (p_74436_) -> {
         return p_74436_.matches(p_74433_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ItemPredicate> item;

      public TriggerInstance(Optional<ContextAwarePredicate> p_297919_, Optional<ItemPredicate> p_297826_) {
         super(p_297919_);
         this.item = p_297826_;
      }

      public static Criterion<UsedTotemTrigger.TriggerInstance> usedTotem(ItemPredicate p_298404_) {
         return CriteriaTriggers.USED_TOTEM.createCriterion(new UsedTotemTrigger.TriggerInstance(Optional.empty(), Optional.of(p_298404_)));
      }

      public static Criterion<UsedTotemTrigger.TriggerInstance> usedTotem(ItemLike p_300178_) {
         return CriteriaTriggers.USED_TOTEM.createCriterion(new UsedTotemTrigger.TriggerInstance(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(p_300178_).build())));
      }

      public boolean matches(ItemStack p_74451_) {
         return this.item.isEmpty() || this.item.get().matches(p_74451_);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.item.ifPresent((p_297715_) -> {
            jsonobject.add("item", p_297715_.serializeToJson());
         });
         return jsonobject;
      }
   }
}