package net.minecraft.advancements;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record AdvancementHolder(ResourceLocation id, Advancement value) {
   public void write(FriendlyByteBuf p_299066_) {
      p_299066_.writeResourceLocation(this.id);
      this.value.write(p_299066_);
   }

   public static AdvancementHolder read(FriendlyByteBuf p_299642_) {
      return new AdvancementHolder(p_299642_.readResourceLocation(), Advancement.read(p_299642_));
   }

   public boolean equals(Object p_298719_) {
      if (this == p_298719_) {
         return true;
      } else {
         if (p_298719_ instanceof AdvancementHolder) {
            AdvancementHolder advancementholder = (AdvancementHolder)p_298719_;
            if (this.id.equals(advancementholder.id)) {
               return true;
            }
         }

         return false;
      }
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public String toString() {
      return this.id.toString();
   }
}