package net.minecraft.world.effect;

import net.minecraft.world.entity.LivingEntity;

class AbsorptionMobEffect extends MobEffect {
   protected AbsorptionMobEffect(MobEffectCategory p_300567_, int p_300827_) {
      super(p_300567_, p_300827_);
   }

   public void applyEffectTick(LivingEntity p_298017_, int p_299434_) {
      super.applyEffectTick(p_298017_, p_299434_);
      if (p_298017_.getAbsorptionAmount() <= 0.0F && !p_298017_.level().isClientSide) {
         p_298017_.removeEffect(this);
      }

   }

   public boolean shouldApplyEffectTickThisTick(int p_299365_, int p_298390_) {
      return true;
   }

   public void onEffectStarted(LivingEntity p_298184_, int p_297925_) {
      super.onEffectStarted(p_298184_, p_297925_);
      p_298184_.setAbsorptionAmount(Math.max(p_298184_.getAbsorptionAmount(), (float)(4 * (1 + p_297925_))));
   }
}