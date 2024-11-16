package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;

public class ConstructBeaconTrigger extends SimpleCriterionTrigger<ConstructBeaconTrigger.TriggerInstance> {
   public ConstructBeaconTrigger.TriggerInstance createInstance(JsonObject p_286465_, Optional<ContextAwarePredicate> p_300541_, DeserializationContext p_286803_) {
      MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.fromJson(p_286465_.get("level"));
      return new ConstructBeaconTrigger.TriggerInstance(p_300541_, minmaxbounds$ints);
   }

   public void trigger(ServerPlayer p_148030_, int p_148031_) {
      this.trigger(p_148030_, (p_148028_) -> {
         return p_148028_.matches(p_148031_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final MinMaxBounds.Ints level;

      public TriggerInstance(Optional<ContextAwarePredicate> p_297785_, MinMaxBounds.Ints p_286272_) {
         super(p_297785_);
         this.level = p_286272_;
      }

      public static Criterion<ConstructBeaconTrigger.TriggerInstance> constructedBeacon() {
         return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new ConstructBeaconTrigger.TriggerInstance(Optional.empty(), MinMaxBounds.Ints.ANY));
      }

      public static Criterion<ConstructBeaconTrigger.TriggerInstance> constructedBeacon(MinMaxBounds.Ints p_22766_) {
         return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new ConstructBeaconTrigger.TriggerInstance(Optional.empty(), p_22766_));
      }

      public boolean matches(int p_148033_) {
         return this.level.matches(p_148033_);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         jsonobject.add("level", this.level.serializeToJson());
         return jsonobject;
      }
   }
}