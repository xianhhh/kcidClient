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
        RenderUtils.drawList(RenderUtils.gui,ModuleManager.EnableModules,Color.BLACK,new Color(0, 0, 0, 70));
    }
}
