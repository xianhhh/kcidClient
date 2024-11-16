package net.minecraft.world.level.storage.loot.entries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class CompositeEntryBase extends LootPoolEntryContainer {
   protected final List<LootPoolEntryContainer> children;
   private final ComposableEntryContainer composedChildren;

   protected CompositeEntryBase(List<LootPoolEntryContainer> p_299424_, List<LootItemCondition> p_299955_) {
      super(p_299955_);
      this.children = p_299424_;
      this.composedChildren = this.compose(p_299424_);
   }

   public void validate(ValidationContext p_79434_) {
      super.validate(p_79434_);
      if (this.children.isEmpty()) {
         p_79434_.reportProblem("Empty children list");
      }

      for(int i = 0; i < this.children.size(); ++i) {
         this.children.get(i).validate(p_79434_.forChild(".entry[" + i + "]"));
      }

   }

   protected abstract ComposableEntryContainer compose(List<? extends ComposableEntryContainer> p_298994_);

   public final boolean expand(LootContext p_79439_, Consumer<LootPoolEntry> p_79440_) {
      return !this.canRun(p_79439_) ? false : this.composedChildren.expand(p_79439_, p_79440_);
   }

   public static <T extends CompositeEntryBase> Codec<T> createCodec(CompositeEntryBase.CompositeEntryConstructor<T> p_300261_) {
      return RecordCodecBuilder.create((p_299098_) -> {
         return p_299098_.group(ExtraCodecs.strictOptionalField(LootPoolEntries.CODEC.listOf(), "children", List.of()).forGetter((p_300130_) -> {
            return p_300130_.children;
         })).and(commonFields(p_299098_).t1()).apply(p_299098_, p_300261_::create);
      });
   }

   @FunctionalInterface
   public interface CompositeEntryConstructor<T extends CompositeEntryBase> {
      T create(List<LootPoolEntryContainer> p_297889_, List<LootItemCondition> p_300348_);
   }
}