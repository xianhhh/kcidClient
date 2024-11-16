package xianhhh.Event.Events;

import net.minecraft.network.chat.Component;
import xianhhh.Event.Event;

public class SendTextEvent extends Event {
    private String text;

    public String getText() {
        return text;
    }

    public SendTextEvent(String text) {
        super("SendTextEvent");
        this.text = text;
    }

    public SendTextEvent() {
        super("SendTextEvent");
        this.text = "";
    }

    public SendTextEvent(Component text) {
        super("SendTextEvent");
        this.text = text.getString();
    }
}
