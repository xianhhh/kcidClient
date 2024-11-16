package net.minecraft.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityVariantPredicate<V> {
   private final Function<Entity, Optional<V>> getter;
   private final EntitySubPredicate.Type type;

   public static <V> EntityVariantPredicate<V> create(Registry<V> p_219094_, Function<Entity, Optional<V>> p_219095_) {
      return new EntityVariantPredicate<>(p_219094_.byNameCodec(), p_219095_);
   }

   public static <V> EntityVariantPredicate<V> create(Codec<V> p_262671_, Function<Entity, Optional<V>> p_262652_) {
      return new EntityVariantPredicate<>(p_262671_, p_262652_);
   }

   private EntityVariantPredicate(Codec<V> p_262574_, Function<Entity, Optional<V>> p_262610_) {
      this.getter = p_262610_;
      MapCodec<EntityVariantPredicate.SubPredicate<V>> mapcodec = RecordCodecBuilder.mapCodec((p_296131_) -> {
         return p_296131_.group(p_262574_.fieldOf("variant").forGetter(EntityVariantPredicate.SubPredicate::variant)).apply(p_296131_, this::createPredicate);
      });
      this.type = new EntitySubPredicate.Type(mapcodec);
   }

   public EntitySubPredicate.Type type() {
      return this.type;
   }

   public EntityVariantPredicate.SubPredicate<V> createPredicate(V p_219097_) {
      return new EntityVariantPredicate.SubPredicate<>(this.type, this.getter, p_219097_);
   }

   public static record SubPredicate<V>(EntitySubPredicate.Type type, Function<Entity, Optional<V>> getter, V variant) implements EntitySubPredicate {
      public boolean matches(Entity p_298600_, ServerLevel p_297848_, @Nullable Vec3 p_298842_) {
         return this.getter.apply(p_298600_).filter((p_298468_) -> {
            return p_298468_.equals(this.variant);
         }).isPresent();
      }

      public EntitySubPredicate.Type type() {
         return this.type;
      }
   }
}