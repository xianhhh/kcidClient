package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import xianhhh.Event.Events.RenderEvent;
import xianhhh.Module.Module;
import xianhhh.Utils.RenderUtils;

import java.awt.event.KeyEvent;

public class Logo extends Module {

    public Logo() {
        super("Logo", KeyEvent.VK_U, xianhhh.Module.Category.Render);
    }
    private final ResourceLocation png = new ResourceLocation("pc/bmw.png");
    private final GuiGraphics g = RenderUtils.gui;
    @Subscribe
    public void onRender(RenderEvent r){
        RenderUtils.fastblit(g,png,0,0,mc.getWindow().getWidth() / 8,mc.getWindow().getHeight() / 8,mc.getWindow().getWidth() / 8,mc.getWindow().getHeight() / 8);
    }
}
