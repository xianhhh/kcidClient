package xianhhh.Module.Render;

import xianhhh.Client.ClickGui.ClickGui;
import xianhhh.Module.Module;

import java.awt.event.KeyEvent;

public class ClickGuiZ extends Module {
    public ClickGuiZ() {
        super("ClickGui", KeyEvent.VK_X, xianhhh.Module.Category.Render);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.setScreen(new ClickGui());
    }
}
