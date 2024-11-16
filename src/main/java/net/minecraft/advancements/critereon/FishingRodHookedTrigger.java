package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class FishingRodHookedTrigger extends SimpleCriterionTrigger<FishingRodHookedTrigger.TriggerInstance> {
   public FishingRodHookedTrigger.TriggerInstance createInstance(JsonObject p_286600_, Optional<ContextAwarePredicate> p_299123_, DeserializationContext p_286299_) {
      Optional<ItemPredicate> optional = ItemPredicate.fromJson(p_286600_.get("rod"));
      Optional<ContextAwarePredicate> optional1 = EntityPredicate.fromJson(p_286600_, "entity", p_286299_);
      Optional<ItemPredicate> optional2 = ItemPredicate.fromJson(p_286600_.get("item"));
      return new FishingRodHookedTrigger.TriggerInstance(p_299123_, optional, optional1, optional2);
   }

   public void trigger(ServerPlayer p_40417_, ItemStack p_40418_, FishingHook p_40419_, Collection<ItemStack> p_40420_) {
      LootContext lootcontext = EntityPredicate.createContext(p_40417_, (Entity)(p_40419_.getHookedIn() != null ? p_40419_.getHookedIn() : p_40419_));
      this.trigger(p_40417_, (p_40425_) -> {
         return p_40425_.matches(p_40418_, lootcontext, p_40420_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Optional<ItemPredicate> rod;
      private final Optional<ContextAwarePredicate> entity;
      private final Optional<ItemPredicate> item;

      public TriggerInstance(Optional<ContextAwarePredicate> p_297705_, Optional<ItemPredicate> p_298398_, Optional<ContextAwarePredicate> p_297250_, Optional<ItemPredicate> p_298108_) {
         super(p_297705_);
         this.rod = p_298398_;
         this.entity = p_297250_;
         this.item = p_298108_;
      }

      public static Criterion<FishingRodHookedTrigger.TriggerInstance> fishedItem(Optional<ItemPredicate> p_300012_, Optional<EntityPredicate> p_297455_, Optional<ItemPredicate> p_297238_) {
         return CriteriaTriggers.FISHING_ROD_HOOKED.createCriterion(new FishingRodHookedTrigger.TriggerInstance(Optional.empty(), p_300012_, EntityPredicate.wrap(p_297455_), p_297238_));
      }

      public boolean matches(ItemStack p_40444_, LootContext p_40445_, Collection<ItemStack> p_40446_) {
         if (this.rod.isPresent() && !this.rod.get().matches(p_40444_)) {
            return false;
         } else if (this.entity.isPresent() && !this.entity.get().matches(p_40445_)) {
            return false;
         } else {
            if (this.item.isPresent()) {
               boolean flag = false;
               Entity entity = p_40445_.getParamOrNull(LootContextParams.THIS_ENTITY);
               if (entity instanceof ItemEntity) {
                  ItemEntity itementity = (ItemEntity)entity;
                  if (this.item.get().matches(itementity.getItem())) {
                     flag = true;
                  }
               }

               for(ItemStack itemstack : p_40446_) {
                  if (this.item.get().matches(itemstack)) {
                     flag = true;
                     break;
                  }
               }

               if (!flag) {
                  return false;
               }
            }

            return true;
         }
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         this.rod.ifPresent((p_299516_) -> {
            jsonobject.add("rod", p_299516_.serializeToJson());
         });
         this.entity.ifPresent((p_297815_) -> {
            jsonobject.add("entity", p_297815_.toJson());
         });
         this.item.ifPresent((p_297690_) -> {
            jsonobject.add("item", p_297690_.serializeToJson());
         });
         return jsonobject;
      }
   }
}