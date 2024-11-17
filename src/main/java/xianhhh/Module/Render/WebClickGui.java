package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import xianhhh.Module.Category;
import xianhhh.Module.Module;
import xianhhh.WebUi.WebUi;

import java.awt.event.KeyEvent;

/**
 * @author Kalud
 * @website pixelskider.github.io/
 * @since 2024/11/17
 */
public class WebClickGui extends Module {
    public WebClickGui() {
        super("WebClickGui", KeyEvent.VK_P, xianhhh.Module.Category.Render);
    }

    @Subscribe
    public void onEnable(){
        super.onEnable();
        WebUi.start();
    }

    @Subscribe
    public void onDisable(){
        super.onDisable();
        WebUi.stop();
    }
}
