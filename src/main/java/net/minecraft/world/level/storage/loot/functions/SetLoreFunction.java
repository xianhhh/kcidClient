package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetLoreFunction extends LootItemConditionalFunction {
   public static final Codec<SetLoreFunction> CODEC = RecordCodecBuilder.create((p_300865_) -> {
      return commonFields(p_300865_).and(p_300865_.group(Codec.BOOL.fieldOf("replace").orElse(false).forGetter((p_301107_) -> {
         return p_301107_.replace;
      }), ExtraCodecs.COMPONENT.listOf().fieldOf("lore").forGetter((p_300292_) -> {
         return p_300292_.lore;
      }), ExtraCodecs.strictOptionalField(LootContext.EntityTarget.CODEC, "entity").forGetter((p_300757_) -> {
         return p_300757_.resolutionContext;
      }))).apply(p_300865_, SetLoreFunction::new);
   });
   private final boolean replace;
   private final List<Component> lore;
   private final Optional<LootContext.EntityTarget> resolutionContext;

   public SetLoreFunction(List<LootItemCondition> p_81085_, boolean p_81084_, List<Component> p_300257_, Optional<LootContext.EntityTarget> p_301400_) {
      super(p_81085_);
      this.replace = p_81084_;
      this.lore = List.copyOf(p_300257_);
      this.resolutionContext = p_301400_;
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.SET_LORE;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return (Set<LootContextParam<?>>)(Set)this.resolutionContext.map((p_298916_) -> {
         return Set.of(p_298916_.getParam());
      }).orElseGet(Set::of);
   }

   public ItemStack run(ItemStack p_81089_, LootContext p_81090_) {
      ListTag listtag = this.getLoreTag(p_81089_, !this.lore.isEmpty());
      if (listtag != null) {
         if (this.replace) {
            listtag.clear();
         }

         UnaryOperator<Component> unaryoperator = SetNameFunction.createResolver(p_81090_, this.resolutionContext.orElse((LootContext.EntityTarget)null));
         this.lore.stream().map(unaryoperator).map(Component.Serializer::toJson).map(StringTag::valueOf).forEach(listtag::add);
      }

      return p_81089_;
   }

   @Nullable
   private ListTag getLoreTag(ItemStack p_81092_, boolean p_81093_) {
      CompoundTag compoundtag;
      if (p_81092_.hasTag()) {
         compoundtag = p_81092_.getTag();
      } else {
         if (!p_81093_) {
            return null;
         }

         compoundtag = new CompoundTag();
         p_81092_.setTag(compoundtag);
      }

      CompoundTag compoundtag1;
      if (compoundtag.contains("display", 10)) {
         compoundtag1 = compoundtag.getCompound("display");
      } else {
         if (!p_81093_) {
            return null;
         }

         compoundtag1 = new CompoundTag();
         compoundtag.put("display", compoundtag1);
      }

      if (compoundtag1.contains("Lore", 9)) {
         return compoundtag1.getList("Lore", 8);
      } else if (p_81093_) {
         ListTag listtag = new ListTag();
         compoundtag1.put("Lore", listtag);
         return listtag;
      } else {
         return null;
      }
   }

   public static SetLoreFunction.Builder setLore() {
      return new SetLoreFunction.Builder();
   }

   public static class Builder extends LootItemConditionalFunction.Builder<SetLoreFunction.Builder> {
      private boolean replace;
      private Optional<LootContext.EntityTarget> resolutionContext = Optional.empty();
      private final ImmutableList.Builder<Component> lore = ImmutableList.builder();

      public SetLoreFunction.Builder setReplace(boolean p_165454_) {
         this.replace = p_165454_;
         return this;
      }

      public SetLoreFunction.Builder setResolutionContext(LootContext.EntityTarget p_165450_) {
         this.resolutionContext = Optional.of(p_165450_);
         return this;
      }

      public SetLoreFunction.Builder addLine(Component p_165452_) {
         this.lore.add(p_165452_);
         return this;
      }

      protected SetLoreFunction.Builder getThis() {
         return this;
      }

      public LootItemFunction build() {
         return new SetLoreFunction(this.getConditions(), this.replace, this.lore.build(), this.resolutionContext);
      }
   }
}
