package xianhhh.Setting;

import net.minecraft.client.gui.GuiGraphics;
import xianhhh.Module.Module;
import xianhhh.Utils.Structure;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingBase {
    protected static final HashMap<Module, ArrayList<Setting>> settings = new HashMap<>();
    protected static final Structure<Setting> structure = new Structure();
    public SettingBase(Module parent){
        settings.put(parent,new ArrayList<>());
    }

    public static void addSet(Module parent, Setting setting){
       ArrayList<Setting> z = getSettings(parent);
       z.add(setting);
       settings.put(parent, z);
    }
    public static ArrayList<Setting> getSettings(Module parent) {
        return settings.get(parent);
    }

    public static ArrayList<String> getSettingsName(Module parent){
        ArrayList<String> z = new ArrayList<>();
        if(settings.get(parent) != null){
            for(Setting v : settings.get(parent)){
                z.add(v.getName());
            }
        }
        return z;
    }

    public static Structure<Setting> getStructure(){
        return structure;
    }

    public static void renderStructure(int x, int length, int y, GuiGraphics graphics){
        if(structure.getT() instanceof Setting){
            Setting s;

            for(Setting v : structure.elements()){
                s = v;
                v.render(x,x + length - y,graphics);
            }
        }
    }

}
