package xianhhh.Setting.Settings;


import net.minecraft.client.gui.GuiGraphics;
import xianhhh.Module.Module;
import xianhhh.Setting.Setting;

public class BoolSetting extends Setting{
    private boolean value;
    public BoolSetting(boolean value,String text, Module parent) {
        super(text,parent,"BoolSetting");
        this.value = value;
    }

    @Override
    public <V, T extends V> T getValue() {
        return (T) (Object)value;
    }

    @Override
    public void render(int x, int x2, int y, int y2, GuiGraphics g) {

    }

    @Override
    public void render(int topLeft, int bottomRight, GuiGraphics g) {

    }


    @Override
    public <T> void setValue(T value) {
        this.value = (boolean)(Object)value;
    }


}
