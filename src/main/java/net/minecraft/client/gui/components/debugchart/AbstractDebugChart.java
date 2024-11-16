package net.minecraft.client.gui.components.debugchart;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.SampleLogger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractDebugChart {
   protected static final int COLOR_GREY = 14737632;
   protected static final int CHART_HEIGHT = 60;
   protected static final int LINE_WIDTH = 1;
   protected final Font font;
   protected final SampleLogger logger;

   protected AbstractDebugChart(Font p_297994_, SampleLogger p_298236_) {
      this.font = p_297994_;
      this.logger = p_298236_;
   }

   public int getWidth(int p_300792_) {
      return Math.min(this.logger.capacity() + 2, p_300792_);
   }

   public void drawChart(GuiGraphics p_300681_, int p_298472_, int p_298870_) {
      int i = p_300681_.guiHeight();
      p_300681_.fill(RenderType.guiOverlay(), p_298472_, i - 60, p_298472_ + p_298870_, i, -1873784752);
      long j = 0L;
      long k = 2147483647L;
      long l = -2147483648L;
      int i1 = Math.max(0, this.logger.capacity() - (p_298870_ - 2));
      int j1 = this.logger.size() - i1;

      for(int k1 = 0; k1 < j1; ++k1) {
         int l1 = p_298472_ + k1 + 1;
         long i2 = this.logger.get(i1 + k1);
         k = Math.min(k, i2);
         l = Math.max(l, i2);
         j += i2;
         int j2 = this.getSampleHeight((double)i2);
         int k2 = this.getSampleColor(i2);
         p_300681_.fill(RenderType.guiOverlay(), l1, i - j2, l1 + 1, i, k2);
      }

      p_300681_.hLine(RenderType.guiOverlay(), p_298472_, p_298472_ + p_298870_ - 1, i - 60, -1);
      p_300681_.hLine(RenderType.guiOverlay(), p_298472_, p_298472_ + p_298870_ - 1, i - 1, -1);
      p_300681_.vLine(RenderType.guiOverlay(), p_298472_, i - 60, i, -1);
      p_300681_.vLine(RenderType.guiOverlay(), p_298472_ + p_298870_ - 1, i - 60, i, -1);
      if (j1 > 0) {
         String s = this.toDisplayString((double)k) + " min";
         String s1 = this.toDisplayString((double)j / (double)j1) + " avg";
         String s2 = this.toDisplayString((double)l) + " max";
         p_300681_.drawString(this.font, s, p_298472_ + 2, i - 60 - 9, 14737632);
         p_300681_.drawCenteredString(this.font, s1, p_298472_ + p_298870_ / 2, i - 60 - 9, 14737632);
         p_300681_.drawString(this.font, s2, p_298472_ + p_298870_ - this.font.width(s2) - 2, i - 60 - 9, 14737632);
      }

      this.renderAdditionalLinesAndLabels(p_300681_, p_298472_, p_298870_, i);
   }

   protected void renderAdditionalLinesAndLabels(GuiGraphics p_300007_, int p_299062_, int p_300355_, int p_297248_) {
   }

   protected void drawStringWithShade(GuiGraphics p_300760_, String p_299957_, int p_301259_, int p_298524_) {
      p_300760_.fill(RenderType.guiOverlay(), p_301259_, p_298524_, p_301259_ + this.font.width(p_299957_) + 1, p_298524_ + 9, -1873784752);
      p_300760_.drawString(this.font, p_299957_, p_301259_ + 1, p_298524_ + 1, 14737632, false);
   }

   protected abstract String toDisplayString(double p_299846_);

   protected abstract int getSampleHeight(double p_298917_);

   protected abstract int getSampleColor(long p_301058_);

   protected int getSampleColor(double p_300651_, double p_300082_, int p_298618_, double p_299706_, int p_300095_, double p_298068_, int p_299403_) {
      p_300651_ = Mth.clamp(p_300651_, p_300082_, p_298068_);
      return p_300651_ < p_299706_ ? FastColor.ARGB32.lerp((float)(p_300651_ / (p_299706_ - p_300082_)), p_298618_, p_300095_) : FastColor.ARGB32.lerp((float)((p_300651_ - p_299706_) / (p_298068_ - p_299706_)), p_300095_, p_299403_);
   }
}