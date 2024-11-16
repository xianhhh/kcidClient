package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class KilledTrigger extends SimpleCriterionTrigger<KilledTrigger.TriggerInstance> {
   public KilledTrigger.TriggerInstance createInstance(JsonObject p_286846_, Optional<ContextAwarePredicate> p_298055_, DeserializationContext p_286257_) {
      return new KilledTrigger.TriggerInstance(p_298055_, EntityPredicate.fromJson(p_286846_, "entity", p_286257_), DamageSourcePredicate.fromJson(p_286846_.get("killing_blow")));
   }

   public void trigger(ServerPlayer p_48105_, Entity p_48106_, DamageSource p_48107_) {
      LootContext lootcontext = EntityPredicate.createContext(p_48105_, p_48106_);
      this.trigger(p_48105_, (p_48112_) -> {
         return p_48112_.matches(p_48105_, lootcontext, p_48107_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ContextAwarePredicate> entityPredicate;
      private final Optional<DamageSourcePredicate> killingBlow;

      public TriggerInstance(Optional<ContextAwarePredicate> p_300998_, Optional<ContextAwarePredicate> p_300514_, Optional<DamageSourcePredicate> p_297546_) {
         super(p_300998_);
         this.entityPredicate = p_300514_;
         this.killingBlow = p_297546_;
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntity(Optional<EntityPredicate> p_299523_) {
         return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_299523_), Optional.empty()));
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntity(EntityPredicate.Builder p_48137_) {
         return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(p_48137_)), Optional.empty()));
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntity() {
         return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntity(Optional<EntityPredicate> p_299572_, Optional<DamageSourcePredicate> p_297245_) {
         return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_299572_), p_297245_));
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntity(EntityPredicate.Builder p_152106_, Optional<DamageSourcePredicate> p_297683_) {
         return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(p_152106_)), p_297683_));
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntity(Optional<EntityPredicate> p_300641_, DamageSourcePredicate.Builder p_300954_) {
         return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_300641_), Optional.of(p_300954_.build())));
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntity(EntityPredicate.Builder p_300999_, DamageSourcePredicate.Builder p_298768_) {
         return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(p_300999_)), Optional.of(p_298768_.build())));
      }

      public static Criterion<KilledTrigger.TriggerInstance> playerKilledEntityNearSculkCatalyst() {
         return CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
      }

      public static Criterion<KilledTrigger.TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> p_300543_) {
         return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_300543_), Optional.empty()));
      }

      public static Criterion<KilledTrigger.TriggerInstance> entityKilledPlayer(EntityPredicate.Builder p_300131_) {
         return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(p_300131_)), Optional.empty()));
      }

      public static Criterion<KilledTrigger.TriggerInstance> entityKilledPlayer() {
         return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
      }

      public static Criterion<KilledTrigger.TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> p_297719_, Optional<DamageSourcePredicate> p_298112_) {
         return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_297719_), p_298112_));
      }

      public static Criterion<KilledTrigger.TriggerInstance> entityKilledPlayer(EntityPredicate.Builder p_298074_, Optional<DamageSourcePredicate> p_300879_) {
         return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(p_298074_)), p_300879_));
      }

      public static Criterion<KilledTrigger.TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> p_297520_, DamageSourcePredicate.Builder p_299317_) {
         return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_297520_), Optional.of(p_299317_.build())));
      }

      public static Criterion<KilledTrigger.TriggerInstance> entityKilledPlayer(EntityPredicate.Builder p_152122_, DamageSourcePredicate.Builder p_299947_) {
         return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(p_152122_)), Optional.of(p_299947_.build())));
      }

      public boolean matches(ServerPlayer p_48131_, LootContext p_48132_, DamageSource p_48133_) {
         if (this.killingBlow.isPresent() && !this.killingBlow.get().matches(p_48131_, p_48133_)) {
            return false;
         } else {
            return this.entityPredicate.isEmpty() || this.entityPredicate.get().matches(p_48132_);
         }
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.entityPredicate.ifPresent((p_301388_) -> {
            jsonobject.add("entity", p_301388_.toJson());
         });
         this.killingBlow.ifPresent((p_299547_) -> {
            jsonobject.add("killing_blow", p_299547_.serializeToJson());
         });
         return jsonobject;
      }
   }
}