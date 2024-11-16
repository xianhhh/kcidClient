package net.minecraft.world.level.storage.loot.functions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public class SetNameFunction extends LootItemConditionalFunction {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Codec<SetNameFunction> CODEC = RecordCodecBuilder.create((p_297156_) -> {
      return commonFields(p_297156_).and(p_297156_.group(ExtraCodecs.strictOptionalField(ExtraCodecs.COMPONENT, "name").forGetter((p_297155_) -> {
         return p_297155_.name;
      }), ExtraCodecs.strictOptionalField(LootContext.EntityTarget.CODEC, "entity").forGetter((p_297165_) -> {
         return p_297165_.resolutionContext;
      }))).apply(p_297156_, SetNameFunction::new);
   });
   private final Optional<Component> name;
   private final Optional<LootContext.EntityTarget> resolutionContext;

   private SetNameFunction(List<LootItemCondition> p_298434_, Optional<Component> p_299902_, Optional<LootContext.EntityTarget> p_300668_) {
      super(p_298434_);
      this.name = p_299902_;
      this.resolutionContext = p_300668_;
   }

   public LootItemFunctionType getType() {
      return LootItemFunctions.SET_NAME;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return (Set<LootContextParam<?>>)(Set)this.resolutionContext.map((p_297154_) -> {
         return Set.of(p_297154_.getParam());
      }).orElse(Set.of());
   }

   public static UnaryOperator<Component> createResolver(LootContext p_81140_, @Nullable LootContext.EntityTarget p_81141_) {
      if (p_81141_ != null) {
         Entity entity = p_81140_.getParamOrNull(p_81141_.getParam());
         if (entity != null) {
            CommandSourceStack commandsourcestack = entity.createCommandSourceStack().withPermission(2);
            return (p_81147_) -> {
               try {
                  return ComponentUtils.updateForEntity(commandsourcestack, p_81147_, entity, 0);
               } catch (CommandSyntaxException commandsyntaxexception) {
                  LOGGER.warn("Failed to resolve text component", (Throwable)commandsyntaxexception);
                  return p_81147_;
               }
            };
         }
      }

      return (p_81152_) -> {
         return p_81152_;
      };
   }

   public ItemStack run(ItemStack p_81137_, LootContext p_81138_) {
      this.name.ifPresent((p_297161_) -> {
         p_81137_.setHoverName(createResolver(p_81138_, this.resolutionContext.orElse((LootContext.EntityTarget)null)).apply(p_297161_));
      });
      return p_81137_;
   }

   public static LootItemConditionalFunction.Builder<?> setName(Component p_165458_) {
      return simpleBuilder((p_297158_) -> {
         return new SetNameFunction(p_297158_, Optional.of(p_165458_), Optional.empty());
      });
   }

   public static LootItemConditionalFunction.Builder<?> setName(Component p_165460_, LootContext.EntityTarget p_165461_) {
      return simpleBuilder((p_297164_) -> {
         return new SetNameFunction(p_297164_, Optional.of(p_165460_), Optional.of(p_165461_));
      });
   }
}
