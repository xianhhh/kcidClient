package xianhhh.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
import net.minecraft.network.chat.Component;
import xianhhh.Client.Client;

import java.time.Instant;

public class PlayerUtils {
    public static void sendMessage(String message) {
        final Component mes = Component.nullToEmpty(message);
        Minecraft.getInstance().gui.getChat().addMessage(mes);

        ChatLog chatlog = Minecraft.getInstance().getReportingContext().chatLog();
        chatlog.push(LoggedChatMessage.system(mes, Instant.now()));

    }

    public static void sendMessage(String[] message) {
        String v = "";
        for(String s : message) {
            v += s + " ";
        }
        PlayerUtils.sendMessage(v);
    }
}
