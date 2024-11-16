package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerInteractTrigger extends SimpleCriterionTrigger<PlayerInteractTrigger.TriggerInstance> {
   protected PlayerInteractTrigger.TriggerInstance createInstance(JsonObject p_286758_, Optional<ContextAwarePredicate> p_299829_, DeserializationContext p_286859_) {
      Optional<ItemPredicate> optional = ItemPredicate.fromJson(p_286758_.get("item"));
      Optional<ContextAwarePredicate> optional1 = EntityPredicate.fromJson(p_286758_, "entity", p_286859_);
      return new PlayerInteractTrigger.TriggerInstance(p_299829_, optional, optional1);
   }

   public void trigger(ServerPlayer p_61495_, ItemStack p_61496_, Entity p_61497_) {
      LootContext lootcontext = EntityPredicate.createContext(p_61495_, p_61497_);
      this.trigger(p_61495_, (p_61501_) -> {
         return p_61501_.matches(p_61496_, lootcontext);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ItemPredicate> item;
      private final Optional<ContextAwarePredicate> entity;

      public TriggerInstance(Optional<ContextAwarePredicate> p_298930_, Optional<ItemPredicate> p_300805_, Optional<ContextAwarePredicate> p_299935_) {
         super(p_298930_);
         this.item = p_300805_;
         this.entity = p_299935_;
      }

      public static Criterion<PlayerInteractTrigger.TriggerInstance> itemUsedOnEntity(Optional<ContextAwarePredicate> p_297673_, ItemPredicate.Builder p_286235_, Optional<ContextAwarePredicate> p_301321_) {
         return CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.createCriterion(new PlayerInteractTrigger.TriggerInstance(p_297673_, Optional.of(p_286235_.build()), p_301321_));
      }

      public static Criterion<PlayerInteractTrigger.TriggerInstance> itemUsedOnEntity(ItemPredicate.Builder p_286289_, Optional<ContextAwarePredicate> p_297754_) {
         return itemUsedOnEntity(Optional.empty(), p_286289_, p_297754_);
      }

      public boolean matches(ItemStack p_61522_, LootContext p_61523_) {
         if (this.item.isPresent() && !this.item.get().matches(p_61522_)) {
            return false;
         } else {
            return this.entity.isEmpty() || this.entity.get().matches(p_61523_);
         }
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.item.ifPresent((p_298601_) -> {
            jsonobject.add("item", p_298601_.serializeToJson());
         });
         this.entity.ifPresent((p_299865_) -> {
            jsonobject.add("entity", p_299865_.toJson());
         });
         return jsonobject;
      }
   }
}