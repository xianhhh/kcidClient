package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class InventoryChangeTrigger extends SimpleCriterionTrigger<InventoryChangeTrigger.TriggerInstance> {
   public InventoryChangeTrigger.TriggerInstance createInstance(JsonObject p_286735_, Optional<ContextAwarePredicate> p_300177_, DeserializationContext p_286698_) {
      JsonObject jsonobject = GsonHelper.getAsJsonObject(p_286735_, "slots", new JsonObject());
      MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.fromJson(jsonobject.get("occupied"));
      MinMaxBounds.Ints minmaxbounds$ints1 = MinMaxBounds.Ints.fromJson(jsonobject.get("full"));
      MinMaxBounds.Ints minmaxbounds$ints2 = MinMaxBounds.Ints.fromJson(jsonobject.get("empty"));
      List<ItemPredicate> list = ItemPredicate.fromJsonArray(p_286735_.get("items"));
      return new InventoryChangeTrigger.TriggerInstance(p_300177_, minmaxbounds$ints, minmaxbounds$ints1, minmaxbounds$ints2, list);
   }

   public void trigger(ServerPlayer p_43150_, Inventory p_43151_, ItemStack p_43152_) {
      int i = 0;
      int j = 0;
      int k = 0;

      for(int l = 0; l < p_43151_.getContainerSize(); ++l) {
         ItemStack itemstack = p_43151_.getItem(l);
         if (itemstack.isEmpty()) {
            ++j;
         } else {
            ++k;
            if (itemstack.getCount() >= itemstack.getMaxStackSize()) {
               ++i;
            }
         }
      }

      this.trigger(p_43150_, p_43151_, p_43152_, i, j, k);
   }

   private void trigger(ServerPlayer p_43154_, Inventory p_43155_, ItemStack p_43156_, int p_43157_, int p_43158_, int p_43159_) {
      this.trigger(p_43154_, (p_43166_) -> {
         return p_43166_.matches(p_43155_, p_43156_, p_43157_, p_43158_, p_43159_);
      });
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final MinMaxBounds.Ints slotsOccupied;
      private final MinMaxBounds.Ints slotsFull;
      private final MinMaxBounds.Ints slotsEmpty;
      private final List<ItemPredicate> predicates;

      public TriggerInstance(Optional<ContextAwarePredicate> p_300026_, MinMaxBounds.Ints p_286313_, MinMaxBounds.Ints p_286767_, MinMaxBounds.Ints p_286601_, List<ItemPredicate> p_297907_) {
         super(p_300026_);
         this.slotsOccupied = p_286313_;
         this.slotsFull = p_286767_;
         this.slotsEmpty = p_286601_;
         this.predicates = p_297907_;
      }

      public static Criterion<InventoryChangeTrigger.TriggerInstance> hasItems(ItemPredicate.Builder... p_297239_) {
         return hasItems((ItemPredicate[])Stream.of(p_297239_).map(ItemPredicate.Builder::build).toArray((int p_296132_) -> {
            return new ItemPredicate[p_296132_];
         }));
      }

      public static Criterion<InventoryChangeTrigger.TriggerInstance> hasItems(ItemPredicate... p_43198_) {
         return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, List.of(p_43198_)));
      }

      public static Criterion<InventoryChangeTrigger.TriggerInstance> hasItems(ItemLike... p_298202_) {
         ItemPredicate[] aitempredicate = new ItemPredicate[p_298202_.length];

         for(int i = 0; i < p_298202_.length; ++i) {
            aitempredicate[i] = new ItemPredicate(Optional.empty(), Optional.of(HolderSet.direct(p_298202_[i].asItem().builtInRegistryHolder())), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, List.of(), List.of(), Optional.empty(), Optional.empty());
         }

         return hasItems(aitempredicate);
      }

      public JsonObject serializeToJson() {
         JsonObject jsonobject = super.serializeToJson();
         if (!this.slotsOccupied.isAny() || !this.slotsFull.isAny() || !this.slotsEmpty.isAny()) {
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.add("occupied", this.slotsOccupied.serializeToJson());
            jsonobject1.add("full", this.slotsFull.serializeToJson());
            jsonobject1.add("empty", this.slotsEmpty.serializeToJson());
            jsonobject.add("slots", jsonobject1);
         }

         if (!this.predicates.isEmpty()) {
            jsonobject.add("items", ItemPredicate.serializeToJsonArray(this.predicates));
         }

         return jsonobject;
      }

      public boolean matches(Inventory p_43187_, ItemStack p_43188_, int p_43189_, int p_43190_, int p_43191_) {
         if (!this.slotsFull.matches(p_43189_)) {
            return false;
         } else if (!this.slotsEmpty.matches(p_43190_)) {
            return false;
         } else if (!this.slotsOccupied.matches(p_43191_)) {
            return false;
         } else if (this.predicates.isEmpty()) {
            return true;
         } else if (this.predicates.size() != 1) {
            List<ItemPredicate> list = new ObjectArrayList<>(this.predicates);
            int i = p_43187_.getContainerSize();

            for(int j = 0; j < i; ++j) {
               if (list.isEmpty()) {
                  return true;
               }

               ItemStack itemstack = p_43187_.getItem(j);
               if (!itemstack.isEmpty()) {
                  list.removeIf((p_43194_) -> {
                     return p_43194_.matches(itemstack);
                  });
               }
            }

            return list.isEmpty();
         } else {
            return !p_43188_.isEmpty() && this.predicates.get(0).matches(p_43188_);
         }
      }
   }
}
