package net.minecraft.world.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

public class SuspiciousStewItem extends Item {
   public static final String EFFECTS_TAG = "effects";
   public static final int DEFAULT_DURATION = 160;

   public SuspiciousStewItem(Item.Properties p_43257_) {
      super(p_43257_);
   }

   public static void saveMobEffects(ItemStack p_298817_, List<SuspiciousEffectHolder.EffectEntry> p_301117_) {
      CompoundTag compoundtag = p_298817_.getOrCreateTag();
      SuspiciousEffectHolder.EffectEntry.LIST_CODEC.encodeStart(NbtOps.INSTANCE, p_301117_).result().ifPresent((p_298613_) -> {
         compoundtag.put("effects", p_298613_);
      });
   }

   public static void appendMobEffects(ItemStack p_298473_, List<SuspiciousEffectHolder.EffectEntry> p_301341_) {
      CompoundTag compoundtag = p_298473_.getOrCreateTag();
      List<SuspiciousEffectHolder.EffectEntry> list = new ArrayList<>();
      listPotionEffects(p_298473_, list::add);
      list.addAll(p_301341_);
      SuspiciousEffectHolder.EffectEntry.LIST_CODEC.encodeStart(NbtOps.INSTANCE, list).result().ifPresent((p_299906_) -> {
         compoundtag.put("effects", p_299906_);
      });
   }

   private static void listPotionEffects(ItemStack p_260126_, Consumer<SuspiciousEffectHolder.EffectEntry> p_259500_) {
      CompoundTag compoundtag = p_260126_.getTag();
      if (compoundtag != null && compoundtag.contains("effects", 9)) {
         SuspiciousEffectHolder.EffectEntry.LIST_CODEC.parse(NbtOps.INSTANCE, compoundtag.getList("effects", 10)).result().ifPresent((p_299369_) -> {
            p_299369_.forEach(p_259500_);
         });
      }

   }

   public void appendHoverText(ItemStack p_260314_, @Nullable Level p_259224_, List<Component> p_259700_, TooltipFlag p_260021_) {
      super.appendHoverText(p_260314_, p_259224_, p_259700_, p_260021_);
      if (p_260021_.isCreative()) {
         List<MobEffectInstance> list = new ArrayList<>();
         listPotionEffects(p_260314_, (p_297468_) -> {
            list.add(p_297468_.createEffectInstance());
         });
         PotionUtils.addPotionTooltip(list, p_259700_, 1.0F);
      }

   }

   public ItemStack finishUsingItem(ItemStack p_43263_, Level p_43264_, LivingEntity p_43265_) {
      ItemStack itemstack = super.finishUsingItem(p_43263_, p_43264_, p_43265_);
      listPotionEffects(itemstack, (p_300365_) -> {
         p_43265_.addEffect(p_300365_.createEffectInstance());
      });
      return p_43265_ instanceof Player && ((Player)p_43265_).getAbilities().instabuild ? itemstack : new ItemStack(Items.BOWL);
   }
}