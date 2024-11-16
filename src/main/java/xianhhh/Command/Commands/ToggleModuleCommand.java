package xianhhh.Command.Commands;

import xianhhh.Command.Command;
import xianhhh.Module.Module;
import xianhhh.Module.ModuleManager;

public class ToggleModuleCommand extends Command {

    public ToggleModuleCommand() {
        super("toggle");
    }

    @Override
    public boolean execute(String[] args) {
        if(args[1] != null){
            Module module = ModuleManager.getModule(args[1]);
            if (module != null) {
                module.Toggle();
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public String error() {
        return "Illegal Module | .toggle [ModuleName]";
    }
}
