package xianhhh.Command.Commands;

import xianhhh.Command.Command;
import xianhhh.Module.Module;
import xianhhh.Module.ModuleManager;

import java.awt.event.KeyEvent;

public class BindModuleCommand extends Command {
    public BindModuleCommand() {
        super("bind");
    }

    @Override
    public boolean execute(String[] args) {
        if(args.length == 3) {
            if (args[1] != null && args[2] != null) {
                Module module = ModuleManager.getModule(args[1]);
                int key = KeyEvent.getExtendedKeyCodeForChar(args[2].chars().sum());
                System.out.println("Bind key: " + key);
                if (module != null) {
                    module.setKey(key);
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    @Override
    public String error() {
        return "Illegal Module Or Key | .bind [module] <key>";
    }
}
