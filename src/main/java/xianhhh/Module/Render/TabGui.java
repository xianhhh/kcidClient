package xianhhh.Module.Render;

import net.minecraft.client.gui.GuiGraphics;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.Render2DEvent;
import xianhhh.Module.Module;
import xianhhh.Utils.RenderUtils;

import java.awt.event.KeyEvent;

public class TabGui extends Module {
    public TabGui() {
        super("TabGui", KeyEvent.VK_H, xianhhh.Module.Category.Render);
    }

    private final GuiGraphics g = RenderUtils.gui;

    @EventTarget
    public void onRender(Render2DEvent e) {

    }
}
