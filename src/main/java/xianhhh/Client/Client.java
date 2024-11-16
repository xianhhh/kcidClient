package xianhhh.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import xianhhh.Client.ClickGui.ClickGui;
import xianhhh.Command.CommandManager;
import xianhhh.Event.EventHandleT;
import xianhhh.Module.ModuleManager;

public class Client {

    public Client() {

    }

    protected static final Minecraft mc = Minecraft.getInstance();

    public static ModuleManager modManager;

    public static EventHandleT eventHandle;

    public static CommandManager commandManager;

    public static final String NAME = "D1CK-Client";

    public static void Start() {
        modManager = new ModuleManager();
        eventHandle = new EventHandleT();
        commandManager = new CommandManager();
        modManager.register();
        commandManager.register();
        mc.getWindow().setTitle(NAME + " " + "Type: " + mc.getVersionType() + " 1.20.2 ");
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
                                    mc.setScreen(new CialloGameMainScreen());
                                }else if(!(mc.screen instanceof CialloGameMainScreen)){
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
