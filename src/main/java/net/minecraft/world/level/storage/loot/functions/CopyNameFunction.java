package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CopyNameFunction extends LootItemConditionalFunction {
   public static final Codec<CopyNameFunction> CODEC = RecordCodecBuilder.create((p_297078_) -> {
      return commonFields(p_297078_).and(CopyNameFunction.NameSource.CODEC.fieldOf("source").forGetter((p_297077_) -> {
         return p_297077_.source;
      })).apply(p_297078_, CopyNameFunction::new);
   });
   private final CopyNameFunction.NameSource source;

   private CopyNameFunction(List<LootItemCondition> p_300985_, CopyNameFunction.NameSource p_80178_) {
      super(p_300985_);
      this.source = p_80178_;
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.COPY_NAME;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return ImmutableSet.of(this.source.param);
   }

   public ItemStack run(ItemStack p_80185_, LootContext p_80186_) {
      Object object = p_80186_.getParamOrNull(this.source.param);
      if (object instanceof Nameable nameable) {
         if (nameable.hasCustomName()) {
            p_80185_.setHoverName(nameable.getDisplayName());
         }
      }

      return p_80185_;
   }

   public static LootItemConditionalFunction.Builder<?> copyName(CopyNameFunction.NameSource p_80188_) {
      return simpleBuilder((p_297080_) -> {
         return new CopyNameFunction(p_297080_, p_80188_);
      });
   }

   public static enum NameSource implements StringRepresentable {
      THIS("this", LootContextParams.THIS_ENTITY),
      KILLER("killer", LootContextParams.KILLER_ENTITY),
      KILLER_PLAYER("killer_player", LootContextParams.LAST_DAMAGE_PLAYER),
      BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY);

      public static final Codec<CopyNameFunction.NameSource> CODEC = StringRepresentable.fromEnum(CopyNameFunction.NameSource::values);
      private final String name;
      final LootContextParam<?> param;

      private NameSource(String p_80206_, LootContextParam<?> p_80207_) {
         this.name = p_80206_;
         this.param = p_80207_;
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}