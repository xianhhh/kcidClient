package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class ChanneledLightningTrigger extends SimpleCriterionTrigger<ChanneledLightningTrigger.TriggerInstance> {
   public ChanneledLightningTrigger.TriggerInstance createInstance(JsonObject p_286659_, Optional<ContextAwarePredicate> p_298475_, DeserializationContext p_286807_) {
      List<ContextAwarePredicate> list = EntityPredicate.fromJsonArray(p_286659_, "victims", p_286807_);
      return new ChanneledLightningTrigger.TriggerInstance(p_298475_, list);
   }

   public void trigger(ServerPlayer p_21722_, Collection<? extends Entity> p_21723_) {
      List<LootContext> list = p_21723_.stream().map((p_21720_) -> {
         return EntityPredicate.createContext(p_21722_, p_21720_);
      }).collect(Collectors.toList());
      this.trigger(p_21722_, (p_21730_) -> {
         return p_21730_.matches(list);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final List<ContextAwarePredicate> victims;

      public TriggerInstance(Optional<ContextAwarePredicate> p_301216_, List<ContextAwarePredicate> p_299482_) {
         super(p_301216_);
         this.victims = p_299482_;
      }

      public static Criterion<ChanneledLightningTrigger.TriggerInstance> channeledLightning(EntityPredicate.Builder... p_299370_) {
         return CriteriaTriggers.CHANNELED_LIGHTNING.createCriterion(new ChanneledLightningTrigger.TriggerInstance(Optional.empty(), EntityPredicate.wrap(p_299370_)));
      }

      public boolean matches(Collection<? extends LootContext> p_21745_) {
         for(ContextAwarePredicate contextawarepredicate : this.victims) {
            boolean flag = false;

            for(LootContext lootcontext : p_21745_) {
               if (contextawarepredicate.matches(lootcontext)) {
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

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         jsonobject.add("victims", ContextAwarePredicate.toJson(this.victims));
         return jsonobject;
      }
   }
}