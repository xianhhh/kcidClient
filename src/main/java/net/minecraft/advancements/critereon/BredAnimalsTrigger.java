package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class BredAnimalsTrigger extends SimpleCriterionTrigger<BredAnimalsTrigger.TriggerInstance> {
   public BredAnimalsTrigger.TriggerInstance createInstance(JsonObject p_286232_, Optional<ContextAwarePredicate> p_297942_, DeserializationContext p_286439_) {
      Optional<ContextAwarePredicate> optional = EntityPredicate.fromJson(p_286232_, "parent", p_286439_);
      Optional<ContextAwarePredicate> optional1 = EntityPredicate.fromJson(p_286232_, "partner", p_286439_);
      Optional<ContextAwarePredicate> optional2 = EntityPredicate.fromJson(p_286232_, "child", p_286439_);
      return new BredAnimalsTrigger.TriggerInstance(p_297942_, optional, optional1, optional2);
   }

   public void trigger(ServerPlayer p_147279_, Animal p_147280_, Animal p_147281_, @Nullable AgeableMob p_147282_) {
      LootContext lootcontext = EntityPredicate.createContext(p_147279_, p_147280_);
      LootContext lootcontext1 = EntityPredicate.createContext(p_147279_, p_147281_);
      LootContext lootcontext2 = p_147282_ != null ? EntityPredicate.createContext(p_147279_, p_147282_) : null;
      this.trigger(p_147279_, (p_18653_) -> {
         return p_18653_.matches(lootcontext, lootcontext1, lootcontext2);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ContextAwarePredicate> parent;
      private final Optional<ContextAwarePredicate> partner;
      private final Optional<ContextAwarePredicate> child;

      public TriggerInstance(Optional<ContextAwarePredicate> p_300648_, Optional<ContextAwarePredicate> p_299824_, Optional<ContextAwarePredicate> p_299138_, Optional<ContextAwarePredicate> p_297646_) {
         super(p_300648_);
         this.parent = p_299824_;
         this.partner = p_299138_;
         this.child = p_297646_;
      }

      public static Criterion<BredAnimalsTrigger.TriggerInstance> bredAnimals() {
         return CriteriaTriggers.BRED_ANIMALS.createCriterion(new BredAnimalsTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
      }

      public static Criterion<BredAnimalsTrigger.TriggerInstance> bredAnimals(EntityPredicate.Builder p_18668_) {
         return CriteriaTriggers.BRED_ANIMALS.createCriterion(new BredAnimalsTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(p_18668_))));
      }

      public static Criterion<BredAnimalsTrigger.TriggerInstance> bredAnimals(Optional<EntityPredicate> p_298213_, Optional<EntityPredicate> p_299258_, Optional<EntityPredicate> p_297439_) {
         return CriteriaTriggers.BRED_ANIMALS.createCriterion(new BredAnimalsTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_298213_), EntityPredicate.wrap(p_299258_), EntityPredicate.wrap(p_297439_)));
      }

      public boolean matches(LootContext p_18676_, LootContext p_18677_, @Nullable LootContext p_18678_) {
         if (!this.child.isPresent() || p_18678_ != null && this.child.get().matches(p_18678_)) {
            return matches(this.parent, p_18676_) && matches(this.partner, p_18677_) || matches(this.parent, p_18677_) && matches(this.partner, p_18676_);
         } else {
            return false;
         }
      }

      private static boolean matches(Optional<ContextAwarePredicate> p_300266_, LootContext p_300903_) {
         return p_300266_.isEmpty() || p_300266_.get().matches(p_300903_);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.parent.ifPresent((p_297504_) -> {
            jsonobject.add("parent", p_297504_.toJson());
         });
         this.partner.ifPresent((p_301179_) -> {
            jsonobject.add("partner", p_301179_.toJson());
         });
         this.child.ifPresent((p_298465_) -> {
            jsonobject.add("child", p_298465_.toJson());
         });
         return jsonobject;
      }
   }
}