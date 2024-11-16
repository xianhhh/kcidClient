package xianhhh.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;

public class RenderUtils {
    public static GuiGraphics gui = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());

    public static Float getStringWidth(String s) {
        Integer width = s.length();
        float z = width.floatValue() * 3.75f;
        return z + 1f;
    }

    public static void drawList(GuiGraphics g,ArrayList<String> arrayList,Color stringColor,Color BoxColor){
        int y = 0;
        //int width = Minecraft.getInstance().screen.width; 427
        int width = Minecraft.getInstance().getWindow().getWidth() / 2; // = screen.width * 2
        //System.out.println(width);
        Font font = Minecraft.getInstance().font;
        ArrayList<String>  z= SortUtils.LengthSort(arrayList);
        for(String s : z){
            g.fill(width - RenderUtils.getStringWidth(s).intValue(),y,width,y + 8 ,BoxColor.getRGB());
            g.drawString(font,s,width -RenderUtils.getStringWidth(s).intValue(),y, stringColor.getRGB());
            y += 8;
        }
    }

    public static boolean isClickinRange(int begin,int end,int value){
       if(value > begin && value < end){
           return true;
       }
        return false;
    }

    public static void fastblit(GuiGraphics g, ResourceLocation rs, int x, int y, int WidthRange, int heightRange, int widthR, int heightR) {
        g.blit(rs, x + 0, y + 0, 0, 0f, 0f, WidthRange + 0, heightRange + 0, widthR + 0, heightR + 0);
        //g.blit(rs,0,0,0,0f,0f,0,0,0,0);
    }

    public static void fastblit(GuiGraphics g, ResourceLocation rs, int x, int y, int width, int height) {
        fastblit(g, rs, x, y, width, height, width, height);
    }

    public static void fastfill(GuiGraphics g, int x, int y, int width, int height, Color v) {
        g.fill(x, y, x + width, y + height, v.getRGB());
    }

    public static void fastfillGradient(GuiGraphics g, int x, int y, int width, int height, Color c1, Color c2) {
        g.fillGradient(x, y, x + width, y + height, c1.getRGB(), c2.getRGB());
    }

    public static void drawString(Font f, GuiGraphics g, String s, int x, int y, Color c) {
        g.drawString(f, s, x, y, c.getRGB());
    }

    public static void drawCenterString(Font f, GuiGraphics g, String s, int x, int y, Color c) {
        g.drawCenteredString(f, s, x, y, c.getRGB());
    }

    public static void fastfill(int topLeft, int bottomRight, @Nullable int changeY, GuiGraphics g, Color c){
        g.fill(topLeft,topLeft,bottomRight,Math.abs(bottomRight - changeY),c.getRGB());
    }
}
