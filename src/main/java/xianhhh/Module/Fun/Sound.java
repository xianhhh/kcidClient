package xianhhh.Module.Fun;

import org.lwjgl.glfw.GLFW;
import xianhhh.Module.Module;
import xianhhh.Utils.SoundUtils;

import java.awt.event.KeyEvent;

public class Sound extends Module {

    public Sound() {
        super("Sound", GLFW.GLFW_KEY_Y, xianhhh.Module.Category.Fun);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        SoundUtils.playSound();
    }
}
