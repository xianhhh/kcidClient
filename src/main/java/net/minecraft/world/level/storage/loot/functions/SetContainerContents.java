package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetContainerContents extends LootItemConditionalFunction {
   public static final Codec<SetContainerContents> CODEC = RecordCodecBuilder.create((p_297113_) -> {
      return commonFields(p_297113_).and(p_297113_.group(BuiltInRegistries.BLOCK_ENTITY_TYPE.holderByNameCodec().fieldOf("type").forGetter((p_297114_) -> {
         return p_297114_.type;
      }), LootPoolEntries.CODEC.listOf().fieldOf("entries").forGetter((p_297115_) -> {
         return p_297115_.entries;
      }))).apply(p_297113_, SetContainerContents::new);
   });
   private final Holder<BlockEntityType<?>> type;
   private final List<LootPoolEntryContainer> entries;

   SetContainerContents(List<LootItemCondition> p_193035_, Holder<BlockEntityType<?>> p_300015_, List<LootPoolEntryContainer> p_298786_) {
      super(p_193035_);
      this.type = p_300015_;
      this.entries = List.copyOf(p_298786_);
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.SET_CONTENTS;
   }

   public ItemStack run(ItemStack p_80911_, LootContext p_80912_) {
      if (p_80911_.isEmpty()) {
         return p_80911_;
      } else {
         NonNullList<ItemStack> nonnulllist = NonNullList.create();
         this.entries.forEach((p_80916_) -> {
            p_80916_.expand(p_80912_, (p_287573_) -> {
               p_287573_.createItemStack(LootTable.createStackSplitter(p_80912_.getLevel(), nonnulllist::add), p_80912_);
            });
         });
         CompoundTag compoundtag = new CompoundTag();
         ContainerHelper.saveAllItems(compoundtag, nonnulllist);
         CompoundTag compoundtag1 = BlockItem.getBlockEntityData(p_80911_);
         if (compoundtag1 == null) {
            compoundtag1 = compoundtag;
         } else {
            compoundtag1.merge(compoundtag);
         }

         BlockItem.setBlockEntityData(p_80911_, this.type.value(), compoundtag1);
         return p_80911_;
      }
   }

   public void validate(ValidationContext p_80918_) {
      super.validate(p_80918_);

      for(int i = 0; i < this.entries.size(); ++i) {
         this.entries.get(i).validate(p_80918_.forChild(".entry[" + i + "]"));
      }

   }

   public static SetContainerContents.Builder setContents(BlockEntityType<?> p_193037_) {
      return new SetContainerContents.Builder(p_193037_);
   }

   public static class Builder extends LootItemConditionalFunction.Builder<SetContainerContents.Builder> {
      private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
      private final BlockEntityType<?> type;

      public Builder(BlockEntityType<?> p_193040_) {
         this.type = p_193040_;
      }

      protected SetContainerContents.Builder getThis() {
         return this;
      }

      public SetContainerContents.Builder withEntry(LootPoolEntryContainer.Builder<?> p_80931_) {
         this.entries.add(p_80931_.build());
         return this;
      }

      public LootItemFunction build() {
         return new SetContainerContents(this.getConditions(), this.type.builtInRegistryHolder(), this.entries.build());
      }
   }
}