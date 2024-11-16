package net.minecraft.world.scores;

import java.util.function.IntFunction;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public enum DisplaySlot implements StringRepresentable {
   LIST(0, "list"),
   SIDEBAR(1, "sidebar"),
   BELOW_NAME(2, "below_name"),
   TEAM_BLACK(3, "sidebar.team.black"),
   TEAM_DARK_BLUE(4, "sidebar.team.dark_blue"),
   TEAM_DARK_GREEN(5, "sidebar.team.dark_green"),
   TEAM_DARK_AQUA(6, "sidebar.team.dark_aqua"),
   TEAM_DARK_RED(7, "sidebar.team.dark_red"),
   TEAM_DARK_PURPLE(8, "sidebar.team.dark_purple"),
   TEAM_GOLD(9, "sidebar.team.gold"),
   TEAM_GRAY(10, "sidebar.team.gray"),
   TEAM_DARK_GRAY(11, "sidebar.team.dark_gray"),
   TEAM_BLUE(12, "sidebar.team.blue"),
   TEAM_GREEN(13, "sidebar.team.green"),
   TEAM_AQUA(14, "sidebar.team.aqua"),
   TEAM_RED(15, "sidebar.team.red"),
   TEAM_LIGHT_PURPLE(16, "sidebar.team.light_purple"),
   TEAM_YELLOW(17, "sidebar.team.yellow"),
   TEAM_WHITE(18, "sidebar.team.white");

   public static final StringRepresentable.EnumCodec<DisplaySlot> CODEC = StringRepresentable.fromEnum(DisplaySlot::values);
   public static final IntFunction<DisplaySlot> BY_ID = ByIdMap.continuous(DisplaySlot::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
   private final int id;
   private final String name;

   private DisplaySlot(int p_299274_, String p_299536_) {
      this.id = p_299274_;
      this.name = p_299536_;
   }

   public int id() {
      return this.id;
   }

   public String getSerializedName() {
      return this.name;
   }

   @Nullable
   public static DisplaySlot teamColorToSlot(ChatFormatting p_298500_) {
      DisplaySlot displayslot;
      switch (p_298500_) {
         case BLACK:
            displayslot = TEAM_BLACK;
            break;
         case DARK_BLUE:
            displayslot = TEAM_DARK_BLUE;
            break;
         case DARK_GREEN:
            displayslot = TEAM_DARK_GREEN;
            break;
         case DARK_AQUA:
            displayslot = TEAM_DARK_AQUA;
            break;
         case DARK_RED:
            displayslot = TEAM_DARK_RED;
            break;
         case DARK_PURPLE:
            displayslot = TEAM_DARK_PURPLE;
            break;
         case GOLD:
            displayslot = TEAM_GOLD;
            break;
         case GRAY:
            displayslot = TEAM_GRAY;
            break;
         case DARK_GRAY:
            displayslot = TEAM_DARK_GRAY;
            break;
         case BLUE:
            displayslot = TEAM_BLUE;
            break;
         case GREEN:
            displayslot = TEAM_GREEN;
            break;
         case AQUA:
            displayslot = TEAM_AQUA;
            break;
         case RED:
            displayslot = TEAM_RED;
            break;
         case LIGHT_PURPLE:
            displayslot = TEAM_LIGHT_PURPLE;
            break;
         case YELLOW:
            displayslot = TEAM_YELLOW;
            break;
         case WHITE:
            displayslot = TEAM_WHITE;
            break;
         case BOLD:
         case ITALIC:
         case UNDERLINE:
         case RESET:
         case OBFUSCATED:
         case STRIKETHROUGH:
            displayslot = null;
            break;
         default:
            throw new IncompatibleClassChangeError();
      }

      return displayslot;
   }
}