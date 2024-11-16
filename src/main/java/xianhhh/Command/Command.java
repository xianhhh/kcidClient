package xianhhh.Command;

public abstract class Command {
    private String trigger;
    public Command(String name){
        this.trigger = name;
    }

    public String getName() {
        return trigger;
    };

    public abstract boolean execute(String[] args);

    public abstract String error();

}
