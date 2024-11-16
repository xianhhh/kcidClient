package xianhhh.Module;

import net.minecraft.client.Minecraft;
import xianhhh.Module.Fun.FreeCamera;
import xianhhh.Module.Fun.Sound;
import xianhhh.Module.Move.Sprint;
import xianhhh.Module.Player.PlayerTest;
import xianhhh.Module.Render.ClickGuiZ;
import xianhhh.Module.Render.Logo;
import xianhhh.Module.Render.TargetInformation;
import xianhhh.Module.Render.Title;

import java.util.ArrayList;
import java.util.Locale;

public class ModuleManager {
    protected static final Minecraft mc = Minecraft.getInstance();
    public static ArrayList<Module> temp = new ArrayList<Module>();
    public boolean reload;
    public static ArrayList<Module> modulesA;

    public static ArrayList<String> SmodulesA;

    public static ArrayList<String> EnableModules;

    public static ArrayList<Module> EnableModulesM;

    public ModuleManager() {
        modulesA = new ArrayList<Module>();
        SmodulesA = new ArrayList<String>();
        EnableModules = new ArrayList<String>();
        EnableModulesM = new ArrayList<Module>();
    }

    private void addModule(Module module) {
        modulesA.add(module);
        SmodulesA.add(module.getName());
    }

    public static ArrayList<Module> getModuleFromCategory(Category ct){
        ArrayList<Module> result = new ArrayList<Module>();
        for(Module m : modulesA){
            if(m.getCategory() == ct){
                result.add(m);
            }
        }
        return result;
    }

    public static Module getModule(String name){
        for(Module m : modulesA){
            if(m.getName().toLowerCase(Locale.ROOT).equalsIgnoreCase(name)){
                return m;
            }
        }
        return null;
    }

    private static void adm(Module m){
        modulesA.add(m);
    }

    public static void onKeyT(Integer key){
        if(modulesA != null) {
            for (Module m : modulesA) {
                if (m.getKey() == key && Minecraft.getInstance().screen == null) {
                    m.Toggle();
                }
            }
            //System.out.print(EnableModules);
        }
    }

    public void register() {
       addModule(new Sprint());
       addModule(new Title());
       addModule(new PlayerTest());
       addModule(new Sound());
       addModule(new Logo());
       addModule(new xianhhh.Module.Render.ArrayList());
       addModule(new ClickGuiZ());
       addModule(new FreeCamera());
       addModule(new TargetInformation());
       //addModule(new TabGui());
    }
}
