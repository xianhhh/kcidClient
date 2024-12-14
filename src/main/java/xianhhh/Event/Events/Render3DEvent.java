package xianhhh.Event.Events;

import xianhhh.Event.Event;

public class Render3DEvent extends Event {
    private float partialTick;
    private long nanos;
    private boolean flag;

    public float getPartialTick() {
        return partialTick;
    }

    public long getNanos() {
        return nanos;
    }

    public boolean isFlag() {
        return flag;
    }

    public Render3DEvent(float partialTick, long nanos, boolean flag) {
        super("Render3DEvent");
        this.partialTick = partialTick;
        this.nanos = nanos;
        this.flag = flag;
    }
}
