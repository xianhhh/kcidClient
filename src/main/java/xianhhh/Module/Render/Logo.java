package xianhhh.Module.Render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.Render2DEvent;
import xianhhh.Module.Module;
import xianhhh.Utils.RenderUtils;

import java.awt.event.KeyEvent;

public class Logo extends Module {

    public Logo() {
        super("Logo", KeyEvent.VK_U, xianhhh.Module.Category.Render);
    }
    private final ResourceLocation png = new ResourceLocation("pc/bmw.png");
    private final GuiGraphics g = RenderUtils.gui;
    @EventTarget
    public void onRender(Render2DEvent r){
        RenderUtils.fastblit(g,png,0,0,mc.getWindow().getWidth() / 8,mc.getWindow().getHeight() / 8,mc.getWindow().getWidth() / 8,mc.getWindow().getHeight() / 8);
    }
}
