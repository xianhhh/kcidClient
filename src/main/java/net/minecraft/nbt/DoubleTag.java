package net.minecraft.nbt;

import net.minecraft.util.Mth;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleTag extends NumericTag {
   private static final int SELF_SIZE_IN_BYTES = 16;
   public static final DoubleTag ZERO = new DoubleTag(0.0D);
   public static final TagType<DoubleTag> TYPE = new TagType.StaticSize<DoubleTag>() {
      public DoubleTag load(DataInput p_128529_, NbtAccounter p_128531_) throws IOException {
         return DoubleTag.valueOf(readAccounted(p_128529_, p_128531_));
      }

      public StreamTagVisitor.ValueResult parse(DataInput p_197454_, StreamTagVisitor p_197455_, NbtAccounter p_301767_) throws IOException {
         return p_197455_.visit(readAccounted(p_197454_, p_301767_));
      }

      private static double readAccounted(DataInput p_301722_, NbtAccounter p_301770_) throws IOException {
         p_301770_.accountBytes(16L);
         return p_301722_.readDouble();
      }

      public int size() {
         return 8;
      }

      public String getName() {
         return "DOUBLE";
      }

      public String getPrettyName() {
         return "TAG_Double";
      }

      public boolean isValue() {
         return true;
      }
   };
   private final double data;

   private DoubleTag(double p_128498_) {
      this.data = p_128498_;
   }

   public static DoubleTag valueOf(double p_128501_) {
      return p_128501_ == 0.0D ? ZERO : new DoubleTag(p_128501_);
   }

   public void write(DataOutput p_128503_) throws IOException {
      p_128503_.writeDouble(this.data);
   }

   public int sizeInBytes() {
      return 16;
   }

   public byte getId() {
      return 6;
   }

   public TagType<DoubleTag> getType() {
      return TYPE;
   }

   public DoubleTag copy() {
      return this;
   }

   public boolean equals(Object p_128512_) {
      if (this == p_128512_) {
         return true;
      } else {
         return p_128512_ instanceof DoubleTag && this.data == ((DoubleTag)p_128512_).data;
      }
   }

   public int hashCode() {
      long i = Double.doubleToLongBits(this.data);
      return (int)(i ^ i >>> 32);
   }

   public void accept(TagVisitor p_177860_) {
      p_177860_.visitDouble(this);
   }

   public long getAsLong() {
      return (long)Math.floor(this.data);
   }

   public int getAsInt() {
      return Mth.floor(this.data);
   }

   public short getAsShort() {
      return (short)(Mth.floor(this.data) & '\uffff');
   }

   public byte getAsByte() {
      return (byte)(Mth.floor(this.data) & 255);
   }

   public double getAsDouble() {
      return this.data;
   }

   public float getAsFloat() {
      return (float)this.data;
   }

   public Number getAsNumber() {
      return this.data;
   }

   public StreamTagVisitor.ValueResult accept(StreamTagVisitor p_197452_) {
      return p_197452_.visit(this.data);
   }
}