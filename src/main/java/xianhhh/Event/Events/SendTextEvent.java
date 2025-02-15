package xianhhh.Event.Events;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import xianhhh.Event.Event;

public class SendTextEvent extends Event {
    private final PlayerChatMessage playerChatMessage;
    private final ChatType.Bound chatType;
    private final GameProfile gameProfile;

    public SendTextEvent(PlayerChatMessage playerChatMessage, ChatType.Bound chatType, GameProfile gameProfile) {
        this.playerChatMessage = playerChatMessage;
        this.chatType = chatType;
        this.gameProfile = gameProfile;
    }

    public PlayerChatMessage getPlayerChatMessage() {
        return playerChatMessage;
    }

    public ChatType.Bound getChatType() {
        return chatType;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public String getText(){
        return playerChatMessage.signedContent();
    }
}
