package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ShotCrossbowTrigger extends SimpleCriterionTrigger<ShotCrossbowTrigger.TriggerInstance> {
   public ShotCrossbowTrigger.TriggerInstance createInstance(JsonObject p_286726_, Optional<ContextAwarePredicate> p_300212_, DeserializationContext p_286701_) {
      Optional<ItemPredicate> optional = ItemPredicate.fromJson(p_286726_.get("item"));
      return new ShotCrossbowTrigger.TriggerInstance(p_300212_, optional);
   }

   public void trigger(ServerPlayer p_65463_, ItemStack p_65464_) {
      this.trigger(p_65463_, (p_65467_) -> {
         return p_65467_.matches(p_65464_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ItemPredicate> item;

      public TriggerInstance(Optional<ContextAwarePredicate> p_297452_, Optional<ItemPredicate> p_301333_) {
         super(p_297452_);
         this.item = p_301333_;
      }

      public static Criterion<ShotCrossbowTrigger.TriggerInstance> shotCrossbow(Optional<ItemPredicate> p_299474_) {
         return CriteriaTriggers.SHOT_CROSSBOW.createCriterion(new ShotCrossbowTrigger.TriggerInstance(Optional.empty(), p_299474_));
      }

      public static Criterion<ShotCrossbowTrigger.TriggerInstance> shotCrossbow(ItemLike p_65484_) {
         return CriteriaTriggers.SHOT_CROSSBOW.createCriterion(new ShotCrossbowTrigger.TriggerInstance(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(p_65484_).build())));
      }

      public boolean matches(ItemStack p_65482_) {
         return this.item.isEmpty() || this.item.get().matches(p_65482_);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.item.ifPresent((p_298022_) -> {
            jsonobject.add("item", p_298022_.serializeToJson());
         });
         return jsonobject;
      }
   }
}