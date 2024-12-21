package xianhhh.Module.Render;

import xianhhh.Module.Module;
import xianhhh.WebUi.WebUiBase;

import java.awt.event.KeyEvent;


public class WebClickGui extends Module {
    public WebClickGui() {
        super("WebClickGui", KeyEvent.VK_P, xianhhh.Module.Category.Render);
    }


    public void onEnable(){
        super.onEnable();
        WebUiBase.start();
    }


    public void onDisable(){
        super.onDisable();
        WebUiBase.stop();
    }
}
