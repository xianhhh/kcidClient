package xianhhh.Module.Player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.world.entity.Entity;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.Events.TickEvent;
import xianhhh.Module.Module;
import xianhhh.Utils.WorldUtils;

import java.awt.event.KeyEvent;

public class PlayerTest extends Module {
    public PlayerTest() {
        super("PlayerTest", KeyEvent.VK_V, xianhhh.Module.Category.Player);
    }

    @EventTarget
    public void onUpdate(TickEvent ev) {
        /*
        Random r = new Random();
        int a = r.nextInt(180);
        mc.player.setYHeadRot(a);
        mc.player.xRotO = a;
        mc.player.setYBodyRot(-a);
        mc.player.xRotO = -a;
        */



        //mc.player.swing(mc.player.getUsedItemHand());
        for(Entity e : world.getEntitiesI()) {
            if(world.PlayergetDistancetoEntity(e) < 5){

                //mc.player.attack(e);
            }
            WorldUtils.rotationTo(mc.player,e);
            mc.player.skipAttackInteraction(e);
        }
    }
}
