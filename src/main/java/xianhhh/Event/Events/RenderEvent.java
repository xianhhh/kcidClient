package xianhhh.Event.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import xianhhh.Event.Event;

public class RenderEvent extends Event {
    public GuiGraphics gui = new GuiGraphics(Minecraft.getInstance(),Minecraft.getInstance().renderBuffers().bufferSource());
    public RenderEvent(float tick){//post in gui render overlay
        super("RenderEvent");
    }

    public RenderEvent(){//post in gui render overlay
        super("RenderEvent");
    }

    public GuiGraphics getGui() {
        if(gui != null) {
            return gui;
        }
        return null;
    }

    public RenderEvent(GuiGraphics g){//post in gui render overlay
        super("RenderEvent");
        if(g != null) {
            this.gui = g;
        }
    }
}
