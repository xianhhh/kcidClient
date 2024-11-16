package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CopyBlockState extends LootItemConditionalFunction {
   public static final Codec<CopyBlockState> CODEC = RecordCodecBuilder.create((p_297076_) -> {
      return commonFields(p_297076_).and(p_297076_.group(BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter((p_297074_) -> {
         return p_297074_.block;
      }), Codec.STRING.listOf().fieldOf("properties").forGetter((p_297075_) -> {
         return p_297075_.properties.stream().map(Property::getName).toList();
      }))).apply(p_297076_, CopyBlockState::new);
   });
   private final Holder<Block> block;
   private final Set<Property<?>> properties;

   CopyBlockState(List<LootItemCondition> p_301076_, Holder<Block> p_298008_, Set<Property<?>> p_80052_) {
      super(p_301076_);
      this.block = p_298008_;
      this.properties = p_80052_;
   }

   private CopyBlockState(List<LootItemCondition> p_297498_, Holder<Block> p_299449_, List<String> p_298231_) {
      this(p_297498_, p_299449_, p_298231_.stream().map(p_299449_.value().getStateDefinition()::getProperty).filter(Objects::nonNull).collect(Collectors.toSet()));
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.COPY_STATE;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return ImmutableSet.of(LootContextParams.BLOCK_STATE);
   }

   protected ItemStack run(ItemStack p_80060_, LootContext p_80061_) {
      BlockState blockstate = p_80061_.getParamOrNull(LootContextParams.BLOCK_STATE);
      if (blockstate != null) {
         CompoundTag compoundtag = p_80060_.getOrCreateTag();
         CompoundTag compoundtag1;
         if (compoundtag.contains("BlockStateTag", 10)) {
            compoundtag1 = compoundtag.getCompound("BlockStateTag");
         } else {
            compoundtag1 = new CompoundTag();
            compoundtag.put("BlockStateTag", compoundtag1);
         }

         for(Property<?> property : this.properties) {
            if (blockstate.hasProperty(property)) {
               compoundtag1.putString(property.getName(), serialize(blockstate, property));
            }
         }
      }

      return p_80060_;
   }

   public static CopyBlockState.Builder copyState(Block p_80063_) {
      return new CopyBlockState.Builder(p_80063_);
   }

   private static <T extends Comparable<T>> String serialize(BlockState p_80065_, Property<T> p_80066_) {
      T t = p_80065_.getValue(p_80066_);
      return p_80066_.getName(t);
   }

   public static class Builder extends LootItemConditionalFunction.Builder<CopyBlockState.Builder> {
      private final Holder<Block> block;
      private final ImmutableSet.Builder<Property<?>> properties = ImmutableSet.builder();

      Builder(Block p_80079_) {
         this.block = p_80079_.builtInRegistryHolder();
      }

      public CopyBlockState.Builder copy(Property<?> p_80085_) {
         if (!this.block.value().getStateDefinition().getProperties().contains(p_80085_)) {
            throw new IllegalStateException("Property " + p_80085_ + " is not present on block " + this.block);
         } else {
            this.properties.add(p_80085_);
            return this;
         }
      }

      protected CopyBlockState.Builder getThis() {
         return this;
      }

      public LootItemFunction build() {
         return new CopyBlockState(this.getConditions(), this.block, this.properties.build());
      }
   }
}