package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import xianhhh.Event.Events.RenderEvent;
import xianhhh.Module.Module;
import xianhhh.Module.ModuleManager;
import xianhhh.Utils.RenderUtils;
import xianhhh.Utils.SortUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ModuleList extends Module {
    public ModuleList() {
        super("ModuleList", KeyEvent.VK_B, xianhhh.Module.Category.Render);
    }
    @Subscribe
    public void onRender(RenderEvent r){
        // NEW ArrayList By PixelSkider

        int y = 3;
        ArrayList<String> moduleArrayList = SortUtils.LengthSort(ModuleManager.EnableModules);
        for (String text : moduleArrayList){
            RenderUtils.gui.drawString(mc.font,text,3,y, new Color(255,255,255,255).getRGB());
            y += 10;
        }

        // OLD RenderUtils.drawList(RenderUtils.gui,ModuleManager.EnableModules,Color.BLACK,new Color(0, 0, 0, 70));
    }
}
