package xianhhh.Event.Events;

import xianhhh.Event.Event;

import java.awt.event.KeyEvent;

public class KeyEventS extends Event {
    private static int key;
    public KeyEventS(int key) {//Post in KeyboardHandler
        super("KeyEvent");
        KeyEventS.key = key;
        System.out.println(key + " " + KeyEvent.getKeyText(key));
    }

    public static int getKey() {
        return key;
    }
}
