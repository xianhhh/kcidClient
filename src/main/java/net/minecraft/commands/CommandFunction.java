package net.minecraft.commands;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerFunctionManager;

public class CommandFunction {
   private final CommandFunction.Entry[] entries;
   final ResourceLocation id;

   public CommandFunction(ResourceLocation p_77979_, CommandFunction.Entry[] p_77980_) {
      this.id = p_77979_;
      this.entries = p_77980_;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public CommandFunction.Entry[] getEntries() {
      return this.entries;
   }

   public CommandFunction instantiate(@Nullable CompoundTag p_300636_, CommandDispatcher<CommandSourceStack> p_300698_, CommandSourceStack p_298553_) throws FunctionInstantiationException {
      return this;
   }

   private static boolean shouldConcatenateNextLine(CharSequence p_298962_) {
      int i = p_298962_.length();
      return i > 0 && p_298962_.charAt(i - 1) == '\\';
   }

   public static CommandFunction fromLines(ResourceLocation p_77985_, CommandDispatcher<CommandSourceStack> p_77986_, CommandSourceStack p_77987_, List<String> p_77988_) {
      List<CommandFunction.Entry> list = new ArrayList<>(p_77988_.size());
      Set<String> set = new ObjectArraySet<>();

      for(int i = 0; i < p_77988_.size(); ++i) {
         int j = i + 1;
         String s = p_77988_.get(i).trim();
         String s1;
         if (shouldConcatenateNextLine(s)) {
            StringBuilder stringbuilder = new StringBuilder(s);

            do {
               ++i;
               if (i == p_77988_.size()) {
                  throw new IllegalArgumentException("Line continuation at end of file");
               }

               stringbuilder.deleteCharAt(stringbuilder.length() - 1);
               String s2 = p_77988_.get(i).trim();
               stringbuilder.append(s2);
            } while(shouldConcatenateNextLine(stringbuilder));

            s1 = stringbuilder.toString();
         } else {
            s1 = s;
         }

         StringReader stringreader = new StringReader(s1);
         if (stringreader.canRead() && stringreader.peek() != '#') {
            if (stringreader.peek() == '/') {
               stringreader.skip();
               if (stringreader.peek() == '/') {
                  throw new IllegalArgumentException("Unknown or invalid command '" + s1 + "' on line " + j + " (if you intended to make a comment, use '#' not '//')");
               }

               String s3 = stringreader.readUnquotedString();
               throw new IllegalArgumentException("Unknown or invalid command '" + s1 + "' on line " + j + " (did you mean '" + s3 + "'? Do not use a preceding forwards slash.)");
            }

            if (stringreader.peek() == '$') {
               CommandFunction.MacroEntry commandfunction$macroentry = decomposeMacro(s1.substring(1), j);
               list.add(commandfunction$macroentry);
               set.addAll(commandfunction$macroentry.parameters());
            } else {
               try {
                  ParseResults<CommandSourceStack> parseresults = p_77986_.parse(stringreader, p_77987_);
                  if (parseresults.getReader().canRead()) {
                     throw Commands.getParseException(parseresults);
                  }

                  list.add(new CommandFunction.CommandEntry(parseresults));
               } catch (CommandSyntaxException commandsyntaxexception) {
                  throw new IllegalArgumentException("Whilst parsing command on line " + j + ": " + commandsyntaxexception.getMessage());
               }
            }
         }
      }

      return (CommandFunction)(set.isEmpty() ? new CommandFunction(p_77985_, list.toArray((p_299604_) -> {
         return new CommandFunction.Entry[p_299604_];
      })) : new CommandFunction.CommandMacro(p_77985_, list.toArray((p_299648_) -> {
         return new CommandFunction.Entry[p_299648_];
      }), List.copyOf(set)));
   }

   @VisibleForTesting
   public static CommandFunction.MacroEntry decomposeMacro(String p_301200_, int p_300045_) {
      ImmutableList.Builder<String> builder = ImmutableList.builder();
      ImmutableList.Builder<String> builder1 = ImmutableList.builder();
      int i = p_301200_.length();
      int j = 0;
      int k = p_301200_.indexOf(36);

      while(k != -1) {
         if (k != i - 1 && p_301200_.charAt(k + 1) == '(') {
            builder.add(p_301200_.substring(j, k));
            int l = p_301200_.indexOf(41, k + 1);
            if (l == -1) {
               throw new IllegalArgumentException("Unterminated macro variable in macro '" + p_301200_ + "' on line " + p_300045_);
            }

            String s = p_301200_.substring(k + 2, l);
            if (!isValidVariableName(s)) {
               throw new IllegalArgumentException("Invalid macro variable name '" + s + "' on line " + p_300045_);
            }

            builder1.add(s);
            j = l + 1;
            k = p_301200_.indexOf(36, j);
         } else {
            k = p_301200_.indexOf(36, k + 1);
         }
      }

      if (j == 0) {
         throw new IllegalArgumentException("Macro without variables on line " + p_300045_);
      } else {
         if (j != i) {
            builder.add(p_301200_.substring(j));
         }

         return new CommandFunction.MacroEntry(builder.build(), builder1.build());
      }
   }

   private static boolean isValidVariableName(String p_299170_) {
      for(int i = 0; i < p_299170_.length(); ++i) {
         char c0 = p_299170_.charAt(i);
         if (!Character.isLetterOrDigit(c0) && c0 != '_') {
            return false;
         }
      }

      return true;
   }

   public static class CacheableFunction {
      public static final CommandFunction.CacheableFunction NONE = new CommandFunction.CacheableFunction((ResourceLocation)null);
      @Nullable
      private final ResourceLocation id;
      private boolean resolved;
      private Optional<CommandFunction> function = Optional.empty();

      public CacheableFunction(@Nullable ResourceLocation p_77998_) {
         this.id = p_77998_;
      }

      public CacheableFunction(CommandFunction p_77996_) {
         this.resolved = true;
         this.id = null;
         this.function = Optional.of(p_77996_);
      }

      public Optional<CommandFunction> get(ServerFunctionManager p_78003_) {
         if (!this.resolved) {
            if (this.id != null) {
               this.function = p_78003_.get(this.id);
            }

            this.resolved = true;
         }

         return this.function;
      }

      @Nullable
      public ResourceLocation getId() {
         return this.function.map((p_78001_) -> {
            return p_78001_.id;
         }).orElse(this.id);
      }
   }

   public static class CommandEntry implements CommandFunction.Entry {
      private final ParseResults<CommandSourceStack> parse;

      public CommandEntry(ParseResults<CommandSourceStack> p_78006_) {
         this.parse = p_78006_;
      }

      public void execute(ServerFunctionManager p_164879_, CommandSourceStack p_164880_, Deque<ServerFunctionManager.QueuedCommand> p_164881_, int p_164882_, int p_164883_, @Nullable ServerFunctionManager.TraceCallbacks p_164884_) throws CommandSyntaxException {
         if (p_164884_ != null) {
            String s = this.parse.getReader().getString();
            p_164884_.onCommand(p_164883_, s);
            int i = this.execute(p_164879_, p_164880_);
            p_164884_.onReturn(p_164883_, s, i);
         } else {
            this.execute(p_164879_, p_164880_);
         }

      }

      private int execute(ServerFunctionManager p_164876_, CommandSourceStack p_164877_) throws CommandSyntaxException {
         return p_164876_.getDispatcher().execute(Commands.mapSource(this.parse, (p_242934_) -> {
            return p_164877_;
         }));
      }

      public String toString() {
         return this.parse.getReader().getString();
      }
   }

   static class CommandMacro extends CommandFunction {
      private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#");
      private final List<String> parameters;
      private static final int MAX_CACHE_ENTRIES = 8;
      private final Object2ObjectLinkedOpenHashMap<List<String>, CommandFunction> cache = new Object2ObjectLinkedOpenHashMap<>(8, 0.25F);

      public CommandMacro(ResourceLocation p_297380_, CommandFunction.Entry[] p_300955_, List<String> p_300316_) {
         super(p_297380_, p_300955_);
         this.parameters = p_300316_;
      }

      public CommandFunction instantiate(@Nullable CompoundTag p_300836_, CommandDispatcher<CommandSourceStack> p_297591_, CommandSourceStack p_298828_) throws FunctionInstantiationException {
         if (p_300836_ == null) {
            throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_arguments", this.getId()));
         } else {
            List<String> list = new ArrayList<>(this.parameters.size());

            for(String s : this.parameters) {
               if (!p_300836_.contains(s)) {
                  throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_argument", this.getId(), s));
               }

               list.add(stringify(p_300836_.get(s)));
            }

            CommandFunction commandfunction = this.cache.getAndMoveToLast(list);
            if (commandfunction != null) {
               return commandfunction;
            } else {
               if (this.cache.size() >= 8) {
                  this.cache.removeFirst();
               }

               CommandFunction commandfunction1 = this.substituteAndParse(list, p_297591_, p_298828_);
               if (commandfunction1 != null) {
                  this.cache.put(list, commandfunction1);
               }

               return commandfunction1;
            }
         }
      }

      private static String stringify(Tag p_298736_) {
         if (p_298736_ instanceof FloatTag floattag) {
            return DECIMAL_FORMAT.format((double)floattag.getAsFloat());
         } else if (p_298736_ instanceof DoubleTag doubletag) {
            return DECIMAL_FORMAT.format(doubletag.getAsDouble());
         } else if (p_298736_ instanceof ByteTag bytetag) {
            return String.valueOf((int)bytetag.getAsByte());
         } else if (p_298736_ instanceof ShortTag shorttag) {
            return String.valueOf((int)shorttag.getAsShort());
         } else if (p_298736_ instanceof LongTag longtag) {
            return String.valueOf(longtag.getAsLong());
         } else {
            return p_298736_.getAsString();
         }
      }

      private CommandFunction substituteAndParse(List<String> p_300530_, CommandDispatcher<CommandSourceStack> p_299533_, CommandSourceStack p_301159_) throws FunctionInstantiationException {
         CommandFunction.Entry[] acommandfunction$entry = this.getEntries();
         CommandFunction.Entry[] acommandfunction$entry1 = new CommandFunction.Entry[acommandfunction$entry.length];

         for(int i = 0; i < acommandfunction$entry.length; ++i) {
            CommandFunction.Entry commandfunction$entry = acommandfunction$entry[i];
            if (!(commandfunction$entry instanceof CommandFunction.MacroEntry commandfunction$macroentry)) {
               acommandfunction$entry1[i] = commandfunction$entry;
            } else {
               List<String> list = commandfunction$macroentry.parameters();
               List<String> list1 = new ArrayList<>(list.size());

               for(String s : list) {
                  list1.add(p_300530_.get(this.parameters.indexOf(s)));
               }

               String s1 = commandfunction$macroentry.substitute(list1);

               try {
                  ParseResults<CommandSourceStack> parseresults = p_299533_.parse(s1, p_301159_);
                  if (parseresults.getReader().canRead()) {
                     throw Commands.getParseException(parseresults);
                  }

                  acommandfunction$entry1[i] = new CommandFunction.CommandEntry(parseresults);
               } catch (CommandSyntaxException commandsyntaxexception) {
                  throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse", this.getId(), s1, commandsyntaxexception.getMessage()));
               }
            }
         }

         ResourceLocation resourcelocation = this.getId();
         return new CommandFunction(new ResourceLocation(resourcelocation.getNamespace(), resourcelocation.getPath() + "/" + p_300530_.hashCode()), acommandfunction$entry1);
      }

      static {
         DECIMAL_FORMAT.setMaximumFractionDigits(15);
         DECIMAL_FORMAT.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
      }
   }

   @FunctionalInterface
   public interface Entry {
      void execute(ServerFunctionManager p_164885_, CommandSourceStack p_164886_, Deque<ServerFunctionManager.QueuedCommand> p_164887_, int p_164888_, int p_164889_, @Nullable ServerFunctionManager.TraceCallbacks p_164890_) throws CommandSyntaxException;
   }

   public static class FunctionEntry implements CommandFunction.Entry {
      private final CommandFunction.CacheableFunction function;

      public FunctionEntry(CommandFunction p_78019_) {
         this.function = new CommandFunction.CacheableFunction(p_78019_);
      }

      public void execute(ServerFunctionManager p_164902_, CommandSourceStack p_164903_, Deque<ServerFunctionManager.QueuedCommand> p_164904_, int p_164905_, int p_164906_, @Nullable ServerFunctionManager.TraceCallbacks p_164907_) {
         Util.ifElse(this.function.get(p_164902_), (p_164900_) -> {
            CommandFunction.Entry[] acommandfunction$entry = p_164900_.getEntries();
            if (p_164907_ != null) {
               p_164907_.onCall(p_164906_, p_164900_.getId(), acommandfunction$entry.length);
            }

            int i = p_164905_ - p_164904_.size();
            int j = Math.min(acommandfunction$entry.length, i);

            for(int k = j - 1; k >= 0; --k) {
               p_164904_.addFirst(new ServerFunctionManager.QueuedCommand(p_164903_, p_164906_ + 1, acommandfunction$entry[k]));
            }

         }, () -> {
            if (p_164907_ != null) {
               p_164907_.onCall(p_164906_, this.function.getId(), -1);
            }

         });
      }

      public String toString() {
         return "function " + this.function.getId();
      }
   }

   public static class MacroEntry implements CommandFunction.Entry {
      private final List<String> segments;
      private final List<String> parameters;

      public MacroEntry(List<String> p_299524_, List<String> p_299522_) {
         this.segments = p_299524_;
         this.parameters = p_299522_;
      }

      public List<String> parameters() {
         return this.parameters;
      }

      public String substitute(List<String> p_300217_) {
         StringBuilder stringbuilder = new StringBuilder();

         for(int i = 0; i < this.parameters.size(); ++i) {
            stringbuilder.append(this.segments.get(i)).append(p_300217_.get(i));
         }

         if (this.segments.size() > this.parameters.size()) {
            stringbuilder.append(this.segments.get(this.segments.size() - 1));
         }

         return stringbuilder.toString();
      }

      public void execute(ServerFunctionManager p_299564_, CommandSourceStack p_298428_, Deque<ServerFunctionManager.QueuedCommand> p_299632_, int p_298690_, int p_299006_, @Nullable ServerFunctionManager.TraceCallbacks p_297793_) throws CommandSyntaxException {
         throw new IllegalStateException("Tried to execute an uninstantiated macro");
      }
   }
}