package net.minecraft.client.gui.screens.reporting;

import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.CommonLayouts;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.chat.report.NameReport;
import net.minecraft.client.multiplayer.chat.report.Report;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NameReportScreen extends AbstractReportScreen<NameReport.Builder> {
   private static final int BUTTON_WIDTH = 120;
   private static final Component TITLE = Component.translatable("gui.abuseReport.name.title");
   private final LinearLayout layout = LinearLayout.vertical().spacing(8);
   private MultiLineEditBox commentBox;
   private Button sendButton;

   private NameReportScreen(Screen p_300534_, ReportingContext p_300915_, NameReport.Builder p_300014_) {
      super(TITLE, p_300534_, p_300915_, p_300014_);
   }

   public NameReportScreen(Screen p_300152_, ReportingContext p_300083_, UUID p_298096_, String p_300249_) {
      this(p_300152_, p_300083_, new NameReport.Builder(p_298096_, p_300249_, p_300083_.sender().reportLimits()));
   }

   public NameReportScreen(Screen p_300445_, ReportingContext p_299367_, NameReport p_297896_) {
      this(p_300445_, p_299367_, new NameReport.Builder(p_297896_, p_299367_.sender().reportLimits()));
   }

   protected void init() {
      this.layout.defaultCellSetting().alignHorizontallyCenter();
      this.layout.addChild(new StringWidget(this.title, this.font));
      Component component = Component.literal(this.reportBuilder.report().getReportedName()).withStyle(ChatFormatting.YELLOW);
      this.layout.addChild(new StringWidget(Component.translatable("gui.abuseReport.name.reporting", component), this.font), (p_297722_) -> {
         p_297722_.alignHorizontallyLeft().padding(0, 8);
      });
      this.commentBox = this.createCommentBox(280, 9 * 8, (p_299894_) -> {
         this.reportBuilder.setComments(p_299894_);
         this.onReportChanged();
      });
      this.layout.addChild(CommonLayouts.labeledElement(this.font, this.commentBox, MORE_COMMENTS_LABEL, (p_299823_) -> {
         p_299823_.paddingBottom(12);
      }));
      LinearLayout linearlayout = this.layout.addChild(LinearLayout.horizontal().spacing(8));
      linearlayout.addChild(Button.builder(CommonComponents.GUI_BACK, (p_298578_) -> {
         this.onClose();
      }).width(120).build());
      this.sendButton = linearlayout.addChild(Button.builder(SEND_REPORT, (p_299575_) -> {
         this.sendReport();
      }).width(120).build());
      this.onReportChanged();
      this.layout.visitWidgets((p_297689_) -> {
         AbstractWidget abstractwidget = this.addRenderableWidget(p_297689_);
      });
      this.repositionElements();
   }

   protected void repositionElements() {
      this.layout.arrangeElements();
      FrameLayout.centerInRectangle(this.layout, this.getRectangle());
   }

   private void onReportChanged() {
      Report.CannotBuildReason report$cannotbuildreason = this.reportBuilder.checkBuildable();
      this.sendButton.active = report$cannotbuildreason == null;
      this.sendButton.setTooltip(Optionull.map(report$cannotbuildreason, Report.CannotBuildReason::tooltip));
   }

   public boolean mouseReleased(double p_297585_, double p_300170_, int p_297299_) {
      return super.mouseReleased(p_297585_, p_300170_, p_297299_) ? true : this.commentBox.mouseReleased(p_297585_, p_300170_, p_297299_);
   }
}