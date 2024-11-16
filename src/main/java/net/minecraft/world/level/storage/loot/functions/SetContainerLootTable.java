package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetContainerLootTable extends LootItemConditionalFunction {
   public static final Codec<SetContainerLootTable> CODEC = RecordCodecBuilder.create((p_297121_) -> {
      return commonFields(p_297121_).and(p_297121_.group(ResourceLocation.CODEC.fieldOf("name").forGetter((p_297123_) -> {
         return p_297123_.name;
      }), ExtraCodecs.strictOptionalField(Codec.LONG, "seed", 0L).forGetter((p_297122_) -> {
         return p_297122_.seed;
      }), BuiltInRegistries.BLOCK_ENTITY_TYPE.holderByNameCodec().fieldOf("type").forGetter((p_297116_) -> {
         return p_297116_.type;
      }))).apply(p_297121_, SetContainerLootTable::new);
   });
   private final ResourceLocation name;
   private final long seed;
   private final Holder<BlockEntityType<?>> type;

   private SetContainerLootTable(List<LootItemCondition> p_297857_, ResourceLocation p_193046_, long p_193047_, Holder<BlockEntityType<?>> p_300516_) {
      super(p_297857_);
      this.name = p_193046_;
      this.seed = p_193047_;
      this.type = p_300516_;
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.SET_LOOT_TABLE;
   }

   public ItemStack run(ItemStack p_80967_, LootContext p_80968_) {
      if (p_80967_.isEmpty()) {
         return p_80967_;
      } else {
         CompoundTag compoundtag = BlockItem.getBlockEntityData(p_80967_);
         if (compoundtag == null) {
            compoundtag = new CompoundTag();
         }

         compoundtag.putString("LootTable", this.name.toString());
         if (this.seed != 0L) {
            compoundtag.putLong("LootTableSeed", this.seed);
         }

         BlockItem.setBlockEntityData(p_80967_, this.type.value(), compoundtag);
         return p_80967_;
      }
   }

   public void validate(ValidationContext p_80970_) {
      super.validate(p_80970_);
      LootDataId<LootTable> lootdataid = new LootDataId<>(LootDataType.TABLE, this.name);
      if (p_80970_.resolver().getElementOptional(lootdataid).isEmpty()) {
         p_80970_.reportProblem("Missing loot table used for container: " + this.name);
      }

   }

   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> p_193050_, ResourceLocation p_193051_) {
      return simpleBuilder((p_297126_) -> {
         return new SetContainerLootTable(p_297126_, p_193051_, 0L, p_193050_.builtInRegistryHolder());
      });
   }

   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> p_193053_, ResourceLocation p_193054_, long p_193055_) {
      return simpleBuilder((p_297120_) -> {
         return new SetContainerLootTable(p_297120_, p_193054_, p_193055_, p_193053_.builtInRegistryHolder());
      });
   }
}