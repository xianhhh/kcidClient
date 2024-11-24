package xianhhh.Client.Gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xianhhh.Utils.RatioUtil;
import xianhhh.Utils.RenderUtils;
import xianhhh.Utils.SizeUtil;


public class MainMenuScreen extends Screen {
    public MainMenuScreen() {
        super(Component.translatable("narrator.screen.title"));
    }
    int x = 0,y = 0;
    ResourceLocation backgroundRL = new ResourceLocation("pc/gui/background.png");
    ResourceLocation lenaRL = new ResourceLocation("pc/gui/title_mako.png");
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        int width = p_281549_.guiWidth(),height = p_281549_.guiHeight();
        RatioUtil lenaRatio = new RatioUtil(new SizeUtil(1920,1080),new SizeUtil(width,height));
        //1118,694

        RenderUtils.fastblit(p_281549_,backgroundRL,0,0,width,height);
        RenderUtils.fastblit(p_281549_,lenaRL,x,y, lenaRatio.getWidthRatio() * 1118,lenaRatio.getHeightRatio() * 694);
        System.out.println(lenaRatio.getWidthRatio() * 1118);
        System.out.println(lenaRatio.getHeightRatio() * 694);
    }

    public void button(GuiGraphics g) {

    }
}
