package xianhhh.Event.Events;

import org.lwjgl.glfw.GLFW;
import xianhhh.Event.Event;

import java.awt.event.KeyEvent;

public class KeyEventS extends Event {
    private long window;
    private int key;
    private int scancode;
    private int modifiers;
    private int action;



    public KeyEventS(long window, int key, int scancode, int action, int modifiers) {//Post in KeyboardHandler
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.modifiers = modifiers;
        this.action = action;
        System.out.println(key + " " + GLFW.glfwGetKeyName(key,scancode));
    }
    public long getWindow() {
        return window;
    }

    public int getScancode() {
        return scancode;
    }

    public int getModifiers() {
        return modifiers;
    }

    public int getAction() {
        return action;
    }

    public int getKey() {
        return key;
    }
}
