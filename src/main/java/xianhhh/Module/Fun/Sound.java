package xianhhh.Module.Fun;

import xianhhh.Module.Module;
import xianhhh.Utils.SoundUtils;

import java.awt.event.KeyEvent;

public class Sound extends Module {

    public Sound() {
        super("Sound", KeyEvent.VK_Y, xianhhh.Module.Category.Fun);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        SoundUtils.playSound();
    }
}
