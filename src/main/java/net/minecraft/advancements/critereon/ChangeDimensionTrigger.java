package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

public class ChangeDimensionTrigger extends SimpleCriterionTrigger<ChangeDimensionTrigger.TriggerInstance> {
   public ChangeDimensionTrigger.TriggerInstance createInstance(JsonObject p_19762_, Optional<ContextAwarePredicate> p_299300_, DeserializationContext p_19764_) {
      ResourceKey<Level> resourcekey = p_19762_.has("from") ? ResourceKey.create(Registries.DIMENSION, new ResourceLocation(GsonHelper.getAsString(p_19762_, "from"))) : null;
      ResourceKey<Level> resourcekey1 = p_19762_.has("to") ? ResourceKey.create(Registries.DIMENSION, new ResourceLocation(GsonHelper.getAsString(p_19762_, "to"))) : null;
      return new ChangeDimensionTrigger.TriggerInstance(p_299300_, resourcekey, resourcekey1);
   }

   public void trigger(ServerPlayer p_19758_, ResourceKey<Level> p_19759_, ResourceKey<Level> p_19760_) {
      this.trigger(p_19758_, (p_19768_) -> {
         return p_19768_.matches(p_19759_, p_19760_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      @Nullable
      private final ResourceKey<Level> from;
      @Nullable
      private final ResourceKey<Level> to;

      public TriggerInstance(Optional<ContextAwarePredicate> p_297290_, @Nullable ResourceKey<Level> p_286585_, @Nullable ResourceKey<Level> p_286666_) {
         super(p_297290_);
         this.from = p_286585_;
         this.to = p_286666_;
      }

      public static Criterion<ChangeDimensionTrigger.TriggerInstance> changedDimension() {
         return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new ChangeDimensionTrigger.TriggerInstance(Optional.empty(), (ResourceKey<Level>)null, (ResourceKey<Level>)null));
      }

      public static Criterion<ChangeDimensionTrigger.TriggerInstance> changedDimension(ResourceKey<Level> p_301176_, ResourceKey<Level> p_298639_) {
         return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new ChangeDimensionTrigger.TriggerInstance(Optional.empty(), p_301176_, p_298639_));
      }

      public static Criterion<ChangeDimensionTrigger.TriggerInstance> changedDimensionTo(ResourceKey<Level> p_19783_) {
         return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new ChangeDimensionTrigger.TriggerInstance(Optional.empty(), (ResourceKey<Level>)null, p_19783_));
      }

      public static Criterion<ChangeDimensionTrigger.TriggerInstance> changedDimensionFrom(ResourceKey<Level> p_147564_) {
         return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new ChangeDimensionTrigger.TriggerInstance(Optional.empty(), p_147564_, (ResourceKey<Level>)null));
      }

      public boolean matches(ResourceKey<Level> p_19785_, ResourceKey<Level> p_19786_) {
         if (this.from != null && this.from != p_19785_) {
            return false;
         } else {
            return this.to == null || this.to == p_19786_;
         }
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         if (this.from != null) {
            jsonobject.addProperty("from", this.from.location().toString());
         }

         if (this.to != null) {
            jsonobject.addProperty("to", this.to.location().toString());
         }

         return jsonobject;
      }
   }
}