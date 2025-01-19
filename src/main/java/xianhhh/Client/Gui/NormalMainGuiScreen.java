package xianhhh.Client.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import xianhhh.Utils.RenderUtils;

import java.awt.*;

public class NormalMainGuiScreen extends Screen {
    public NormalMainGuiScreen() {
        super(Component.translatable("narrator.screen.title"));
    }

    @Override
    public void renderDirtBackground(GuiGraphics g) {
        super.renderDirtBackground(g);
        g.fill(0, 0, this.width, this.height, Color.WHITE.getRGB());
    }

    public void render(GuiGraphics g, int p_281550_, int p_282878_, float p_282465_) {
        super.render(g, p_281550_, p_282878_, p_282465_);
        addButton();
        renderButtonBox(g);
    }

    private final Component singleplayer = Component.translatable("menu.singleplayer");
    private final Component muiltPlayer = Component.translatable("menu.multiplayer");
    private final Component options = Component.translatable("menu.options");
    private final Font mcF = Minecraft.getInstance().font;

    public void addButton() {
        this.addWidget(addMiddleButton(this.width / 2, this.height / 2, mcF.width(singleplayer.getString()), 10, singleplayer, (z) -> {
            minecraft.setScreen(new SelectWorldScreen(this));
        }));

        this.addWidget(addMiddleButton(this.width / 2, this.height / 2 + 15, mcF.width(muiltPlayer.getString()), 10, muiltPlayer, (z) -> {
            minecraft.setScreen(new JoinMultiplayerScreen(this));
        }));

        this.addWidget(addMiddleButton(this.width / 2, this.height / 2 + 30, mcF.width(options.getString()), 10, options, (z) -> {
            minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));
    }


    public Button addMiddleButton(int x, int y, int width, int height, Component text, Button.OnPress onpress) {
        return new Button.Builder(text, onpress).bounds(x - width / 2, y, width, height).build();
    }


    public void drawDebug(GuiGraphics g) {
        g.fill(this.width / 2, 0, this.width / 2 + 2, this.height, Color.RED.getRGB());
    }

    public void renderButtonBox(GuiGraphics g) {
        //drawDebug(g);
        RenderUtils.renderRoundFastMiddle(g.pose(), this.width / 2, this.height / 2, mcF.width(singleplayer.getString()), 10, 4, 1000, Color.BLACK);
        RenderUtils.renderRoundFastMiddle(g.pose(), this.width / 2, this.height / 2 + 15, mcF.width(muiltPlayer.getString()), 10, 4, 1000, Color.BLACK);
        RenderUtils.renderRoundFastMiddle(g.pose(), this.width / 2, this.height / 2 + 30, mcF.width(options.getString()), 10, 4, 1000, Color.BLACK);
        renderText(g);
    }

    public void renderText(GuiGraphics g) {
        g.drawCenteredString(mcF, singleplayer, this.width / 2, (this.height / 2 - 10 / 2) + 6, Color.WHITE.getRGB());
        g.drawCenteredString(mcF, muiltPlayer, this.width / 2, (this.height / 2 - 10 / 2) + 6 + 15, Color.WHITE.getRGB());
        g.drawCenteredString(mcF, options, this.width / 2, (this.height / 2 - 10 / 2) + 6 + 30, Color.WHITE.getRGB());
    }


    @Override
    protected void init() {

    }
}
