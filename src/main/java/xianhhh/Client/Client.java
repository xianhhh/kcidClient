package xianhhh.Client;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import xianhhh.Client.ClickGui.ClickGui;
import xianhhh.Client.Gui.CialloGameMainScreen;
import xianhhh.Client.Gui.NormalMainGuiScreen;
import xianhhh.Client.Gui.StartScreen;
import xianhhh.Command.CommandManager;
import xianhhh.Event.EventHandleT;
import xianhhh.ModLoader.ModLoader;
import xianhhh.Module.ModuleManager;

public class Client {

    public Client() {

    }

    protected static final Minecraft mc = Minecraft.getInstance();

    public static ModuleManager modManager;

    public static EventHandleT eventHandle;

    public static CommandManager commandManager;

    public static final String NAME = "D1CK-Client";

    public static final Screen startScreen = new StartScreen();
    public static final Screen gameMainScreen = new NormalMainGuiScreen();

    private static Screen set(String name){
        switch (name) {
            case "ciallo":return new CialloGameMainScreen();
        }
        return null;
    }

    public static void Start() {
        eventHandle = new EventHandleT();
        modManager = new ModuleManager();
        commandManager = new CommandManager();
        modManager.register();
        commandManager.register();
        ModLoader.registerSelf();
        mc.getWindow().setTitle(NAME + " " + "Type: " + mc.getVersionType() + " " + SharedConstants.getCurrentVersion().getName());
        objects();
    }

    public static void updateTitle(String and){
       mc.getWindow().setTitle(NAME + " " + "Type: " + mc.getVersionType() + and);
    }


    private static void objects(){
        try {
            Thread screen = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (mc.screen != null) {
                                if(mc.screen instanceof TitleScreen) {

                                }else if(!(mc.screen instanceof StartScreen)){
                                    //System.out.println(mc.screen.toString());
                                }
                            }
                            if(!(Minecraft.getInstance().screen instanceof ClickGui) && ModuleManager.getModule("clickgui").isToggle()){
                                    ModuleManager.getModule("clickgui").Toggle();
                            }
                            Thread.sleep(1500);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
            screen.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
