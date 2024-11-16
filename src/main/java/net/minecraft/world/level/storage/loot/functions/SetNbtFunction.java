package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetNbtFunction extends LootItemConditionalFunction {
   public static final Codec<SetNbtFunction> CODEC = RecordCodecBuilder.create((p_297169_) -> {
      return commonFields(p_297169_).and(TagParser.AS_CODEC.fieldOf("tag").forGetter((p_297166_) -> {
         return p_297166_.tag;
      })).apply(p_297169_, SetNbtFunction::new);
   });
   private final CompoundTag tag;

   private SetNbtFunction(List<LootItemCondition> p_299790_, CompoundTag p_81177_) {
      super(p_299790_);
      this.tag = p_81177_;
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.SET_NBT;
   }

   public ItemStack run(ItemStack p_81183_, LootContext p_81184_) {
      p_81183_.getOrCreateTag().merge(this.tag);
      return p_81183_;
   }

   /** @deprecated */
   @Deprecated
   public static LootItemConditionalFunction.Builder<?> setTag(CompoundTag p_81188_) {
      return simpleBuilder((p_297168_) -> {
         return new SetNbtFunction(p_297168_, p_81188_);
      });
   }
}