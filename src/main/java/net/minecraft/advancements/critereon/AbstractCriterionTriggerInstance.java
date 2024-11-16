package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Optional;

public abstract class AbstractCriterionTriggerInstance implements SimpleCriterionTrigger.SimpleInstance {
   private final Optional<ContextAwarePredicate> player;

   public AbstractCriterionTriggerInstance(Optional<ContextAwarePredicate> p_299445_) {
      this.player = p_299445_;
   }

   public Optional<ContextAwarePredicate> playerPredicate() {
      return this.player;
   }

   public JsonObject serializeToJson() {
      JsonObject jsonobject = new JsonObject();
      this.player.ifPresent((p_297311_) -> {
         jsonobject.add("player", p_297311_.toJson());
      });
      return jsonobject;
   }
}