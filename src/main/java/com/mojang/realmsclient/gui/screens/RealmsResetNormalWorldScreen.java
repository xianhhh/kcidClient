package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.util.LevelType;
import com.mojang.realmsclient.util.WorldGenerationInfo;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.CommonLayouts;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class RealmsResetNormalWorldScreen extends RealmsScreen {
   private static final Component SEED_LABEL = Component.translatable("mco.reset.world.seed");
   public static final Component TITLE = Component.translatable("mco.reset.world.generate");
   private static final int BUTTON_SPACING = 10;
   private static final int CONTENT_WIDTH = 210;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private final Consumer<WorldGenerationInfo> callback;
   private EditBox seedEdit;
   private LevelType levelType = LevelType.DEFAULT;
   private boolean generateStructures = true;
   private final Component buttonTitle;

   public RealmsResetNormalWorldScreen(Consumer<WorldGenerationInfo> p_167438_, Component p_167439_) {
      super(TITLE);
      this.callback = p_167438_;
      this.buttonTitle = p_167439_;
   }

   public void init() {
      this.seedEdit = new EditBox(this.font, 210, 20, Component.translatable("mco.reset.world.seed"));
      this.seedEdit.setMaxLength(32);
      this.setInitialFocus(this.seedEdit);
      this.layout.addToHeader(new StringWidget(this.title, this.font));
      LinearLayout linearlayout = this.layout.addToContents(LinearLayout.vertical()).spacing(10);
      linearlayout.addChild(CommonLayouts.labeledElement(this.font, this.seedEdit, SEED_LABEL));
      linearlayout.addChild(CycleButton.builder(LevelType::getName).withValues(LevelType.values()).withInitialValue(this.levelType).create(0, 0, 210, 20, Component.translatable("selectWorld.mapType"), (p_167441_, p_167442_) -> {
         this.levelType = p_167442_;
      }));
      linearlayout.addChild(CycleButton.onOffBuilder(this.generateStructures).create(0, 0, 210, 20, Component.translatable("selectWorld.mapFeatures"), (p_167444_, p_167445_) -> {
         this.generateStructures = p_167445_;
      }));
      LinearLayout linearlayout1 = this.layout.addToFooter(LinearLayout.horizontal().spacing(10));
      linearlayout1.addChild(Button.builder(this.buttonTitle, (p_296074_) -> {
         this.callback.accept(this.createWorldGenerationInfo());
      }).build());
      linearlayout1.addChild(Button.builder(CommonComponents.GUI_BACK, (p_89288_) -> {
         this.onClose();
      }).build());
      this.layout.visitWidgets((p_296076_) -> {
         AbstractWidget abstractwidget = this.addRenderableWidget(p_296076_);
      });
      this.repositionElements();
   }

   private WorldGenerationInfo createWorldGenerationInfo() {
      return new WorldGenerationInfo(this.seedEdit.getValue(), this.levelType, this.generateStructures);
   }

   protected void repositionElements() {
      this.layout.arrangeElements();
   }

   public void onClose() {
      this.callback.accept((WorldGenerationInfo)null);
   }
}