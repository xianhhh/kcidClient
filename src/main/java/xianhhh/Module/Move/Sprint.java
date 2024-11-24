package xianhhh.Module.Move;

import com.google.common.eventbus.Subscribe;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.TickEvent;
import xianhhh.Module.Module;
import xianhhh.Setting.Settings.BoolSetting;

import java.awt.event.KeyEvent;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", KeyEvent.VK_R, xianhhh.Module.Category.Move);
    }

    public BoolSetting bool = new BoolSetting(true,"Sprint",this);

    @EventTarget
    public void onTick(TickEvent e){
        e.cancel();
        mc.player.setSprinting(bool.getValue());
    }
}
