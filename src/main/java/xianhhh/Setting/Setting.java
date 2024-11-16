package xianhhh.Setting;

import net.minecraft.client.gui.GuiGraphics;
import xianhhh.Module.Module;

public abstract class Setting extends SettingBase{
    private final String name;
    private final String text;

    public Setting(String Text, Module parent, String settingName){
        super(parent);
        this.name = settingName;
        this.text = Text;
        addSet(parent,this);
        structure.add(this);
    }

    public String getName(){
        return name;
    }

    public String getText() {
        return text;
    }

    public abstract <T> void setValue(T value);

    public abstract <V,T extends V> T getValue();

    public abstract void render(int x,int x2, int y,int y2,GuiGraphics g);

    public abstract void render(int topLeft,int bottomRight,GuiGraphics g);
}
