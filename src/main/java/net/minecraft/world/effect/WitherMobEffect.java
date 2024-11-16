package net.minecraft.world.effect;

import net.minecraft.world.entity.LivingEntity;

class WitherMobEffect extends MobEffect {
   protected WitherMobEffect(MobEffectCategory p_300352_, int p_298007_) {
      super(p_300352_, p_298007_);
   }

   public void applyEffectTick(LivingEntity p_299783_, int p_298645_) {
      super.applyEffectTick(p_299783_, p_298645_);
      p_299783_.hurt(p_299783_.damageSources().wither(), 1.0F);
   }

   public boolean shouldApplyEffectTickThisTick(int p_299625_, int p_297396_) {
      int i = 40 >> p_297396_;
      if (i > 0) {
         return p_299625_ % i == 0;
      } else {
         return true;
      }
   }
}