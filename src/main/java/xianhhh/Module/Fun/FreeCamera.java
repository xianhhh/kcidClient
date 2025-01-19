package xianhhh.Module.Fun;

import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.TickEvent;
import xianhhh.Module.Module;

import java.awt.event.KeyEvent;

public class FreeCamera extends Module {
    public FreeCamera() {
        super("FreeCamera", KeyEvent.VK_F, xianhhh.Module.Category.Fun);
    }

    @EventTarget
    public void onTick(TickEvent e){

    }
}
