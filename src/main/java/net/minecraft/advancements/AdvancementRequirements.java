package net.minecraft.advancements;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record AdvancementRequirements(String[][] requirements) {
   public static final AdvancementRequirements EMPTY = new AdvancementRequirements(new String[0][]);

   public AdvancementRequirements(FriendlyByteBuf p_299417_) {
      this(read(p_299417_));
   }

   private static String[][] read(FriendlyByteBuf p_298812_) {
      String[][] astring = new String[p_298812_.readVarInt()][];

      for(int i = 0; i < astring.length; ++i) {
         astring[i] = new String[p_298812_.readVarInt()];

         for(int j = 0; j < astring[i].length; ++j) {
            astring[i][j] = p_298812_.readUtf();
         }
      }

      return astring;
   }

   public void write(FriendlyByteBuf p_299546_) {
      p_299546_.writeVarInt(this.requirements.length);

      for(String[] astring : this.requirements) {
         p_299546_.writeVarInt(astring.length);

         for(String s : astring) {
            p_299546_.writeUtf(s);
         }
      }

   }

   public static AdvancementRequirements allOf(Collection<String> p_300431_) {
      return new AdvancementRequirements(p_300431_.stream().map((p_298440_) -> {
         return new String[]{p_298440_};
      }).toArray((p_301148_) -> {
         return new String[p_301148_][];
      }));
   }

   public static AdvancementRequirements anyOf(Collection<String> p_297776_) {
      return new AdvancementRequirements(new String[][]{p_297776_.toArray((p_299867_) -> {
         return new String[p_299867_];
      })});
   }

   public int size() {
      return this.requirements.length;
   }

   public boolean test(Predicate<String> p_297982_) {
      if (this.requirements.length == 0) {
         return false;
      } else {
         for(String[] astring : this.requirements) {
            if (!anyMatch(astring, p_297982_)) {
               return false;
            }
         }

         return true;
      }
   }

   public int count(Predicate<String> p_300443_) {
      int i = 0;

      for(String[] astring : this.requirements) {
         if (anyMatch(astring, p_300443_)) {
            ++i;
         }
      }

      return i;
   }

   private static boolean anyMatch(String[] p_299779_, Predicate<String> p_299134_) {
      for(String s : p_299779_) {
         if (p_299134_.test(s)) {
            return true;
         }
      }

      return false;
   }

   public static AdvancementRequirements fromJson(JsonArray p_297416_, Set<String> p_298134_) {
      String[][] astring = new String[p_297416_.size()][];
      Set<String> set = new ObjectOpenHashSet<>();

      for(int i = 0; i < p_297416_.size(); ++i) {
         JsonArray jsonarray = GsonHelper.convertToJsonArray(p_297416_.get(i), "requirements[" + i + "]");
         if (jsonarray.isEmpty() && p_298134_.isEmpty()) {
            throw new JsonSyntaxException("Requirement entry cannot be empty");
         }

         astring[i] = new String[jsonarray.size()];

         for(int j = 0; j < jsonarray.size(); ++j) {
            String s = GsonHelper.convertToString(jsonarray.get(j), "requirements[" + i + "][" + j + "]");
            astring[i][j] = s;
            set.add(s);
         }
      }

      if (!p_298134_.equals(set)) {
         Set<String> set1 = Sets.difference(p_298134_, set);
         Set<String> set2 = Sets.difference(set, p_298134_);
         throw new JsonSyntaxException("Advancement completion requirements did not exactly match specified criteria. Missing: " + set1 + ". Unknown: " + set2);
      } else {
         return new AdvancementRequirements(astring);
      }
   }

   public JsonArray toJson() {
      JsonArray jsonarray = new JsonArray();

      for(String[] astring : this.requirements) {
         JsonArray jsonarray1 = new JsonArray();
         Arrays.stream(astring).forEach(jsonarray1::add);
         jsonarray.add(jsonarray1);
      }

      return jsonarray;
   }

   public boolean isEmpty() {
      return this.requirements.length == 0;
   }

   public String toString() {
      return Arrays.deepToString(this.requirements);
   }

   public Set<String> names() {
      Set<String> set = new ObjectOpenHashSet<>();

      for(String[] astring : this.requirements) {
         Collections.addAll(set, astring);
      }

      return set;
   }

   public interface Strategy {
      AdvancementRequirements.Strategy AND = AdvancementRequirements::allOf;
      AdvancementRequirements.Strategy OR = AdvancementRequirements::anyOf;

      AdvancementRequirements create(Collection<String> p_297497_);
   }
}