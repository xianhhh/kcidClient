package xianhhh.Module.Render;

import org.lwjgl.glfw.GLFW;
import xianhhh.Client.ClickGui.ClickGui;
import xianhhh.Module.Module;

import java.awt.event.KeyEvent;

public class ClickGuiZ extends Module {
    public ClickGuiZ() {
        super("ClickGui", GLFW.GLFW_KEY_X, xianhhh.Module.Category.Render);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.setScreen(new ClickGui());
    }
}
