package xianhhh.Command;

import xianhhh.Client.Client;
import xianhhh.Command.Commands.BindModuleCommand;
import xianhhh.Command.Commands.HelpCommand;
import xianhhh.Command.Commands.ToggleModuleCommand;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.SendTextEvent;
import xianhhh.Utils.PlayerUtils;
import xianhhh.Utils.SortUtils;

import java.util.ArrayList;

public class CommandManager {
    public final ArrayList<Command> commands;

    private final String startCommandString;

    public CommandManager() {
        commands = new ArrayList<>();
        this.startCommandString = ".";
        Client.eventBus.register(this);
    }

    public void register() {
        commands.add(new ToggleModuleCommand());
        commands.add(new BindModuleCommand());
        commands.add(new HelpCommand());
    }
@EventTarget
    public void start(SendTextEvent e) {
        String message = e.getText();
        if(message.startsWith(startCommandString)){
            if(message.split(" ").length > 1){
                Command command = getCommand(message.split(" ")[0]);//
                String[] args = SortUtils.replace(message.replaceAll("\\." + command.getName(),"").split(" ")," ","");
                if(!command.execute(args)){
                    PlayerUtils.sendMessage(command.error());
                }
            }else if(!(getCommand(message) instanceof HelpCommand)){
                PlayerUtils.sendMessage("try .help");
            }else{
                 getCommand(message).execute((String[]) null);
            }
        }
    }

    public Command getCommand(String commandName){
        for(Command c : commands){
            if(c.getName().equalsIgnoreCase(commandName.replaceAll("\\.",""))){
                return c;
            }
        }
        return null;
    }
}
