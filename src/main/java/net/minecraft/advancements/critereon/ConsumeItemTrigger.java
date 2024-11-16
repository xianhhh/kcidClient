package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ConsumeItemTrigger extends SimpleCriterionTrigger<ConsumeItemTrigger.TriggerInstance> {
   public ConsumeItemTrigger.TriggerInstance createInstance(JsonObject p_286664_, Optional<ContextAwarePredicate> p_297606_, DeserializationContext p_286347_) {
      return new ConsumeItemTrigger.TriggerInstance(p_297606_, ItemPredicate.fromJson(p_286664_.get("item")));
   }

   public void trigger(ServerPlayer p_23683_, ItemStack p_23684_) {
      this.trigger(p_23683_, (p_23687_) -> {
         return p_23687_.matches(p_23684_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ItemPredicate> item;

      public TriggerInstance(Optional<ContextAwarePredicate> p_297633_, Optional<ItemPredicate> p_298856_) {
         super(p_297633_);
         this.item = p_298856_;
      }

      public static Criterion<ConsumeItemTrigger.TriggerInstance> usedItem() {
         return CriteriaTriggers.CONSUME_ITEM.createCriterion(new ConsumeItemTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
      }

      public static Criterion<ConsumeItemTrigger.TriggerInstance> usedItem(ItemLike p_299577_) {
         return usedItem(ItemPredicate.Builder.item().of(p_299577_.asItem()));
      }

      public static Criterion<ConsumeItemTrigger.TriggerInstance> usedItem(ItemPredicate.Builder p_297282_) {
         return CriteriaTriggers.CONSUME_ITEM.createCriterion(new ConsumeItemTrigger.TriggerInstance(Optional.empty(), Optional.of(p_297282_.build())));
      }

      public boolean matches(ItemStack p_23702_) {
         return this.item.isEmpty() || this.item.get().matches(p_23702_);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.item.ifPresent((p_299494_) -> {
            jsonobject.add("item", p_299494_.serializeToJson());
         });
         return jsonobject;
      }
   }
}