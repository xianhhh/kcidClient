package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import xianhhh.Module.Category;
import xianhhh.Module.Module;
import xianhhh.WebUi.WebUiBase;

import java.awt.event.KeyEvent;


public class WebClickGui extends Module {
    public WebClickGui() {
        super("WebClickGui", KeyEvent.VK_P, xianhhh.Module.Category.Render);
    }

    @Subscribe
    public void onEnable(){
        super.onEnable();
        WebUiBase.start();
    }

    @Subscribe
    public void onDisable(){
        super.onDisable();
        WebUiBase.stop();
    }
}
