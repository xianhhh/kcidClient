package xianhhh.Module.Move;

import com.google.common.eventbus.Subscribe;
import net.minecraft.server.dedicated.Settings;
import xianhhh.Event.Events.TickEvent;
import xianhhh.Module.Module;
import xianhhh.Setting.Settings.BoolSetting;

import java.awt.event.KeyEvent;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", KeyEvent.VK_R, xianhhh.Module.Category.Move);
    }

    public BoolSetting bool = new BoolSetting(true,"Sprint",this);

    @Subscribe
    public void onTick(TickEvent e){
        mc.player.setSprinting(bool.getValue());
    }
}
