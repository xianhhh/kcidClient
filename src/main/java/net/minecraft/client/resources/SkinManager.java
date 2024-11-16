package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.common.hash.Hashing;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecurePublicKeyException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.properties.Property;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.Optionull;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkinManager {
   private static final String PROPERTY_TEXTURES = "textures";
   private final LoadingCache<SkinManager.CacheKey, CompletableFuture<PlayerSkin>> skinCache;
   private final SkinManager.TextureCache skinTextures;
   private final SkinManager.TextureCache capeTextures;
   private final SkinManager.TextureCache elytraTextures;

   public SkinManager(TextureManager p_118812_, Path p_299617_, final MinecraftSessionService p_118814_, final Executor p_299732_) {
      this.skinTextures = new SkinManager.TextureCache(p_118812_, p_299617_, Type.SKIN);
      this.capeTextures = new SkinManager.TextureCache(p_118812_, p_299617_, Type.CAPE);
      this.elytraTextures = new SkinManager.TextureCache(p_118812_, p_299617_, Type.ELYTRA);
      this.skinCache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofSeconds(15L)).build(new CacheLoader<SkinManager.CacheKey, CompletableFuture<PlayerSkin>>() {
         public CompletableFuture<PlayerSkin> load(SkinManager.CacheKey p_298169_) {
            GameProfile gameprofile = p_298169_.profile();
            return CompletableFuture.supplyAsync(() -> {
               try {
                  try {
                     return SkinManager.TextureInfo.unpack(p_118814_.getTextures(gameprofile, true), true);
                  } catch (InsecurePublicKeyException insecurepublickeyexception) {
                     return SkinManager.TextureInfo.unpack(p_118814_.getTextures(gameprofile, false), false);
                  }
               } catch (Throwable throwable) {
                  return SkinManager.TextureInfo.EMPTY;
               }
            }, Util.backgroundExecutor()).thenComposeAsync((p_301039_) -> {
               return SkinManager.this.registerTextures(gameprofile, p_301039_);
            }, p_299732_);
         }
      });
   }

   public Supplier<PlayerSkin> lookupInsecure(GameProfile p_298295_) {
      CompletableFuture<PlayerSkin> completablefuture = this.getOrLoad(p_298295_);
      PlayerSkin playerskin = DefaultPlayerSkin.get(p_298295_);
      return () -> {
         return completablefuture.getNow(playerskin);
      };
   }

   public PlayerSkin getInsecureSkin(GameProfile p_298019_) {
      PlayerSkin playerskin = this.getOrLoad(p_298019_).getNow((PlayerSkin)null);
      return playerskin != null ? playerskin : DefaultPlayerSkin.get(p_298019_);
   }

   public CompletableFuture<PlayerSkin> getOrLoad(GameProfile p_298661_) {
      return this.skinCache.getUnchecked(new SkinManager.CacheKey(p_298661_));
   }

   CompletableFuture<PlayerSkin> registerTextures(GameProfile p_299268_, SkinManager.TextureInfo p_298597_) {
      MinecraftProfileTexture minecraftprofiletexture = p_298597_.skin();
      CompletableFuture<ResourceLocation> completablefuture;
      PlayerSkin.Model playerskin$model;
      if (minecraftprofiletexture != null) {
         completablefuture = this.skinTextures.getOrLoad(minecraftprofiletexture);
         playerskin$model = PlayerSkin.Model.byName(minecraftprofiletexture.getMetadata("model"));
      } else {
         PlayerSkin playerskin = DefaultPlayerSkin.get(p_299268_);
         completablefuture = CompletableFuture.completedFuture(playerskin.texture());
         playerskin$model = playerskin.model();
      }

      String s = Optionull.map(minecraftprofiletexture, MinecraftProfileTexture::getUrl);
      MinecraftProfileTexture minecraftprofiletexture1 = p_298597_.cape();
      CompletableFuture<ResourceLocation> completablefuture1 = minecraftprofiletexture1 != null ? this.capeTextures.getOrLoad(minecraftprofiletexture1) : CompletableFuture.completedFuture((ResourceLocation)null);
      MinecraftProfileTexture minecraftprofiletexture2 = p_298597_.elytra();
      CompletableFuture<ResourceLocation> completablefuture2 = minecraftprofiletexture2 != null ? this.elytraTextures.getOrLoad(minecraftprofiletexture2) : CompletableFuture.completedFuture((ResourceLocation)null);
      return CompletableFuture.allOf(completablefuture, completablefuture1, completablefuture2).thenApply((p_296316_) -> {
         return new PlayerSkin(completablefuture.join(), s, completablefuture1.join(), completablefuture2.join(), playerskin$model, p_298597_.secure());
      });
   }

   @Nullable
   static Property getTextureProperty(GameProfile p_300071_) {
      return Iterables.getFirst(p_300071_.getProperties().get("textures"), (Property)null);
   }

   @OnlyIn(Dist.CLIENT)
   static record CacheKey(GameProfile profile) {
      public boolean equals(Object p_299382_) {
         if (!(p_299382_ instanceof SkinManager.CacheKey skinmanager$cachekey)) {
            return false;
         } else {
            return this.profile.getId().equals(skinmanager$cachekey.profile.getId()) && Objects.equals(this.texturesData(), skinmanager$cachekey.texturesData());
         }
      }

      public int hashCode() {
         return this.profile.getId().hashCode() + Objects.hashCode(this.texturesData()) * 31;
      }

      @Nullable
      private String texturesData() {
         Property property = SkinManager.getTextureProperty(this.profile);
         return property != null ? property.value() : null;
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class TextureCache {
      private final TextureManager textureManager;
      private final Path root;
      private final MinecraftProfileTexture.Type type;
      private final Map<String, CompletableFuture<ResourceLocation>> textures = new Object2ObjectOpenHashMap<>();

      TextureCache(TextureManager p_298110_, Path p_297921_, MinecraftProfileTexture.Type p_298775_) {
         this.textureManager = p_298110_;
         this.root = p_297921_;
         this.type = p_298775_;
      }

      public CompletableFuture<ResourceLocation> getOrLoad(MinecraftProfileTexture p_300959_) {
         String s = p_300959_.getHash();
         CompletableFuture<ResourceLocation> completablefuture = this.textures.get(s);
         if (completablefuture == null) {
            completablefuture = this.registerTexture(p_300959_);
            this.textures.put(s, completablefuture);
         }

         return completablefuture;
      }

      private CompletableFuture<ResourceLocation> registerTexture(MinecraftProfileTexture p_300607_) {
         String s = Hashing.sha1().hashUnencodedChars(p_300607_.getHash()).toString();
         ResourceLocation resourcelocation = this.getTextureLocation(s);
         Path path = this.root.resolve(s.length() > 2 ? s.substring(0, 2) : "xx").resolve(s);
         CompletableFuture<ResourceLocation> completablefuture = new CompletableFuture<>();
         HttpTexture httptexture = new HttpTexture(path.toFile(), p_300607_.getUrl(), DefaultPlayerSkin.getDefaultTexture(), this.type == Type.SKIN, () -> {
            completablefuture.complete(resourcelocation);
         });
         this.textureManager.register(resourcelocation, httptexture);
         return completablefuture;
      }

      private ResourceLocation getTextureLocation(String p_297392_) {
         String s1;
         switch (this.type) {
            case SKIN:
               s1 = "skins";
               break;
            case CAPE:
               s1 = "capes";
               break;
            case ELYTRA:
               s1 = "elytra";
               break;
            default:
               throw new IncompatibleClassChangeError();
         }

         String s = s1;
         return new ResourceLocation(s + "/" + p_297392_);
      }
   }

   @OnlyIn(Dist.CLIENT)
   static record TextureInfo(@Nullable MinecraftProfileTexture skin, @Nullable MinecraftProfileTexture cape, @Nullable MinecraftProfileTexture elytra, boolean secure) {
      public static final SkinManager.TextureInfo EMPTY = new SkinManager.TextureInfo((MinecraftProfileTexture)null, (MinecraftProfileTexture)null, (MinecraftProfileTexture)null, true);

      public static SkinManager.TextureInfo unpack(Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> p_297479_, boolean p_297713_) {
         return p_297479_.isEmpty() ? EMPTY : new SkinManager.TextureInfo(p_297479_.get(Type.SKIN), p_297479_.get(Type.CAPE), p_297479_.get(Type.ELYTRA), p_297713_);
      }
   }
}