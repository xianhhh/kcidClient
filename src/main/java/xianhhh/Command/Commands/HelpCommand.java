package xianhhh.Command.Commands;

import xianhhh.Client.Client;
import xianhhh.Command.Command;
import xianhhh.Utils.PlayerUtils;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(String[] args) {
        for(Command command : Client.commandManager.commands){
            int size = command.error().split("['|]").length - 1;
            PlayerUtils.sendMessage(command.getName() + ":" + command.error().split("['|']")[size]);
        }
        return true;
    }

    @Override
    public String error() {
        return ".help";
    }
}
