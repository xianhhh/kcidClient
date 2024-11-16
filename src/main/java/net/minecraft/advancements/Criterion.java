package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import java.util.Objects;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public record Criterion<T extends CriterionTriggerInstance>(CriterionTrigger<T> trigger, T triggerInstance) {
   public static Criterion<?> criterionFromJson(JsonObject p_11418_, DeserializationContext p_11419_) {
      ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(p_11418_, "trigger"));
      CriterionTrigger<?> criteriontrigger = CriteriaTriggers.getCriterion(resourcelocation);
      if (criteriontrigger == null) {
         throw new JsonSyntaxException("Invalid criterion trigger: " + resourcelocation);
      } else {
         return criterionFromJson(p_11418_, p_11419_, criteriontrigger);
      }
   }

   private static <T extends CriterionTriggerInstance> Criterion<T> criterionFromJson(JsonObject p_298246_, DeserializationContext p_297334_, CriterionTrigger<T> p_300883_) {
      T t = p_300883_.createInstance(GsonHelper.getAsJsonObject(p_298246_, "conditions", new JsonObject()), p_297334_);
      return new Criterion<>(p_300883_, t);
   }

   public static Map<String, Criterion<?>> criteriaFromJson(JsonObject p_11427_, DeserializationContext p_11428_) {
      Map<String, Criterion<?>> map = Maps.newHashMap();

      for(Map.Entry<String, JsonElement> entry : p_11427_.entrySet()) {
         map.put(entry.getKey(), criterionFromJson(GsonHelper.convertToJsonObject(entry.getValue(), "criterion"), p_11428_));
      }

      return map;
   }

   public JsonElement serializeToJson() {
      JsonObject jsonobject = new JsonObject();
      jsonobject.addProperty("trigger", Objects.requireNonNull(CriteriaTriggers.getId(this.trigger), "Unregistered trigger").toString());
      JsonObject jsonobject1 = this.triggerInstance.serializeToJson();
      if (jsonobject1.size() != 0) {
         jsonobject.add("conditions", jsonobject1);
      }

      return jsonobject;
   }
}