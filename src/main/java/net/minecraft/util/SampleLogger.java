package net.minecraft.util;

public class SampleLogger {
   public static final int CAPACITY = 240;
   private final long[] samples = new long[240];
   private int start;
   private int size;

   public void logSample(long p_300449_) {
      int i = this.wrapIndex(this.start + this.size);
      this.samples[i] = p_300449_;
      if (this.size < 240) {
         ++this.size;
      } else {
         this.start = this.wrapIndex(this.start + 1);
      }

   }

   public int capacity() {
      return this.samples.length;
   }

   public int size() {
      return this.size;
   }

   public long get(int p_299109_) {
      if (p_299109_ >= 0 && p_299109_ < this.size) {
         return this.samples[this.wrapIndex(this.start + p_299109_)];
      } else {
         throw new IndexOutOfBoundsException(p_299109_ + " out of bounds for length " + this.size);
      }
   }

   private int wrapIndex(int p_300608_) {
      return p_300608_ % 240;
   }

   public void reset() {
      this.start = 0;
      this.size = 0;
   }
}