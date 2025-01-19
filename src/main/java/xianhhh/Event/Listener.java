package xianhhh.Event;

import java.util.EventListener;

public interface Listener extends EventListener {
    void setCancelled(boolean cancel);
    void cancel();
    boolean isCancelled();
}
