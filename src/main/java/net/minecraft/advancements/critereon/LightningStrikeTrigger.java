package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.storage.loot.LootContext;

public class LightningStrikeTrigger extends SimpleCriterionTrigger<LightningStrikeTrigger.TriggerInstance> {
   public LightningStrikeTrigger.TriggerInstance createInstance(JsonObject p_286889_, Optional<ContextAwarePredicate> p_301355_, DeserializationContext p_286384_) {
      Optional<ContextAwarePredicate> optional = EntityPredicate.fromJson(p_286889_, "lightning", p_286384_);
      Optional<ContextAwarePredicate> optional1 = EntityPredicate.fromJson(p_286889_, "bystander", p_286384_);
      return new LightningStrikeTrigger.TriggerInstance(p_301355_, optional, optional1);
   }

   public void trigger(ServerPlayer p_153392_, LightningBolt p_153393_, List<Entity> p_153394_) {
      List<LootContext> list = p_153394_.stream().map((p_153390_) -> {
         return EntityPredicate.createContext(p_153392_, p_153390_);
      }).collect(Collectors.toList());
      LootContext lootcontext = EntityPredicate.createContext(p_153392_, p_153393_);
      this.trigger(p_153392_, (p_153402_) -> {
         return p_153402_.matches(lootcontext, list);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ContextAwarePredicate> lightning;
      private final Optional<ContextAwarePredicate> bystander;

      public TriggerInstance(Optional<ContextAwarePredicate> p_299191_, Optional<ContextAwarePredicate> p_297442_, Optional<ContextAwarePredicate> p_299715_) {
         super(p_299191_);
         this.lightning = p_297442_;
         this.bystander = p_299715_;
      }

      public static Criterion<LightningStrikeTrigger.TriggerInstance> lightningStrike(Optional<EntityPredicate> p_301310_, Optional<EntityPredicate> p_299336_) {
         return CriteriaTriggers.LIGHTNING_STRIKE.createCriterion(new LightningStrikeTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_301310_), EntityPredicate.wrap(p_299336_)));
      }

      public boolean matches(LootContext p_153419_, List<LootContext> p_153420_) {
         if (this.lightning.isPresent() && !this.lightning.get().matches(p_153419_)) {
            return false;
         } else {
            return !this.bystander.isPresent() || !p_153420_.stream().noneMatch(this.bystander.get()::matches);
         }
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.lightning.ifPresent((p_297794_) -> {
            jsonobject.add("lightning", p_297794_.toJson());
         });
         this.bystander.ifPresent((p_297949_) -> {
            jsonobject.add("bystander", p_297949_.toJson());
         });
         return jsonobject;
      }
   }
}