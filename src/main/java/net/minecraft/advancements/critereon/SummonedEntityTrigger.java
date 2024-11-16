package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class SummonedEntityTrigger extends SimpleCriterionTrigger<SummonedEntityTrigger.TriggerInstance> {
   public SummonedEntityTrigger.TriggerInstance createInstance(JsonObject p_286898_, Optional<ContextAwarePredicate> p_301005_, DeserializationContext p_286829_) {
      Optional<ContextAwarePredicate> optional = EntityPredicate.fromJson(p_286898_, "entity", p_286829_);
      return new SummonedEntityTrigger.TriggerInstance(p_301005_, optional);
   }

   public void trigger(ServerPlayer p_68257_, Entity p_68258_) {
      LootContext lootcontext = EntityPredicate.createContext(p_68257_, p_68258_);
      this.trigger(p_68257_, (p_68265_) -> {
         return p_68265_.matches(lootcontext);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ContextAwarePredicate> entity;

      public TriggerInstance(Optional<ContextAwarePredicate> p_298549_, Optional<ContextAwarePredicate> p_297625_) {
         super(p_298549_);
         this.entity = p_297625_;
      }

      public static Criterion<SummonedEntityTrigger.TriggerInstance> summonedEntity(EntityPredicate.Builder p_68276_) {
         return CriteriaTriggers.SUMMONED_ENTITY.createCriterion(new SummonedEntityTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(p_68276_))));
      }

      public boolean matches(LootContext p_68280_) {
         return this.entity.isEmpty() || this.entity.get().matches(p_68280_);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.entity.ifPresent((p_298872_) -> {
            jsonobject.add("entity", p_298872_.toJson());
         });
         return jsonobject;
      }
   }
}