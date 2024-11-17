package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import xianhhh.Event.Events.RenderEvent;
import xianhhh.Module.Module;
import xianhhh.Module.ModuleManager;
import xianhhh.Utils.RenderUtils;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ArrayList extends Module {
    public ArrayList() {
        super("ArrayList", KeyEvent.VK_B, xianhhh.Module.Category.Render);
    }
    @Subscribe
    public void onRender(RenderEvent r){
        // NEW ArrayList By PixelSkider

        int y = 0;
        for (String module : ModuleManager.EnableModules){
            RenderUtils.gui.drawString(mc.font,module,0,y,new Color(255,255,255,255).getRGB());
            y += mc.font.lineHeight;
        }

        // OLD RenderUtils.drawList(RenderUtils.gui,ModuleManager.EnableModules,Color.BLACK,new Color(0, 0, 0, 70));
    }
}
