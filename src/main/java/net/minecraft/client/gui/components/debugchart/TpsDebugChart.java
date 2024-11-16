package net.minecraft.client.gui.components.debugchart;

import java.util.Locale;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.SampleLogger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TpsDebugChart extends AbstractDebugChart {
   private static final int RED = -65536;
   private static final int YELLOW = -256;
   private static final int GREEN = -16711936;
   private static final int CHART_TOP_VALUE = 50;

   public TpsDebugChart(Font p_298557_, SampleLogger p_298113_) {
      super(p_298557_, p_298113_);
   }

   protected void renderAdditionalLinesAndLabels(GuiGraphics p_297354_, int p_298051_, int p_298343_, int p_299488_) {
      this.drawStringWithShade(p_297354_, "20 TPS", p_298051_ + 1, p_299488_ - 60 + 1);
   }

   protected String toDisplayString(double p_301254_) {
      return String.format(Locale.ROOT, "%d ms", (int)Math.round(toMilliseconds(p_301254_)));
   }

   protected int getSampleHeight(double p_299260_) {
      return (int)Math.round(toMilliseconds(p_299260_) * 60.0D / 50.0D);
   }

   protected int getSampleColor(long p_300761_) {
      return this.getSampleColor(toMilliseconds((double)p_300761_), 0.0D, -16711936, 25.0D, -256, 50.0D, -65536);
   }

   private static double toMilliseconds(double p_300655_) {
      return p_300655_ / 1000000.0D;
   }
}