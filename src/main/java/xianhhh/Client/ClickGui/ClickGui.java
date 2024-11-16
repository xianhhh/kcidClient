package xianhhh.Client.ClickGui;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xianhhh.Module.Category;
import xianhhh.Module.Module;
import xianhhh.Module.ModuleManager;
import xianhhh.Module.Move.Sprint;
import xianhhh.Setting.Setting;
import xianhhh.Setting.SettingBase;
import xianhhh.Utils.RenderUtils;
import xianhhh.Utils.Structure;

import java.awt.*;
import java.util.ArrayList;

public class ClickGui extends Screen {

    private final ResourceLocation background = new ResourceLocation("pc/clickgui/guibackground.png");

    private Category ct = null;

    private final ArrayList<Integer> moveRange = new ArrayList<Integer>();
    private final  ArrayList<Integer> renderRange = new ArrayList<Integer>();
    private final  ArrayList<Integer> playerRange = new ArrayList<Integer>();
    private final  ArrayList<Integer> funRange = new ArrayList<Integer>();

    private final  ArrayList<Integer> widthRange = new ArrayList<Integer>();

    public ClickGui() {
        super(Component.nullToEmpty(null));
    }

    public void render(GuiGraphics g, int p_281550_, int p_282878_, float p_282465_) {
        super.render(g, p_281550_, p_282878_, p_282465_);
        if (ct != null) {
            renderModule(ct, g);
        }
        renderSetting(new Sprint(), g);
    }

    public void renderBackground(GuiGraphics g, int p_299421_, int p_298679_, float p_297268_) {
        RenderUtils.fastblit(g, background, 0, 0, this.width / 2, this.height / 3 * 2, this.width / 2, this.height / 3 * 2);
        renderBox(g);
    }

    public void renderBox(GuiGraphics g) {
        renderLine(g);
        int w = (this.width / 2) / 4;
        int y = 6;
        int ad = this.height / 3 * 2 / 4 - 10;
        int x = this.width / 50;
        Color c = new Color(0, 0, 0, 40);

        this.widthRange.add(0, x);
        this.widthRange.add(1, x + w);


        for (int i = 0; i < 4; i++) {
            g.fill(x // x
                    , y + 10 * i // y
                    , x + w // x2
                    , y + 10 * i + ad // y2 //end y length = (y + 10 * i + ad) - (y + 10 * i)
                    , c.getRGB()// color
            );
            g.drawCenteredString(this.font, Category.values()[i].name(), x + w / 2, (y + 10 * i + ad / 2), Color.WHITE.getRGB());
            //System.out.println("begin Y =" + y + " end Y = " + y + 10 * i + " index = " + i);
            switch (i) {
                case 0:
                    this.moveRange.add(0, y + 10 * i);
                    this.moveRange.add(1, y + 10 * i + ad);
                    break;
                case 1:
                    this.renderRange.add(0, y + 10 * i);
                    this.renderRange.add(1, y + 10 * i + ad);
                    break;
                case 2:
                    this.playerRange.add(0, y + 10 * i);
                    this.playerRange.add(1, y + 10 * i + ad);
                    break;
                case 3:
                    this.funRange.add(0, y + 10 * i);
                    this.funRange.add(1, y + 10 * i + ad);
                    break;
            }
            y += ad;
            //System.out.println("begin Y =" + y + " end Y = " + y + 10 * i + " index = " + i);
        }
    }


    public void renderLine(GuiGraphics g){
        int x = this.width / 13 * 2;
        //g.fill(x,4,x2,this.height/3*2 - 2,new Color(0, 0, 0, 40).getRGB());
        RenderUtils.fastfill(g,x,4,2,this.height/3*2 - 6,new Color(0, 0, 0, 40));
    }

    public void renderModule(Category category,GuiGraphics g){
       renderModuleOne(category,g);
    }

    private void renderSetting(Module m,GuiGraphics g){

        ArrayList<Setting> settings = SettingBase.getSettings(m);
        int y = 0;
        int y2 = this.height;

        int x = this.width / 4 * 3;
        int x2 = this.width;
        g.fill(x,y,x2,y2,new Color(0,0,0,40).getRGB());
        for(Setting s : settings){
            s.render(x,x2,y,y2,g);
        }
    }


    private void renderModuleOne(Category category, GuiGraphics g) {
        int X = this.width / 7;
        int y = 6;
        int ad = this.height / 3 * 2 / 5 - this.height/100;
        Color c = new Color(0, 0, 0, 40);
        ArrayList<Module> modules = ModuleManager.getModuleFromCategory(category);
        int i = 0;


        for(Module m : modules){
           if(i % 2 != 0 && modules.get(i) != null){
              int a = (X + this.width / 80) + this.width / 5;
              int b = a + (this.width / 5 + X - this.width / 20);
               g.drawCenteredString(this.font,m.getName(),(a - a/2) * 2 + a/2 - a/ 4 - a/20 ,(y + this.height/100 * i + ad / 2),Color.WHITE.getRGB());
               g.fill(a,
                       y + this.height/100 * i,
                       b,
                       y + this.height/100 * i,
                       c.getRGB()
               );
               y += ad;
           }
           if(modules.get(i) != null && i % 2 == 0){
               g.drawCenteredString(this.font,m.getName(),X + X/2,(y + this.height/100 * i + ad / 2),Color.WHITE.getRGB());
               g.fill(X + this.width / 80, // x
                       y + this.height/100 * i,// y
                       this.width / 5 + X - this.width / 20 ,//x2
                       y + this.height/100 * i + ad,
                       c.getRGB()
               );
           }
            i++;

        }
    }

    private void categoryClick(int x,int y){
        if (x > widthRange.get(0) && x < widthRange.get(1)) {
            if (y > moveRange.get(0) && y < moveRange.get(1)) {
                this.ct = Category.Move;
            }
            if (y > renderRange.get(0) && y < renderRange.get(1)) {
                this.ct = Category.Render;
            }
            if (y > playerRange.get(0) && y < playerRange.get(1)) {
                this.ct = Category.Player;
            }
            if (y > funRange.get(0) && y < funRange.get(1)) {
                this.ct = Category.Fun;
            }
        }
    }


    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        System.out.println(p_94697_);
        int x = (int) p_94695_;
        int y = (int) p_94696_;

        int cp = p_94697_;
        if (cp == 0) {
           categoryClick(x,y);
        }

        if (ct != null) {
            System.out.println(ct.name());
        }
        return super.mouseClicked(p_94695_, p_94696_, p_94697_);
    }

    public void init() {
        Structure<Setting> st = SettingBase.getStructure();

        if (this.minecraft != null) {
            this.minecraft.mouseHandler.releaseMouse();
        }
        KeyMapping.releaseAll();
    }
}
