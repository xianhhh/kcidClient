package xianhhh.Event.Events;

import xianhhh.Event.Event;

public class ModuleEvent extends Event {
    private boolean istoggle;

    public ModuleEvent() {
        super("ModuleEvent");
    }

    public void setToggle(boolean is){
        this.istoggle = is;
    }

    public boolean isIstoggle() {
        return istoggle;
    }
}
