package xianhhh.Module.Render;

import org.lwjgl.glfw.GLFW;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.Render2DEvent;
import xianhhh.Module.Module;
import xianhhh.Module.ModuleManager;
import xianhhh.Utils.RenderUtils;
import xianhhh.Utils.SortUtils;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ArrayList extends Module {
    public ArrayList() {
        super("ArrayList", GLFW.GLFW_KEY_B, xianhhh.Module.Category.Render);
    }
    @EventTarget
    public void onRender(Render2DEvent r){

        int y = 3;
        java.util.ArrayList<String> moduleArrayList = SortUtils.LengthSort(ModuleManager.EnableModules);
        for (String text : moduleArrayList){
            RenderUtils.gui.drawString(mc.font,text,3,y, new Color(255,255,255,255).getRGB());
            y += 10;
        }

        RenderUtils.drawList(RenderUtils.gui,ModuleManager.EnableModules,new Color(0,0,0),new Color(0,0,0, 70));
    }
}
