package xianhhh.Client.Gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xianhhh.Module.ModuleManager;
import xianhhh.Utils.RenderUtils;

import java.awt.*;

public class CialloGameMainScreen extends Screen {

    private Color ccc = new Color(0, 0, 0, 40);
    private Color tsc = new Color(1f, 1f, 1f, 1f);
    private final ResourceLocation r = new ResourceLocation("pc/dick.png");
    private final ResourceLocation q = new ResourceLocation("pc/qlwh.png");

    public CialloGameMainScreen() {
        super(Component.translatable("narrator.screen.title"));
    }


    public void render(GuiGraphics g, int p_281550_, int p_282878_, float p_282465_) {

        super.render(g, p_281550_, p_282878_, p_282465_);
        g.drawString(this.font, "ZZZZZ", this.width - RenderUtils.getStringWidth("ZZZZZ").intValue(), this.height - 10, Color.WHITE.getRGB());
        RenderUtils.drawList(g, ModuleManager.SmodulesA, Color.BLACK, new Color(0, 0, 0, 70));
        button(g);
    }

    public void button(GuiGraphics g) {

        this.addWidget(Button.builder(Component.translatable("menu.singleplayer"),
                (z) -> {
                    this.minecraft.setScreen(new SelectWorldScreen(this));
                }
        ).bounds(0, this.height / 6 *2, this.width / 4, 35).build());

        this.addWidget(Button.builder(Component.translatable("menu.multiplayer"),
                (z) -> {
                    Screen screen = (Screen) (this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this));
                    this.minecraft.setScreen(screen);
                }
        ).bounds(0, this.height / 6 * 2+43, this.width / 4, 35).build());

        this.addWidget(Button.builder(Component.translatable("menu.options"),
                (z) -> {
                    this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
                }
        ).bounds(0, this.height / 10 * 7 + 10 + 7 + 4 + 3  , this.width / 4, this.height / 10).build());





/*
        this.addRenderableWidget(Button.builder(Component.translatable("menu.singleplayer"),
                (z) -> {
                    this.minecraft.setScreen(new SelectWorldScreen(this));
                }
        ).bounds(0, this.height / 6 *2, this.width / 4, 35).build());

        this.addRenderableWidget(Button.builder(Component.translatable("menu.multiplayer"),
                (z) -> {
                    Screen screen = (Screen) (this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this));
                    this.minecraft.setScreen(screen);
                }
        ).bounds(0, this.height / 6 * 2+43, this.width / 4, 35).build());

        this.addRenderableWidget(Button.builder(Component.translatable("menu.options"),
                (z) -> {
                    this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
                }
        ).bounds(0, this.height / 10 * 7 + 10  , this.width / 4, this.height / 10).build());
*/

    }

    private void fastfill(GuiGraphics g,int x, int y, int width, int height, Color v){
        g.fill(x, y, x + width, y + height, v.getRGB());
    }

    protected void init(){

    }

    public void renderBackground(GuiGraphics g,int p_299421_, int p_298679_, float p_297268_){
        g.fillGradient(0, 0, this.width, this.height, Color.WHITE.getRGB(), Color.WHITE.getRGB());
        //RenderUtils.fastblit(g,r,0,0,this.width,this.height,this.width,this.height);
        RenderUtils.fastblit(g, q, 0, 0, this.width, this.height, this.width, this.height);
    }
}
