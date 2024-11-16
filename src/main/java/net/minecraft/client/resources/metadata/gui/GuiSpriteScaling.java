package net.minecraft.client.resources.metadata.gui;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface GuiSpriteScaling {
   Codec<GuiSpriteScaling> CODEC = GuiSpriteScaling.Type.CODEC.dispatch(GuiSpriteScaling::type, GuiSpriteScaling.Type::codec);
   GuiSpriteScaling DEFAULT = new GuiSpriteScaling.Stretch();

   GuiSpriteScaling.Type type();

   @OnlyIn(Dist.CLIENT)
   public static record NineSlice(int width, int height, GuiSpriteScaling.NineSlice.Border border) implements GuiSpriteScaling {
      public static final Codec<GuiSpriteScaling.NineSlice> CODEC = ExtraCodecs.validate(RecordCodecBuilder.create((p_300419_) -> {
         return p_300419_.group(ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(GuiSpriteScaling.NineSlice::width), ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(GuiSpriteScaling.NineSlice::height), GuiSpriteScaling.NineSlice.Border.CODEC.fieldOf("border").forGetter(GuiSpriteScaling.NineSlice::border)).apply(p_300419_, GuiSpriteScaling.NineSlice::new);
      }), GuiSpriteScaling.NineSlice::validate);

      private static DataResult<GuiSpriteScaling.NineSlice> validate(GuiSpriteScaling.NineSlice p_298579_) {
         GuiSpriteScaling.NineSlice.Border guispritescaling$nineslice$border = p_298579_.border();
         if (guispritescaling$nineslice$border.left() + guispritescaling$nineslice$border.right() >= p_298579_.width()) {
            return DataResult.error(() -> {
               return "Nine-sliced texture has no horizontal center slice: " + guispritescaling$nineslice$border.left() + " + " + guispritescaling$nineslice$border.right() + " >= " + p_298579_.width();
            });
         } else {
            return guispritescaling$nineslice$border.top() + guispritescaling$nineslice$border.bottom() >= p_298579_.height() ? DataResult.error(() -> {
               return "Nine-sliced texture has no vertical center slice: " + guispritescaling$nineslice$border.top() + " + " + guispritescaling$nineslice$border.bottom() + " >= " + p_298579_.height();
            }) : DataResult.success(p_298579_);
         }
      }

      public GuiSpriteScaling.Type type() {
         return GuiSpriteScaling.Type.NINE_SLICE;
      }

      @OnlyIn(Dist.CLIENT)
      public static record Border(int left, int top, int right, int bottom) {
         private static final Codec<GuiSpriteScaling.NineSlice.Border> VALUE_CODEC = ExtraCodecs.POSITIVE_INT.flatComapMap((p_299885_) -> {
            return new GuiSpriteScaling.NineSlice.Border(p_299885_, p_299885_, p_299885_, p_299885_);
         }, (p_299528_) -> {
            OptionalInt optionalint = p_299528_.unpackValue();
            return optionalint.isPresent() ? DataResult.success(optionalint.getAsInt()) : DataResult.error(() -> {
               return "Border has different side sizes";
            });
         });
         private static final Codec<GuiSpriteScaling.NineSlice.Border> RECORD_CODEC = RecordCodecBuilder.create((p_297306_) -> {
            return p_297306_.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("left").forGetter(GuiSpriteScaling.NineSlice.Border::left), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("top").forGetter(GuiSpriteScaling.NineSlice.Border::top), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("right").forGetter(GuiSpriteScaling.NineSlice.Border::right), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bottom").forGetter(GuiSpriteScaling.NineSlice.Border::bottom)).apply(p_297306_, GuiSpriteScaling.NineSlice.Border::new);
         });
         static final Codec<GuiSpriteScaling.NineSlice.Border> CODEC = Codec.either(VALUE_CODEC, RECORD_CODEC).xmap((p_297549_) -> {
            return p_297549_.map(Function.identity(), Function.identity());
         }, (p_297509_) -> {
            return p_297509_.unpackValue().isPresent() ? Either.left(p_297509_) : Either.right(p_297509_);
         });

         private OptionalInt unpackValue() {
            return this.left() == this.top() && this.top() == this.right() && this.right() == this.bottom() ? OptionalInt.of(this.left()) : OptionalInt.empty();
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static record Stretch() implements GuiSpriteScaling {
      public static final Codec<GuiSpriteScaling.Stretch> CODEC = Codec.unit(GuiSpriteScaling.Stretch::new);

      public GuiSpriteScaling.Type type() {
         return GuiSpriteScaling.Type.STRETCH;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static record Tile(int width, int height) implements GuiSpriteScaling {
      public static final Codec<GuiSpriteScaling.Tile> CODEC = RecordCodecBuilder.create((p_297832_) -> {
         return p_297832_.group(ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(GuiSpriteScaling.Tile::width), ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(GuiSpriteScaling.Tile::height)).apply(p_297832_, GuiSpriteScaling.Tile::new);
      });

      public GuiSpriteScaling.Type type() {
         return GuiSpriteScaling.Type.TILE;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static enum Type implements StringRepresentable {
      STRETCH("stretch", GuiSpriteScaling.Stretch.CODEC),
      TILE("tile", GuiSpriteScaling.Tile.CODEC),
      NINE_SLICE("nine_slice", GuiSpriteScaling.NineSlice.CODEC);

      public static final Codec<GuiSpriteScaling.Type> CODEC = StringRepresentable.fromEnum(GuiSpriteScaling.Type::values);
      private final String key;
      private final Codec<? extends GuiSpriteScaling> codec;

      private Type(String p_299685_, Codec<? extends GuiSpriteScaling> p_300506_) {
         this.key = p_299685_;
         this.codec = p_300506_;
      }

      public String getSerializedName() {
         return this.key;
      }

      public Codec<? extends GuiSpriteScaling> codec() {
         return this.codec;
      }
   }
}