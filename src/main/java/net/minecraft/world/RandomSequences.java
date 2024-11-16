package net.minecraft.world;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;

public class RandomSequences extends SavedData {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final long worldSeed;
   private int salt;
   private boolean includeWorldSeed = true;
   private boolean includeSequenceId = true;
   private final Map<ResourceLocation, RandomSequence> sequences = new Object2ObjectOpenHashMap<>();

   public static SavedData.Factory<RandomSequences> factory(long p_297402_) {
      return new SavedData.Factory<>(() -> {
         return new RandomSequences(p_297402_);
      }, (p_296656_) -> {
         return load(p_297402_, p_296656_);
      }, DataFixTypes.SAVED_DATA_RANDOM_SEQUENCES);
   }

   public RandomSequences(long p_287622_) {
      this.worldSeed = p_287622_;
   }

   public RandomSource get(ResourceLocation p_287751_) {
      RandomSource randomsource = this.sequences.computeIfAbsent(p_287751_, this::createSequence).random();
      return new RandomSequences.DirtyMarkingRandomSource(randomsource);
   }

   private RandomSequence createSequence(ResourceLocation p_299723_) {
      return this.createSequence(p_299723_, this.salt, this.includeWorldSeed, this.includeSequenceId);
   }

   private RandomSequence createSequence(ResourceLocation p_299881_, int p_299267_, boolean p_300525_, boolean p_297272_) {
      long i = (p_300525_ ? this.worldSeed : 0L) ^ (long)p_299267_;
      return new RandomSequence(i, p_297272_ ? Optional.of(p_299881_) : Optional.empty());
   }

   public void forAllSequences(BiConsumer<ResourceLocation, RandomSequence> p_299883_) {
      this.sequences.forEach(p_299883_);
   }

   public void setSeedDefaults(int p_299968_, boolean p_298395_, boolean p_298518_) {
      this.salt = p_299968_;
      this.includeWorldSeed = p_298395_;
      this.includeSequenceId = p_298518_;
   }

   public CompoundTag save(CompoundTag p_287658_) {
      p_287658_.putInt("salt", this.salt);
      p_287658_.putBoolean("include_world_seed", this.includeWorldSeed);
      p_287658_.putBoolean("include_sequence_id", this.includeSequenceId);
      CompoundTag compoundtag = new CompoundTag();
      this.sequences.forEach((p_287627_, p_287578_) -> {
         compoundtag.put(p_287627_.toString(), RandomSequence.CODEC.encodeStart(NbtOps.INSTANCE, p_287578_).result().orElseThrow());
      });
      p_287658_.put("sequences", compoundtag);
      return p_287658_;
   }

   private static boolean getBooleanWithDefault(CompoundTag p_297418_, String p_298953_, boolean p_297237_) {
      return p_297418_.contains(p_298953_, 1) ? p_297418_.getBoolean(p_298953_) : p_297237_;
   }

   public static RandomSequences load(long p_287756_, CompoundTag p_287587_) {
      RandomSequences randomsequences = new RandomSequences(p_287756_);
      randomsequences.setSeedDefaults(p_287587_.getInt("salt"), getBooleanWithDefault(p_287587_, "include_world_seed", true), getBooleanWithDefault(p_287587_, "include_sequence_id", true));
      CompoundTag compoundtag = p_287587_.getCompound("sequences");

      for(String s : compoundtag.getAllKeys()) {
         try {
            RandomSequence randomsequence = RandomSequence.CODEC.decode(NbtOps.INSTANCE, compoundtag.get(s)).result().get().getFirst();
            randomsequences.sequences.put(new ResourceLocation(s), randomsequence);
         } catch (Exception exception) {
            LOGGER.error("Failed to load random sequence {}", s, exception);
         }
      }

      return randomsequences;
   }

   public int clear() {
      int i = this.sequences.size();
      this.sequences.clear();
      return i;
   }

   public void reset(ResourceLocation p_298741_) {
      this.sequences.put(p_298741_, this.createSequence(p_298741_));
   }

   public void reset(ResourceLocation p_301350_, int p_298554_, boolean p_298049_, boolean p_301283_) {
      this.sequences.put(p_301350_, this.createSequence(p_301350_, p_298554_, p_298049_, p_301283_));
   }

   class DirtyMarkingRandomSource implements RandomSource {
      private final RandomSource random;

      DirtyMarkingRandomSource(RandomSource p_299209_) {
         this.random = p_299209_;
      }

      public RandomSource fork() {
         RandomSequences.this.setDirty();
         return this.random.fork();
      }

      public PositionalRandomFactory forkPositional() {
         RandomSequences.this.setDirty();
         return this.random.forkPositional();
      }

      public void setSeed(long p_300098_) {
         RandomSequences.this.setDirty();
         this.random.setSeed(p_300098_);
      }

      public int nextInt() {
         RandomSequences.this.setDirty();
         return this.random.nextInt();
      }

      public int nextInt(int p_301106_) {
         RandomSequences.this.setDirty();
         return this.random.nextInt(p_301106_);
      }

      public long nextLong() {
         RandomSequences.this.setDirty();
         return this.random.nextLong();
      }

      public boolean nextBoolean() {
         RandomSequences.this.setDirty();
         return this.random.nextBoolean();
      }

      public float nextFloat() {
         RandomSequences.this.setDirty();
         return this.random.nextFloat();
      }

      public double nextDouble() {
         RandomSequences.this.setDirty();
         return this.random.nextDouble();
      }

      public double nextGaussian() {
         RandomSequences.this.setDirty();
         return this.random.nextGaussian();
      }

      public boolean equals(Object p_299603_) {
         if (this == p_299603_) {
            return true;
         } else if (p_299603_ instanceof RandomSequences.DirtyMarkingRandomSource) {
            RandomSequences.DirtyMarkingRandomSource randomsequences$dirtymarkingrandomsource = (RandomSequences.DirtyMarkingRandomSource)p_299603_;
            return this.random.equals(randomsequences$dirtymarkingrandomsource.random);
         } else {
            return false;
         }
      }
   }
}