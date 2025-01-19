package xianhhh.Event.Events;

import net.minecraft.network.chat.Component;
import xianhhh.Event.Event;

public class SendTextEvent extends Event {
    private String text;

    public String getText() {
        return text;
    }

    public SendTextEvent(String text) {
        this.text = text;
    }

    public SendTextEvent() {
        this.text = "";
    }

    public SendTextEvent(Component text) {
        this.text = text.getString();
    }
}
