package xianhhh.Event;


public interface Cancellable {
    void setCancelled(boolean cancel);
    void cancel();
    boolean isCancelled();
}
