package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.slf4j.Logger;

public class LootDataType<T> {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final LootDataType<LootItemCondition> PREDICATE = new LootDataType<>(LootItemConditions.CODEC, "predicates", createSimpleValidator());
   public static final LootDataType<LootItemFunction> MODIFIER = new LootDataType<>(LootItemFunctions.CODEC, "item_modifiers", createSimpleValidator());
   public static final LootDataType<LootTable> TABLE = new LootDataType<>(LootTable.CODEC, "loot_tables", createLootTableValidator());
   private final Codec<T> codec;
   private final String directory;
   private final LootDataType.Validator<T> validator;

   private LootDataType(Codec<T> p_298670_, String p_279433_, LootDataType.Validator<T> p_279363_) {
      this.codec = p_298670_;
      this.directory = p_279433_;
      this.validator = p_279363_;
   }

   public String directory() {
      return this.directory;
   }

   public void runValidation(ValidationContext p_279366_, LootDataId<T> p_279106_, T p_279124_) {
      this.validator.run(p_279366_, p_279106_, p_279124_);
   }

   public Optional<T> deserialize(ResourceLocation p_279253_, JsonElement p_279330_) {
      DataResult<T> dataresult = this.codec.parse(JsonOps.INSTANCE, p_279330_);
      dataresult.error().ifPresent((p_297003_) -> {
         LOGGER.error("Couldn't parse element {}:{} - {}", this.directory, p_279253_, p_297003_.message());
      });
      return dataresult.result();
   }

   public static Stream<LootDataType<?>> values() {
      return Stream.of(PREDICATE, MODIFIER, TABLE);
   }

   private static <T extends LootContextUser> LootDataType.Validator<T> createSimpleValidator() {
      return (p_279353_, p_279374_, p_279097_) -> {
         p_279097_.validate(p_279353_.enterElement("{" + p_279374_.type().directory + ":" + p_279374_.location() + "}", p_279374_));
      };
   }

   private static LootDataType.Validator<LootTable> createLootTableValidator() {
      return (p_279333_, p_279227_, p_279406_) -> {
         p_279406_.validate(p_279333_.setParams(p_279406_.getParamSet()).enterElement("{" + p_279227_.type().directory + ":" + p_279227_.location() + "}", p_279227_));
      };
   }

   @FunctionalInterface
   public interface Validator<T> {
      void run(ValidationContext p_279419_, LootDataId<T> p_279145_, T p_279326_);
   }
}