package xianhhh.Module.Fun;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.world.level.GameType;
import xianhhh.Event.Events.TickEvent;
import xianhhh.Module.Category;
import xianhhh.Module.Module;
import xianhhh.Utils.PlayerUtils;

import java.awt.event.KeyEvent;

public class FreeCamera extends Module {
    public FreeCamera() {
        super("FreeCamera", KeyEvent.VK_F, xianhhh.Module.Category.Fun);
    }

    @Subscribe
    public void onTick(TickEvent e){

    }
}
