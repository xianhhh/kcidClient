package xianhhh.Event;

public class Event implements Cancellable{
    private boolean cancel;


    public Event(){
        this.cancel = false;
    }

    public void setCancelled(boolean cancel){
        this.cancel = cancel;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void cancel(){
        this.cancel = true;
    }

}
