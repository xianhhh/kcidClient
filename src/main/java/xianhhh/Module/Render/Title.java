package xianhhh.Module.Render;

import net.minecraft.world.entity.Entity;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.Render3DEvent;
import xianhhh.Event.Events.RenderEvent;
import xianhhh.Module.Module;
import xianhhh.Module.ModuleManager;
import xianhhh.Utils.RenderUtils;
import xianhhh.Utils.WorldUtils;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Title extends Module {
    public Title() {
        super("Title", KeyEvent.VK_Z, xianhhh.Module.Category.Render);
    }
    @EventTarget
    public void render(RenderEvent r){
        int y = 60;
        render.gui.drawString(mc.font,mc.fpsString,50,40,new Color(255,255,255).getRGB());
        for(Module m : ModuleManager.modulesA){
            render.gui.drawString(mc.font,m.getName() + " " + m.isToggle() + " key: " +KeyEvent.getKeyText(m.getKey()),50,y,new Color(255,255,255).getRGB());
            y += 8;
        }
    }
}
