package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;

public record StatePropertiesPredicate(List<StatePropertiesPredicate.PropertyMatcher> properties) {
   private static final Codec<List<StatePropertiesPredicate.PropertyMatcher>> PROPERTIES_CODEC = Codec.unboundedMap(Codec.STRING, StatePropertiesPredicate.ValueMatcher.CODEC).xmap((p_296151_) -> {
      return p_296151_.entrySet().stream().map((p_296150_) -> {
         return new StatePropertiesPredicate.PropertyMatcher(p_296150_.getKey(), p_296150_.getValue());
      }).toList();
   }, (p_296146_) -> {
      return p_296146_.stream().collect(Collectors.toMap(StatePropertiesPredicate.PropertyMatcher::name, StatePropertiesPredicate.PropertyMatcher::valueMatcher));
   });
   public static final Codec<StatePropertiesPredicate> CODEC = PROPERTIES_CODEC.xmap(StatePropertiesPredicate::new, StatePropertiesPredicate::properties);

   public <S extends StateHolder<?, S>> boolean matches(StateDefinition<?, S> p_67670_, S p_67671_) {
      for(StatePropertiesPredicate.PropertyMatcher statepropertiespredicate$propertymatcher : this.properties) {
         if (!statepropertiespredicate$propertymatcher.match(p_67670_, p_67671_)) {
            return false;
         }
      }

      return true;
   }

   public boolean matches(BlockState p_67668_) {
      return this.matches(p_67668_.getBlock().getStateDefinition(), p_67668_);
   }

   public boolean matches(FluidState p_67685_) {
      return this.matches(p_67685_.getType().getStateDefinition(), p_67685_);
   }

   public Optional<String> checkState(StateDefinition<?, ?> p_299112_) {
      for(StatePropertiesPredicate.PropertyMatcher statepropertiespredicate$propertymatcher : this.properties) {
         Optional<String> optional = statepropertiespredicate$propertymatcher.checkState(p_299112_);
         if (optional.isPresent()) {
            return optional;
         }
      }

      return Optional.empty();
   }

   public void checkState(StateDefinition<?, ?> p_67673_, Consumer<String> p_67674_) {
      this.properties.forEach((p_296149_) -> {
         p_296149_.checkState(p_67673_).ifPresent(p_67674_);
      });
   }

   public static Optional<StatePropertiesPredicate> fromJson(@Nullable JsonElement p_67680_) {
      return p_67680_ != null && !p_67680_.isJsonNull() ? Optional.of(Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, p_67680_), JsonParseException::new)) : Optional.empty();
   }

   public JsonElement serializeToJson() {
      return Util.getOrThrow(CODEC.encodeStart(JsonOps.INSTANCE, this), IllegalStateException::new);
   }

   public static class Builder {
      private final ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers = ImmutableList.builder();

      private Builder() {
      }

      public static StatePropertiesPredicate.Builder properties() {
         return new StatePropertiesPredicate.Builder();
      }

      public StatePropertiesPredicate.Builder hasProperty(Property<?> p_67701_, String p_67702_) {
         this.matchers.add(new StatePropertiesPredicate.PropertyMatcher(p_67701_.getName(), new StatePropertiesPredicate.ExactMatcher(p_67702_)));
         return this;
      }

      public StatePropertiesPredicate.Builder hasProperty(Property<Integer> p_67695_, int p_67696_) {
         return this.hasProperty(p_67695_, Integer.toString(p_67696_));
      }

      public StatePropertiesPredicate.Builder hasProperty(Property<Boolean> p_67704_, boolean p_67705_) {
         return this.hasProperty(p_67704_, Boolean.toString(p_67705_));
      }

      public <T extends Comparable<T> & StringRepresentable> StatePropertiesPredicate.Builder hasProperty(Property<T> p_67698_, T p_67699_) {
         return this.hasProperty(p_67698_, p_67699_.getSerializedName());
      }

      public Optional<StatePropertiesPredicate> build() {
         return Optional.of(new StatePropertiesPredicate(this.matchers.build()));
      }
   }

   static record ExactMatcher(String value) implements StatePropertiesPredicate.ValueMatcher {
      public static final Codec<StatePropertiesPredicate.ExactMatcher> CODEC = Codec.STRING.xmap(StatePropertiesPredicate.ExactMatcher::new, StatePropertiesPredicate.ExactMatcher::value);

      public <T extends Comparable<T>> boolean match(StateHolder<?, ?> p_301115_, Property<T> p_299705_) {
         T t = p_301115_.getValue(p_299705_);
         Optional<T> optional = p_299705_.getValue(this.value);
         return optional.isPresent() && t.compareTo(optional.get()) == 0;
      }
   }

   static record PropertyMatcher(String name, StatePropertiesPredicate.ValueMatcher valueMatcher) {
      public <S extends StateHolder<?, S>> boolean match(StateDefinition<?, S> p_67719_, S p_67720_) {
         Property<?> property = p_67719_.getProperty(this.name);
         return property != null && this.valueMatcher.match(p_67720_, property);
      }

      public Optional<String> checkState(StateDefinition<?, ?> p_67722_) {
         Property<?> property = p_67722_.getProperty(this.name);
         return property != null ? Optional.empty() : Optional.of(this.name);
      }
   }

   static record RangedMatcher(Optional<String> minValue, Optional<String> maxValue) implements StatePropertiesPredicate.ValueMatcher {
      public static final Codec<StatePropertiesPredicate.RangedMatcher> CODEC = RecordCodecBuilder.create((p_299246_) -> {
         return p_299246_.group(ExtraCodecs.strictOptionalField(Codec.STRING, "min").forGetter(StatePropertiesPredicate.RangedMatcher::minValue), ExtraCodecs.strictOptionalField(Codec.STRING, "max").forGetter(StatePropertiesPredicate.RangedMatcher::maxValue)).apply(p_299246_, StatePropertiesPredicate.RangedMatcher::new);
      });

      public <T extends Comparable<T>> boolean match(StateHolder<?, ?> p_299120_, Property<T> p_300783_) {
         T t = p_299120_.getValue(p_300783_);
         if (this.minValue.isPresent()) {
            Optional<T> optional = p_300783_.getValue(this.minValue.get());
            if (optional.isEmpty() || t.compareTo(optional.get()) < 0) {
               return false;
            }
         }

         if (this.maxValue.isPresent()) {
            Optional<T> optional1 = p_300783_.getValue(this.maxValue.get());
            if (optional1.isEmpty() || t.compareTo(optional1.get()) > 0) {
               return false;
            }
         }

         return true;
      }
   }

   interface ValueMatcher {
      Codec<StatePropertiesPredicate.ValueMatcher> CODEC = Codec.either(StatePropertiesPredicate.ExactMatcher.CODEC, StatePropertiesPredicate.RangedMatcher.CODEC).xmap((p_300028_) -> {
         return p_300028_.map((p_300209_) -> {
            return p_300209_;
         }, (p_299353_) -> {
            return p_299353_;
         });
      }, (p_297370_) -> {
         if (p_297370_ instanceof StatePropertiesPredicate.ExactMatcher statepropertiespredicate$exactmatcher) {
            return Either.left(statepropertiespredicate$exactmatcher);
         } else if (p_297370_ instanceof StatePropertiesPredicate.RangedMatcher statepropertiespredicate$rangedmatcher) {
            return Either.right(statepropertiespredicate$rangedmatcher);
         } else {
            throw new UnsupportedOperationException();
         }
      });

      <T extends Comparable<T>> boolean match(StateHolder<?, ?> p_301268_, Property<T> p_300938_);
   }
}