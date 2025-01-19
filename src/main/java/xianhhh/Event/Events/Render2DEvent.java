package xianhhh.Event.Events;

import net.minecraft.client.gui.GuiGraphics;
import xianhhh.Event.Event;

public class Render2DEvent extends Event {
    private final GuiGraphics guiGraphics;
    private final float partialTick;
    public Render2DEvent(GuiGraphics guiGraphics, float tick){//post in gui render overlay
        this.guiGraphics = guiGraphics;
        this.partialTick = tick;
    }

    public GuiGraphics getGuiGraphics() {
        return guiGraphics;
    }

    public float getPartialTick() {
        return partialTick;
    }
}
