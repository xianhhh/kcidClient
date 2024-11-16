package xianhhh.Event;

public class Event{
    private final String name;
    private boolean cancel;
    public Event(String name){
        this.name = name;
        this.cancel = false;
    }

    public String getName() {
        return name;
    }

    public void setCancel(boolean cancel){
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void cancel(){
        this.cancel = true;
    }
}
