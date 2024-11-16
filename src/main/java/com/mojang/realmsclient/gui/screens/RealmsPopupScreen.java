package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RealmsPopupScreen extends RealmsScreen {
   private static final Component POPUP_TEXT = Component.translatable("mco.selectServer.popup");
   private static final Component CLOSE_TEXT = Component.translatable("mco.selectServer.close");
   private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("popup/background");
   private static final ResourceLocation TRIAL_AVAILABLE_SPRITE = new ResourceLocation("icon/trial_available");
   private static final WidgetSprites CROSS_BUTTON_SPRITES = new WidgetSprites(new ResourceLocation("widget/cross_button"), new ResourceLocation("widget/cross_button_highlighted"));
   private static final int BG_TEXTURE_WIDTH = 236;
   private static final int BG_TEXTURE_HEIGHT = 34;
   private static final int BG_BORDER_SIZE = 6;
   private static final int IMAGE_WIDTH = 195;
   private static final int IMAGE_HEIGHT = 152;
   private static final int BUTTON_SPACING = 4;
   private static final int PADDING = 10;
   private static final int WIDTH = 320;
   private static final int HEIGHT = 172;
   private static final int TEXT_WIDTH = 100;
   private static final int BUTTON_WIDTH = 99;
   private static final int CAROUSEL_SWITCH_INTERVAL = 100;
   private static List<ResourceLocation> carouselImages = List.of();
   private final Screen backgroundScreen;
   private final boolean trialAvailable;
   @Nullable
   private Button createTrialButton;
   private int carouselIndex;
   private int carouselTick;

   public RealmsPopupScreen(Screen p_298643_, boolean p_298145_) {
      super(POPUP_TEXT);
      this.backgroundScreen = p_298643_;
      this.trialAvailable = p_298145_;
   }

   public static void updateCarouselImages(ResourceManager p_300048_) {
      Collection<ResourceLocation> collection = p_300048_.listResources("textures/gui/images", (p_299622_) -> {
         return p_299622_.getPath().endsWith(".png");
      }).keySet();
      carouselImages = collection.stream().filter((p_298164_) -> {
         return p_298164_.getNamespace().equals("realms");
      }).toList();
   }

   protected void init() {
      this.backgroundScreen.resize(this.minecraft, this.width, this.height);
      if (this.trialAvailable) {
         this.createTrialButton = this.addRenderableWidget(Button.builder(Component.translatable("mco.selectServer.trial"), (p_298028_) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((p_299772_) -> {
               if (p_299772_) {
                  Util.getPlatform().openUri("https://aka.ms/startjavarealmstrial");
               }

               this.minecraft.setScreen(this);
            }, "https://aka.ms/startjavarealmstrial", true));
         }).bounds(this.right() - 10 - 99, this.bottom() - 10 - 4 - 40, 99, 20).build());
      }

      this.setFocused(this.addRenderableWidget(Button.builder(Component.translatable("mco.selectServer.buy"), (p_297329_) -> {
         this.minecraft.setScreen(new ConfirmLinkScreen((p_297741_) -> {
            if (p_297741_) {
               Util.getPlatform().openUri("https://aka.ms/BuyJavaRealms");
            }

            this.minecraft.setScreen(this);
         }, "https://aka.ms/BuyJavaRealms", true));
      }).bounds(this.right() - 10 - 99, this.bottom() - 10 - 20, 99, 20).build()));
      ImageButton imagebutton = this.addRenderableWidget(new ImageButton(this.left() + 4, this.top() + 4, 14, 14, CROSS_BUTTON_SPRITES, (p_299794_) -> {
         this.onClose();
      }, CLOSE_TEXT));
      imagebutton.setTooltip(Tooltip.create(CLOSE_TEXT));
      int i = 142 - (this.trialAvailable ? 40 : 20);
      FittingMultiLineTextWidget fittingmultilinetextwidget = new FittingMultiLineTextWidget(this.right() - 10 - 100, this.top() + 10, 100, i, POPUP_TEXT, this.font);
      if (fittingmultilinetextwidget.showingScrollBar()) {
         fittingmultilinetextwidget.setWidth(100 - fittingmultilinetextwidget.scrollbarWidth());
      }

      this.addRenderableWidget(fittingmultilinetextwidget);
   }

   public void tick() {
      super.tick();
      if (++this.carouselTick > 100) {
         this.carouselTick = 0;
         this.carouselIndex = (this.carouselIndex + 1) % carouselImages.size();
      }

   }

   public void render(GuiGraphics p_299956_, int p_297292_, int p_300990_, float p_300510_) {
      super.render(p_299956_, p_297292_, p_300990_, p_300510_);
      if (this.createTrialButton != null) {
         renderDiamond(p_299956_, this.createTrialButton);
      }

   }

   public static void renderDiamond(GuiGraphics p_299842_, Button p_300723_) {
      int i = 8;
      p_299842_.pose().pushPose();
      p_299842_.pose().translate(0.0F, 0.0F, 110.0F);
      p_299842_.blitSprite(TRIAL_AVAILABLE_SPRITE, p_300723_.getX() + p_300723_.getWidth() - 8 - 4, p_300723_.getY() + p_300723_.getHeight() / 2 - 4, 8, 8);
      p_299842_.pose().popPose();
   }

   public void renderBackground(GuiGraphics p_297997_, int p_300562_, int p_297822_, float p_297261_) {
      this.backgroundScreen.render(p_297997_, -1, -1, p_297261_);
      p_297997_.flush();
      RenderSystem.clear(256, Minecraft.ON_OSX);
      this.renderTransparentBackground(p_297997_);
      p_297997_.blitSprite(BACKGROUND_SPRITE, this.left(), this.top(), 320, 172);
      if (!carouselImages.isEmpty()) {
         p_297997_.blit(carouselImages.get(this.carouselIndex), this.left() + 10, this.top() + 10, 0, 0.0F, 0.0F, 195, 152, 195, 152);
      }

   }

   private int left() {
      return (this.width - 320) / 2;
   }

   private int top() {
      return (this.height - 172) / 2;
   }

   private int right() {
      return this.left() + 320;
   }

   private int bottom() {
      return this.top() + 172;
   }

   public void onClose() {
      this.minecraft.setScreen(this.backgroundScreen);
   }
}