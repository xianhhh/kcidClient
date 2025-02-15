package xianhhh.Module.Move;

import org.lwjgl.glfw.GLFW;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.TickEvent;
import xianhhh.Module.Module;
import xianhhh.Setting.Settings.BoolSetting;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", GLFW.GLFW_KEY_R, xianhhh.Module.Category.Move);
    }

    public BoolSetting bool = new BoolSetting(true,"Sprint",this);

    @EventTarget
    public void onTick(TickEvent e){
        mc.player.setSprinting(bool.getValue());
    }
}
