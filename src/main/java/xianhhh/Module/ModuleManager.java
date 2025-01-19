package xianhhh.Module;

import net.minecraft.client.Minecraft;
import xianhhh.Module.Fun.FreeCamera;
import xianhhh.Module.Fun.Sound;
import xianhhh.Module.Move.Sprint;
import xianhhh.Module.Player.PlayerTest;
import xianhhh.Module.Render.*;

import java.util.Locale;

public class ModuleManager {
    protected static final Minecraft mc = Minecraft.getInstance();
    public static java.util.ArrayList<Module> temp = new java.util.ArrayList<Module>();
    public boolean reload;
    public static java.util.ArrayList<Module> modulesA;

    public static java.util.ArrayList<String> SmodulesA;

    public static java.util.ArrayList<String> EnableModules;

    public static java.util.ArrayList<Module> EnableModulesM;

    public ModuleManager() {
        modulesA = new java.util.ArrayList<Module>();
        SmodulesA = new java.util.ArrayList<String>();
        EnableModules = new java.util.ArrayList<String>();
        EnableModulesM = new java.util.ArrayList<Module>();
    }

    private void addModule(Module module) {
        modulesA.add(module);
        SmodulesA.add(module.getName());
    }

    public static java.util.ArrayList<Module> getModuleFromCategory(Category ct){
        java.util.ArrayList<Module> result = new java.util.ArrayList<Module>();
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
       addModule(new ArrayList());
       addModule(new ClickGuiZ());
       addModule(new FreeCamera());
       addModule(new WebClickGui());
       addModule(new TargetInformation());
       //addModule(new TabGui());
    }
}
