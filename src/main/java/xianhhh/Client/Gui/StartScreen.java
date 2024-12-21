package xianhhh.Client.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xianhhh.Utils.RenderUtils;

import java.awt.*;
import java.util.Objects;


public class StartScreen extends Screen {
    public StartScreen() {
        super(Component.translatable("narrator.screen.title"));
    }
    long totalTime = 0;
    int times = 0;
    StringBuilder baseString = new StringBuilder("Starting");
    public void render(GuiGraphics g, int p_281550_, int p_282878_, float p_282465_) {
        Minecraft mc = Minecraft.getInstance();
        int width = g.guiWidth(), height = g.guiHeight();
        int x = (width - mc.font.width(String.valueOf(baseString))) / 2, y = (height - mc.font.lineHeight) / 2;

        totalTime +=  30;
        if (totalTime >= 1000){
            baseString.append(".");
            if (Objects.equals(String.valueOf(baseString), "Starting....")){
                baseString = new StringBuilder("Starting");
            }
            RenderUtils.gui.fill(0, 0, width, height, new Color(42, 42, 42).getRGB());
            RenderUtils.gui.drawString(mc.font, String.valueOf(baseString), x, y, new Color(255, 255, 255, 255).getRGB());
            totalTime = 0;
            times++;
        }else if (times == 15){
            mc.setScreen(new CialloGameMainScreen());
        }
    }
}
