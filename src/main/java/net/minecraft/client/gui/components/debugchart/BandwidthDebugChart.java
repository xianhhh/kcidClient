package net.minecraft.client.gui.components.debugchart;

import java.util.Locale;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.util.SampleLogger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BandwidthDebugChart extends AbstractDebugChart {
   private static final int MIN_COLOR = -16711681;
   private static final int MID_COLOR = -6250241;
   private static final int MAX_COLOR = -65536;
   private static final int KILOBYTE = 1024;
   private static final int MEGABYTE = 1048576;
   private static final int CHART_TOP_VALUE = 1048576;

   public BandwidthDebugChart(Font p_298629_, SampleLogger p_299208_) {
      super(p_298629_, p_299208_);
   }

   protected void renderAdditionalLinesAndLabels(GuiGraphics p_298580_, int p_298671_, int p_301018_, int p_300317_) {
      this.drawLabeledLineAtValue(p_298580_, p_298671_, p_301018_, p_300317_, 64);
      this.drawLabeledLineAtValue(p_298580_, p_298671_, p_301018_, p_300317_, 1024);
      this.drawLabeledLineAtValue(p_298580_, p_298671_, p_301018_, p_300317_, 16384);
      this.drawStringWithShade(p_298580_, toDisplayStringInternal(1048576.0D), p_298671_ + 1, p_300317_ - getSampleHeightInternal(1048576.0D) + 1);
   }

   private void drawLabeledLineAtValue(GuiGraphics p_297903_, int p_297652_, int p_298530_, int p_300952_, int p_298161_) {
      this.drawLineWithLabel(p_297903_, p_297652_, p_298530_, p_300952_ - getSampleHeightInternal((double)p_298161_), toDisplayStringInternal((double)p_298161_));
   }

   private void drawLineWithLabel(GuiGraphics p_298208_, int p_300180_, int p_297727_, int p_299631_, String p_301319_) {
      this.drawStringWithShade(p_298208_, p_301319_, p_300180_ + 1, p_299631_ + 1);
      p_298208_.hLine(RenderType.guiOverlay(), p_300180_, p_300180_ + p_297727_ - 1, p_299631_, -1);
   }

   protected String toDisplayString(double p_299768_) {
      return toDisplayStringInternal(toBytesPerSecond(p_299768_));
   }

   private static String toDisplayStringInternal(double p_299142_) {
      if (p_299142_ >= 1048576.0D) {
         return String.format(Locale.ROOT, "%.1f MiB/s", p_299142_ / 1048576.0D);
      } else {
         return p_299142_ >= 1024.0D ? String.format(Locale.ROOT, "%.1f KiB/s", p_299142_ / 1024.0D) : String.format(Locale.ROOT, "%d B/s", Mth.floor(p_299142_));
      }
   }

   protected int getSampleHeight(double p_299298_) {
      return getSampleHeightInternal(toBytesPerSecond(p_299298_));
   }

   private static int getSampleHeightInternal(double p_298407_) {
      return (int)Math.round(Math.log(p_298407_ + 1.0D) * 60.0D / Math.log(1048576.0D));
   }

   protected int getSampleColor(long p_297628_) {
      return this.getSampleColor(toBytesPerSecond((double)p_297628_), 0.0D, -16711681, 8192.0D, -6250241, 1.048576E7D, -65536);
   }

   private static double toBytesPerSecond(double p_298688_) {
      return p_298688_ * 20.0D;
   }
}