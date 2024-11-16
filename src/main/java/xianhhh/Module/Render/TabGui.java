package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiGraphics;
import xianhhh.Event.Events.RenderEvent;
import xianhhh.Module.Module;
import xianhhh.Utils.RenderUtils;

import java.awt.event.KeyEvent;

public class TabGui extends Module {
    public TabGui() {
        super("TabGui", KeyEvent.VK_H, xianhhh.Module.Category.Render);
    }

    private final GuiGraphics g = RenderUtils.gui;

    @Subscribe
    public void onRender(RenderEvent e) {

    }
}
