package xianhhh.Module;

import net.minecraft.client.Minecraft;
import xianhhh.Client.Client;
import xianhhh.Event.EventHandleT;
import xianhhh.Setting.SettingBase;
import xianhhh.Utils.RenderUtils;
import xianhhh.Utils.WorldUtils;

import java.util.ArrayList;

public class Module {
    protected static final Minecraft mc = Minecraft.getInstance();
    protected static final RenderUtils render = new RenderUtils();
    protected static final WorldUtils world = new WorldUtils();


    public int Key;
    public String Name;
    public Category Category;
    public boolean toggle;
    public boolean register;
    public ArrayList<?> settings = new ArrayList<>();

    public int getKey() {
        return Key;
    }

    public void setKey(int key) {
        this.Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public xianhhh.Module.Category getCategory() {
        return Category;
    }

    public void setCategory(xianhhh.Module.Category category) {
        Category = category;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void Toggle() {
        this.toggle = !this.toggle;
        if(toggle){
            onEnable();
        }else{
            onDisable();
        }
    }

    public Module(String name,int key, xianhhh.Module.Category category) {
        Key = key;
        Name = name;
        Category = category;
        this.toggle = false;
        this.register = false;
    }

    public boolean isRegister() {
        return register;
    }



    public void onEnable() {
        Client.eventHandle.supe(this, EventHandleT.Mode.REGISTER);
        System.out.println(this.getName() + " Enable ");
        System.out.print(SettingBase.getSettingsName(this));
        ModuleManager.EnableModules.add(this.getName());
        ModuleManager.EnableModulesM.add(this);
    }

    public void onDisable() {
        Client.eventHandle.supe(this, EventHandleT.Mode.UNREGISTER);
        System.out.println(this.getName() + " Disable ");
        ModuleManager.EnableModules.remove(this.getName());
        ModuleManager.EnableModulesM.remove(this);
    }

    public ArrayList<?> getSettings(){
        return settings;
    }
}
