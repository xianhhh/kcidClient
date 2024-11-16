package net.minecraft.world.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;

class BadOmenMobEffect extends MobEffect {
   protected BadOmenMobEffect(MobEffectCategory p_298574_, int p_301000_) {
      super(p_298574_, p_301000_);
   }

   public boolean shouldApplyEffectTickThisTick(int p_297444_, int p_300866_) {
      return true;
   }

   public void applyEffectTick(LivingEntity p_299568_, int p_299125_) {
      super.applyEffectTick(p_299568_, p_299125_);
      if (p_299568_ instanceof ServerPlayer serverplayer) {
         if (!p_299568_.isSpectator()) {
            ServerLevel serverlevel = serverplayer.serverLevel();
            if (serverlevel.getDifficulty() == Difficulty.PEACEFUL) {
               return;
            }

            if (serverlevel.isVillage(p_299568_.blockPosition())) {
               serverlevel.getRaids().createOrExtendRaid(serverplayer);
            }
         }
      }

   }
}