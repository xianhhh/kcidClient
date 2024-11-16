package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

public record ConstantValue(float value) implements NumberProvider {
   public static final Codec<ConstantValue> CODEC = RecordCodecBuilder.create((p_299462_) -> {
      return p_299462_.group(Codec.FLOAT.fieldOf("value").forGetter(ConstantValue::value)).apply(p_299462_, ConstantValue::new);
   });
   public static final Codec<ConstantValue> INLINE_CODEC = Codec.FLOAT.xmap(ConstantValue::new, ConstantValue::value);

   public LootNumberProviderType getType() {
      return NumberProviders.CONSTANT;
   }

   public float getFloat(LootContext p_165695_) {
      return this.value;
   }

   public static ConstantValue exactly(float p_165693_) {
      return new ConstantValue(p_165693_);
   }

   public boolean equals(Object p_165697_) {
      if (this == p_165697_) {
         return true;
      } else if (p_165697_ != null && this.getClass() == p_165697_.getClass()) {
         return Float.compare(((ConstantValue)p_165697_).value, this.value) == 0;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value != 0.0F ? Float.floatToIntBits(this.value) : 0;
   }
}