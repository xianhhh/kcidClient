package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FocusableTextWidget extends MultiLineTextWidget {
   private static final int BACKGROUND_COLOR = 1426063360;
   private static final int PADDING = 4;
   private final boolean alwaysShowBorder;

   public FocusableTextWidget(int p_298289_, Component p_300031_, Font p_298235_) {
      this(p_298289_, p_300031_, p_298235_, true);
   }

   public FocusableTextWidget(int p_299147_, Component p_299786_, Font p_299475_, boolean p_299140_) {
      super(p_299786_, p_299475_);
      this.setMaxWidth(p_299147_);
      this.setCentered(true);
      this.active = true;
      this.alwaysShowBorder = p_299140_;
   }

   protected void updateWidgetNarration(NarrationElementOutput p_300724_) {
      p_300724_.add(NarratedElementType.TITLE, this.getMessage());
   }

   public void renderWidget(GuiGraphics p_297672_, int p_301298_, int p_300386_, float p_299545_) {
      if (this.isFocused() || this.alwaysShowBorder) {
         int i = this.getX() - 4;
         int j = this.getY() - 4;
         int k = this.getWidth() + 8;
         int l = this.getHeight() + 8;
         int i1 = this.alwaysShowBorder ? (this.isFocused() ? -1 : -6250336) : -1;
         p_297672_.fill(i + 1, j, i + k, j + l, 1426063360);
         p_297672_.renderOutline(i, j, k, l, i1);
      }

      super.renderWidget(p_297672_, p_301298_, p_300386_, p_299545_);
   }

   public void playDownSound(SoundManager p_297351_) {
   }
}