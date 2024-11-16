package net.minecraft.util;

import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;

public class ArrayListDeque<T> extends AbstractList<T> implements Serializable, Cloneable, Deque<T>, RandomAccess {
   private static final int MIN_GROWTH = 1;
   private Object[] contents;
   private int head;
   private int size;

   public ArrayListDeque() {
      this(1);
   }

   public ArrayListDeque(int p_299918_) {
      this.contents = new Object[p_299918_];
      this.head = 0;
      this.size = 0;
   }

   public int size() {
      return this.size;
   }

   @VisibleForTesting
   public int capacity() {
      return this.contents.length;
   }

   private int getIndex(int p_299728_) {
      return (p_299728_ + this.head) % this.contents.length;
   }

   public T get(int p_300499_) {
      this.verifyIndexInRange(p_300499_);
      return this.getInner(this.getIndex(p_300499_));
   }

   private static void verifyIndexInRange(int p_299791_, int p_299333_) {
      if (p_299791_ < 0 || p_299791_ >= p_299333_) {
         throw new IndexOutOfBoundsException(p_299791_);
      }
   }

   private void verifyIndexInRange(int p_298701_) {
      verifyIndexInRange(p_298701_, this.size);
   }

   private T getInner(int p_299306_) {
      return (T)this.contents[p_299306_];
   }

   public T set(int p_300259_, T p_298094_) {
      this.verifyIndexInRange(p_300259_);
      Objects.requireNonNull(p_298094_);
      int i = this.getIndex(p_300259_);
      T t = this.getInner(i);
      this.contents[i] = p_298094_;
      return t;
   }

   public void add(int p_301285_, T p_300734_) {
      verifyIndexInRange(p_301285_, this.size + 1);
      Objects.requireNonNull(p_300734_);
      if (this.size == this.contents.length) {
         this.grow();
      }

      int i = this.getIndex(p_301285_);
      if (p_301285_ == this.size) {
         this.contents[i] = p_300734_;
      } else if (p_301285_ == 0) {
         --this.head;
         if (this.head < 0) {
            this.head += this.contents.length;
         }

         this.contents[this.getIndex(0)] = p_300734_;
      } else {
         for(int j = this.size - 1; j >= p_301285_; --j) {
            this.contents[this.getIndex(j + 1)] = this.contents[this.getIndex(j)];
         }

         this.contents[i] = p_300734_;
      }

      ++this.modCount;
      ++this.size;
   }

   private void grow() {
      int i = this.contents.length + Math.max(this.contents.length >> 1, 1);
      Object[] aobject = new Object[i];
      this.copyCount(aobject, this.size);
      this.head = 0;
      this.contents = aobject;
   }

   public T remove(int p_297670_) {
      this.verifyIndexInRange(p_297670_);
      int i = this.getIndex(p_297670_);
      T t = this.getInner(i);
      if (p_297670_ == 0) {
         this.contents[i] = null;
         ++this.head;
      } else if (p_297670_ == this.size - 1) {
         this.contents[i] = null;
      } else {
         for(int j = p_297670_ + 1; j < this.size; ++j) {
            this.contents[this.getIndex(j - 1)] = this.get(j);
         }

         this.contents[this.getIndex(this.size - 1)] = null;
      }

      ++this.modCount;
      --this.size;
      return t;
   }

   public boolean removeIf(Predicate<? super T> p_300785_) {
      int i = 0;

      for(int j = 0; j < this.size; ++j) {
         T t = this.get(j);
         if (p_300785_.test(t)) {
            ++i;
         } else if (i != 0) {
            this.contents[this.getIndex(j - i)] = t;
            this.contents[this.getIndex(j)] = null;
         }
      }

      this.modCount += i;
      this.size -= i;
      return i != 0;
   }

   private void copyCount(Object[] p_300471_, int p_298513_) {
      for(int i = 0; i < p_298513_; ++i) {
         p_300471_[i] = this.get(i);
      }

   }

   public void replaceAll(UnaryOperator<T> p_299491_) {
      for(int i = 0; i < this.size; ++i) {
         int j = this.getIndex(i);
         this.contents[j] = Objects.requireNonNull(p_299491_.apply(this.getInner(i)));
      }

   }

   public void forEach(Consumer<? super T> p_297273_) {
      for(int i = 0; i < this.size; ++i) {
         p_297273_.accept(this.get(i));
      }

   }

   public void addFirst(T p_300853_) {
      this.add(0, p_300853_);
   }

   public void addLast(T p_301090_) {
      this.add(this.size, p_301090_);
   }

   public boolean offerFirst(T p_300075_) {
      this.addFirst(p_300075_);
      return true;
   }

   public boolean offerLast(T p_300597_) {
      this.addLast(p_300597_);
      return true;
   }

   public T removeFirst() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         return this.remove(0);
      }
   }

   public T removeLast() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         return this.remove(this.size - 1);
      }
   }

   @Nullable
   public T pollFirst() {
      return (T)(this.size == 0 ? null : this.removeFirst());
   }

   @Nullable
   public T pollLast() {
      return (T)(this.size == 0 ? null : this.removeLast());
   }

   public T getFirst() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         return this.get(0);
      }
   }

   public T getLast() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         return this.get(this.size - 1);
      }
   }

   @Nullable
   public T peekFirst() {
      return (T)(this.size == 0 ? null : this.getFirst());
   }

   @Nullable
   public T peekLast() {
      return (T)(this.size == 0 ? null : this.getLast());
   }

   public boolean removeFirstOccurrence(Object p_300960_) {
      for(int i = 0; i < this.size; ++i) {
         T t = this.get(i);
         if (Objects.equals(p_300960_, t)) {
            this.remove(i);
            return true;
         }
      }

      return false;
   }

   public boolean removeLastOccurrence(Object p_297293_) {
      for(int i = this.size - 1; i >= 0; --i) {
         T t = this.get(i);
         if (Objects.equals(p_297293_, t)) {
            this.remove(i);
            return true;
         }
      }

      return false;
   }

   public boolean offer(T p_299376_) {
      return this.offerLast(p_299376_);
   }

   public T remove() {
      return this.removeFirst();
   }

   @Nullable
   public T poll() {
      return this.pollFirst();
   }

   public T element() {
      return this.getFirst();
   }

   @Nullable
   public T peek() {
      return this.peekFirst();
   }

   public void push(T p_298076_) {
      this.addFirst(p_298076_);
   }

   public T pop() {
      return this.removeFirst();
   }

   public Iterator<T> descendingIterator() {
      return new ArrayListDeque.DescendingIterator();
   }

   class DescendingIterator implements Iterator<T> {
      private int index = ArrayListDeque.this.size() - 1;

      public DescendingIterator() {
      }

      public boolean hasNext() {
         return this.index >= 0;
      }

      public T next() {
         return ArrayListDeque.this.get(this.index--);
      }

      public void remove() {
         ArrayListDeque.this.remove(this.index + 1);
      }
   }
}