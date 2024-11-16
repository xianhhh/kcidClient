package net.minecraft.client.resources;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record PlayerSkin(ResourceLocation texture, @Nullable String textureUrl, @Nullable ResourceLocation capeTexture, @Nullable ResourceLocation elytraTexture, PlayerSkin.Model model, boolean secure) {
   @OnlyIn(Dist.CLIENT)
   public static enum Model {
      SLIM("slim"),
      WIDE("default");

      private final String id;

      private Model(String p_300061_) {
         this.id = p_300061_;
      }

      public static PlayerSkin.Model byName(@Nullable String p_299354_) {
         if (p_299354_ == null) {
            return WIDE;
         } else {
            PlayerSkin.Model playerskin$model;
            switch (p_299354_) {
               case "slim":
                  playerskin$model = SLIM;
                  break;
               default:
                  playerskin$model = WIDE;
            }

            return playerskin$model;
         }
      }

      public String id() {
         return this.id;
      }
   }
}