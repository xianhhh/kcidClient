package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import xianhhh.Event.Events.RenderEvent;
import xianhhh.Module.Module;
import xianhhh.Utils.RenderUtils;

import java.awt.*;

public class TargetInformation extends Module {
    public TargetInformation() {
        super("Target", 0, xianhhh.Module.Category.Render);
    }

    @Subscribe
    public void render(RenderEvent e){
        int width = RenderUtils.gui.guiWidth();
        int height = RenderUtils.gui.guiHeight();
        HitResult blockhit =  mc.player.pick(20d,0f,false);
        BlockPos pos = ((BlockHitResult) blockhit).getBlockPos();
        Block block = mc.level.getBlockState(pos).getBlock();
        RenderUtils.gui.drawCenteredString(mc.font,"Target Block: " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " Name: " + block.asItem(),width/2,height/2,Color.WHITE.getRGB());
    }
}
