package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class FilledBucketTrigger extends SimpleCriterionTrigger<FilledBucketTrigger.TriggerInstance> {
   public FilledBucketTrigger.TriggerInstance createInstance(JsonObject p_286377_, Optional<ContextAwarePredicate> p_298249_, DeserializationContext p_286801_) {
      Optional<ItemPredicate> optional = ItemPredicate.fromJson(p_286377_.get("item"));
      return new FilledBucketTrigger.TriggerInstance(p_298249_, optional);
   }

   public void trigger(ServerPlayer p_38773_, ItemStack p_38774_) {
      this.trigger(p_38773_, (p_38777_) -> {
         return p_38777_.matches(p_38774_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ItemPredicate> item;

      public TriggerInstance(Optional<ContextAwarePredicate> p_298716_, Optional<ItemPredicate> p_300324_) {
         super(p_298716_);
         this.item = p_300324_;
      }

      public static Criterion<FilledBucketTrigger.TriggerInstance> filledBucket(ItemPredicate.Builder p_297424_) {
         return CriteriaTriggers.FILLED_BUCKET.createCriterion(new FilledBucketTrigger.TriggerInstance(Optional.empty(), Optional.of(p_297424_.build())));
      }

      public boolean matches(ItemStack p_38792_) {
         return !this.item.isPresent() || this.item.get().matches(p_38792_);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.item.ifPresent((p_300984_) -> {
            jsonobject.add("item", p_300984_.serializeToJson());
         });
         return jsonobject;
      }
   }
}