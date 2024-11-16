package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import java.util.Optional;

public class ZombieVillagerRebuildXpFix extends NamedEntityFix {
   public ZombieVillagerRebuildXpFix(Schema p_17298_, boolean p_17299_) {
      super(p_17298_, p_17299_, "Zombie Villager XP rebuild", References.ENTITY, "minecraft:zombie_villager");
   }

   protected Typed<?> fix(Typed<?> p_17301_) {
      return p_17301_.update(DSL.remainderFinder(), (p_296642_) -> {
         Optional<Number> optional = p_296642_.get("Xp").asNumber().result();
         if (optional.isEmpty()) {
            int i = p_296642_.get("VillagerData").get("level").asInt(1);
            return p_296642_.set("Xp", p_296642_.createInt(VillagerRebuildLevelAndXpFix.getMinXpPerLevel(i)));
         } else {
            return p_296642_;
         }
      });
   }
}