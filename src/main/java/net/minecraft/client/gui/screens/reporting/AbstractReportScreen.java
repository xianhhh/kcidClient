package net.minecraft.client.gui.screens.reporting;

import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.logging.LogUtils;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.GenericWaitingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.client.multiplayer.chat.report.Report;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ThrowingComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractReportScreen<B extends Report.Builder<?>> extends Screen {
   private static final Component REPORT_SENT_MESSAGE = Component.translatable("gui.abuseReport.report_sent_msg");
   private static final Component REPORT_SENDING_TITLE = Component.translatable("gui.abuseReport.sending.title").withStyle(ChatFormatting.BOLD);
   private static final Component REPORT_SENT_TITLE = Component.translatable("gui.abuseReport.sent.title").withStyle(ChatFormatting.BOLD);
   private static final Component REPORT_ERROR_TITLE = Component.translatable("gui.abuseReport.error.title").withStyle(ChatFormatting.BOLD);
   private static final Component REPORT_SEND_GENERIC_ERROR = Component.translatable("gui.abuseReport.send.generic_error");
   protected static final Component SEND_REPORT = Component.translatable("gui.abuseReport.send");
   protected static final Component OBSERVED_WHAT_LABEL = Component.translatable("gui.abuseReport.observed_what");
   protected static final Component SELECT_REASON = Component.translatable("gui.abuseReport.select_reason");
   private static final Component DESCRIBE_PLACEHOLDER = Component.translatable("gui.abuseReport.describe");
   protected static final Component MORE_COMMENTS_LABEL = Component.translatable("gui.abuseReport.more_comments");
   private static final Component MORE_COMMENTS_NARRATION = Component.translatable("gui.abuseReport.comments");
   protected static final int MARGIN = 20;
   protected static final int SCREEN_WIDTH = 280;
   protected static final int SPACING = 8;
   private static final Logger LOGGER = LogUtils.getLogger();
   protected final Screen lastScreen;
   protected final ReportingContext reportingContext;
   protected B reportBuilder;

   protected AbstractReportScreen(Component p_297559_, Screen p_299592_, ReportingContext p_300174_, B p_300351_) {
      super(p_297559_);
      this.lastScreen = p_299592_;
      this.reportingContext = p_300174_;
      this.reportBuilder = p_300351_;
   }

   protected MultiLineEditBox createCommentBox(int p_297252_, int p_301025_, Consumer<String> p_298469_) {
      AbuseReportLimits abusereportlimits = this.reportingContext.sender().reportLimits();
      MultiLineEditBox multilineeditbox = new MultiLineEditBox(this.font, 0, 0, p_297252_, p_301025_, DESCRIBE_PLACEHOLDER, MORE_COMMENTS_NARRATION);
      multilineeditbox.setValue(this.reportBuilder.comments());
      multilineeditbox.setCharacterLimit(abusereportlimits.maxOpinionCommentsLength());
      multilineeditbox.setValueListener(p_298469_);
      return multilineeditbox;
   }

   protected void sendReport() {
      this.reportBuilder.build(this.reportingContext).ifLeft((p_301124_) -> {
         CompletableFuture<?> completablefuture = this.reportingContext.sender().send(p_301124_.id(), p_301124_.reportType(), p_301124_.report());
         this.minecraft.setScreen(GenericWaitingScreen.createWaiting(REPORT_SENDING_TITLE, CommonComponents.GUI_CANCEL, () -> {
            this.minecraft.setScreen(this);
            completablefuture.cancel(true);
         }));
         completablefuture.handleAsync((p_301251_, p_299485_) -> {
            if (p_299485_ == null) {
               this.onReportSendSuccess();
            } else {
               if (p_299485_ instanceof CancellationException) {
                  return null;
               }

               this.onReportSendError(p_299485_);
            }

            return null;
         }, this.minecraft);
      }).ifRight((p_298848_) -> {
         this.displayReportSendError(p_298848_.message());
      });
   }

   private void onReportSendSuccess() {
      this.clearDraft();
      this.minecraft.setScreen(GenericWaitingScreen.createCompleted(REPORT_SENT_TITLE, REPORT_SENT_MESSAGE, CommonComponents.GUI_DONE, () -> {
         this.minecraft.setScreen((Screen)null);
      }));
   }

   private void onReportSendError(Throwable p_297880_) {
      LOGGER.error("Encountered error while sending abuse report", p_297880_);
      Throwable throwable = p_297880_.getCause();
      Component component;
      if (throwable instanceof ThrowingComponent throwingcomponent) {
         component = throwingcomponent.getComponent();
      } else {
         component = REPORT_SEND_GENERIC_ERROR;
      }

      this.displayReportSendError(component);
   }

   private void displayReportSendError(Component p_301245_) {
      Component component = p_301245_.copy().withStyle(ChatFormatting.RED);
      this.minecraft.setScreen(GenericWaitingScreen.createCompleted(REPORT_ERROR_TITLE, component, CommonComponents.GUI_BACK, () -> {
         this.minecraft.setScreen(this);
      }));
   }

   void saveDraft() {
      if (this.reportBuilder.hasContent()) {
         this.reportingContext.setReportDraft(this.reportBuilder.report().copy());
      }

   }

   void clearDraft() {
      this.reportingContext.setReportDraft((Report)null);
   }

   public void onClose() {
      if (this.reportBuilder.hasContent()) {
         this.minecraft.setScreen(new AbstractReportScreen.DiscardReportWarningScreen());
      } else {
         this.minecraft.setScreen(this.lastScreen);
      }

   }

   public void removed() {
      this.saveDraft();
      super.removed();
   }

   @OnlyIn(Dist.CLIENT)
   class DiscardReportWarningScreen extends WarningScreen {
      private static final int BUTTON_MARGIN = 20;
      private static final Component TITLE = Component.translatable("gui.abuseReport.discard.title").withStyle(ChatFormatting.BOLD);
      private static final Component MESSAGE = Component.translatable("gui.abuseReport.discard.content");
      private static final Component RETURN = Component.translatable("gui.abuseReport.discard.return");
      private static final Component DRAFT = Component.translatable("gui.abuseReport.discard.draft");
      private static final Component DISCARD = Component.translatable("gui.abuseReport.discard.discard");

      protected DiscardReportWarningScreen() {
         super(TITLE, MESSAGE, MESSAGE);
      }

      protected void initButtons(int p_300335_) {
         this.addRenderableWidget(Button.builder(RETURN, (p_299113_) -> {
            this.onClose();
         }).pos(this.width / 2 - 155, 100 + p_300335_).build());
         this.addRenderableWidget(Button.builder(DRAFT, (p_301082_) -> {
            AbstractReportScreen.this.saveDraft();
            this.minecraft.setScreen(AbstractReportScreen.this.lastScreen);
         }).pos(this.width / 2 + 5, 100 + p_300335_).build());
         this.addRenderableWidget(Button.builder(DISCARD, (p_299406_) -> {
            AbstractReportScreen.this.clearDraft();
            this.minecraft.setScreen(AbstractReportScreen.this.lastScreen);
         }).pos(this.width / 2 - 75, 130 + p_300335_).build());
      }

      public void onClose() {
         this.minecraft.setScreen(AbstractReportScreen.this);
      }

      public boolean shouldCloseOnEsc() {
         return false;
      }

      protected void renderTitle(GuiGraphics p_299952_) {
         p_299952_.drawString(this.font, this.title, this.width / 2 - 155, 30, -1);
      }
   }
}