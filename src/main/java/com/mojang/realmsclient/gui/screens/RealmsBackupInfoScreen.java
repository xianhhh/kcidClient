package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.Backup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class RealmsBackupInfoScreen extends RealmsScreen {
   private static final Component TITLE = Component.translatable("mco.backup.info.title");
   private static final Component UNKNOWN = Component.translatable("mco.backup.unknown");
   private final Screen lastScreen;
   final Backup backup;
   final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private RealmsBackupInfoScreen.BackupInfoList backupInfoList;

   public RealmsBackupInfoScreen(Screen p_88048_, Backup p_88049_) {
      super(TITLE);
      this.lastScreen = p_88048_;
      this.backup = p_88049_;
   }

   public void init() {
      this.layout.addToHeader(new StringWidget(TITLE, this.font));
      this.backupInfoList = new RealmsBackupInfoScreen.BackupInfoList(this.minecraft);
      this.addRenderableWidget(this.backupInfoList);
      this.layout.addToFooter(Button.builder(CommonComponents.GUI_BACK, (p_296040_) -> {
         this.onClose();
      }).build());
      this.layout.arrangeElements();
      this.layout.visitWidgets((p_296042_) -> {
         AbstractWidget abstractwidget = this.addRenderableWidget(p_296042_);
      });
   }

   protected void repositionElements() {
      this.layout.arrangeElements();
      this.backupInfoList.updateSize(this.width, this.height, this.layout.getHeaderHeight(), this.height - this.layout.getFooterHeight());
   }

   public void onClose() {
      this.minecraft.setScreen(this.lastScreen);
   }

   Component checkForSpecificMetadata(String p_88068_, String p_88069_) {
      String s = p_88068_.toLowerCase(Locale.ROOT);
      if (s.contains("game") && s.contains("mode")) {
         return this.gameModeMetadata(p_88069_);
      } else {
         return (Component)(s.contains("game") && s.contains("difficulty") ? this.gameDifficultyMetadata(p_88069_) : Component.literal(p_88069_));
      }
   }

   private Component gameDifficultyMetadata(String p_88074_) {
      try {
         return RealmsSlotOptionsScreen.DIFFICULTIES.get(Integer.parseInt(p_88074_)).getDisplayName();
      } catch (Exception exception) {
         return UNKNOWN;
      }
   }

   private Component gameModeMetadata(String p_88076_) {
      try {
         return RealmsSlotOptionsScreen.GAME_MODES.get(Integer.parseInt(p_88076_)).getShortDisplayName();
      } catch (Exception exception) {
         return UNKNOWN;
      }
   }

   @OnlyIn(Dist.CLIENT)
   class BackupInfoList extends ObjectSelectionList<RealmsBackupInfoScreen.BackupInfoListEntry> {
      public BackupInfoList(Minecraft p_88082_) {
         super(p_88082_, RealmsBackupInfoScreen.this.width, RealmsBackupInfoScreen.this.height, RealmsBackupInfoScreen.this.layout.getHeaderHeight(), RealmsBackupInfoScreen.this.height - RealmsBackupInfoScreen.this.layout.getFooterHeight(), 36);
         if (RealmsBackupInfoScreen.this.backup.changeList != null) {
            RealmsBackupInfoScreen.this.backup.changeList.forEach((p_88084_, p_88085_) -> {
               this.addEntry(RealmsBackupInfoScreen.this.new BackupInfoListEntry(p_88084_, p_88085_));
            });
         }

      }
   }

   @OnlyIn(Dist.CLIENT)
   class BackupInfoListEntry extends ObjectSelectionList.Entry<RealmsBackupInfoScreen.BackupInfoListEntry> {
      private static final Component TEMPLATE_NAME = Component.translatable("mco.backup.entry.templateName");
      private static final Component GAME_DIFFICULTY = Component.translatable("mco.backup.entry.gameDifficulty");
      private static final Component NAME = Component.translatable("mco.backup.entry.name");
      private static final Component GAME_SERVER_VERSION = Component.translatable("mco.backup.entry.gameServerVersion");
      private static final Component UPLOADED = Component.translatable("mco.backup.entry.uploaded");
      private static final Component ENABLED_PACK = Component.translatable("mco.backup.entry.enabledPack");
      private static final Component DESCRIPTION = Component.translatable("mco.backup.entry.description");
      private static final Component GAME_MODE = Component.translatable("mco.backup.entry.gameMode");
      private static final Component SEED = Component.translatable("mco.backup.entry.seed");
      private static final Component WORLD_TYPE = Component.translatable("mco.backup.entry.worldType");
      private static final Component UNDEFINED = Component.translatable("mco.backup.entry.undefined");
      private final String key;
      private final String value;

      public BackupInfoListEntry(String p_88091_, String p_88092_) {
         this.key = p_88091_;
         this.value = p_88092_;
      }

      public void render(GuiGraphics p_282911_, int p_281482_, int p_283643_, int p_282795_, int p_283291_, int p_282540_, int p_282181_, int p_283535_, boolean p_281916_, float p_282116_) {
         p_282911_.drawString(RealmsBackupInfoScreen.this.font, this.translateKey(this.key), p_282795_, p_283643_, -6250336);
         p_282911_.drawString(RealmsBackupInfoScreen.this.font, RealmsBackupInfoScreen.this.checkForSpecificMetadata(this.key, this.value), p_282795_, p_283643_ + 12, -1);
      }

      private Component translateKey(String p_287652_) {
         Component component;
         switch (p_287652_) {
            case "template_name":
               component = TEMPLATE_NAME;
               break;
            case "game_difficulty":
               component = GAME_DIFFICULTY;
               break;
            case "name":
               component = NAME;
               break;
            case "game_server_version":
               component = GAME_SERVER_VERSION;
               break;
            case "uploaded":
               component = UPLOADED;
               break;
            case "enabled_pack":
               component = ENABLED_PACK;
               break;
            case "description":
               component = DESCRIPTION;
               break;
            case "game_mode":
               component = GAME_MODE;
               break;
            case "seed":
               component = SEED;
               break;
            case "world_type":
               component = WORLD_TYPE;
               break;
            default:
               component = UNDEFINED;
         }

         return component;
      }

      public Component getNarration() {
         return Component.translatable("narrator.select", this.key + " " + this.value);
      }
   }
}