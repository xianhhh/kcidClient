package xianhhh.Module.Render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
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
        //Target
        HitResult blockhit =  mc.player.pick(20d,0f,false);
        BlockPos pos = ((BlockHitResult) blockhit).getBlockPos();
        Block block = mc.level.getBlockState(pos).getBlock();
        RenderUtils.gui.drawCenteredString(mc.font,"Target Block: " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " Name: " + block.asItem(),width/2,height/2,Color.WHITE.getRGB());
        RenderUtils.gui.renderItem(block.asItem().getDefaultInstance(),width/2,height/7*4);
        //Player
        ItemStack helmet = mc.player.getInventory().getArmor(3);
        ItemStack chest = mc.player.getInventory().getArmor(2);
        ItemStack trousers = mc.player.getInventory().getArmor(1);
        ItemStack boots = mc.player.getInventory().getArmor(0);
        //ItemStack antherHand = mc.player.getInventory().getArmor(4);
        ItemStack[] items = new ItemStack[]{helmet,chest,trousers,boots};
        int add = 12;
        int y = height - 50;
        int x = width / 5;
        for(ItemStack i : items){
            if(i != null){
                RenderUtils.gui.renderItem(i,x,y);
                RenderUtils.gui.drawString(mc.font,i.getMaxDamage() - i.getDamageValue() + "/" + i.getMaxDamage(),x - (i.getMaxDamage() - i.getDamageValue() + "/" + i.getMaxDamage()).length() * 4,y,Color.WHITE.getRGB());
            }
            y += add;
        }
    }
}
