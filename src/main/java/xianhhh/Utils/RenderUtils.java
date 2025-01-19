package xianhhh.Utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

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

    public static void fastfill(int topLeft, int bottomRight, int changeY, GuiGraphics g, Color c){
        g.fill(topLeft,topLeft,bottomRight,Math.abs(bottomRight - changeY),c.getRGB());
    }

    public static void renderhitbox(PoseStack poseStack, AABB aabb) {
        LevelRenderer.renderLineBox(poseStack, ((MultiBufferSource)Minecraft.getInstance().renderBuffers().outlineBufferSource()).getBuffer(RenderType.lines()), aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, 1,1,1,1);
    }

    public static void renderRoundFastMiddle(PoseStack pose,double x,double y,double width,double height,double radius,double samples,Color c){
        renderRound(pose,c.getRGB(),x-width/2,y,x+width/2,y+height,radius,samples);
    }

    public static void renderRoundFast(PoseStack pose,double x,double y,double width,double height,double radius,double samples,Color c){

        renderRound(pose, c.getRGB(),x,y,x+width,y+height,radius,samples);
    }

    public static void renderRound(PoseStack pose,double x,double y,double x2,double y2,double radius,double samples,Color c){

        renderRound(pose,c.getRGB(),x,y,x2,y2,radius,samples);
    }


    public static void renderRound(PoseStack pose,int rgb, double fromX, double fromY, double toX, double toY, double radius, double samples){
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();

        Matrix4f matrix = pose.last().pose();

        float ca = (float)FastColor.ARGB32.alpha(rgb) / 255.0F;
        float cr = (float)FastColor.ARGB32.red(rgb) / 255.0F;
        float cg = (float)FastColor.ARGB32.green(rgb) / 255.0F;
        float cb = (float)FastColor.ARGB32.blue(rgb) / 255.0F;

        RenderSystem.enableBlend();  // 开启混合
        RenderSystem.defaultBlendFunc();  // 使用默认混合函数
        RenderSystem.disableDepthTest();  // 禁用深度测试，确保透明物体正确显示

        bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        // 绘制圆角矩形的四个角
        double[][] map = new double[][]{
                {toX - radius, toY - radius, radius},
                {toX - radius, fromY + radius, radius},
                {fromX + radius, fromY + radius, radius},
                {fromX + radius, toY - radius, radius}
        };

        // 遍历四个角，分别绘制圆弧
        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            double rad = current[2];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F)
                        .color(cr, cg, cb, ca)  // 使用传入的透明度
                        .endVertex();
            }
            float rad1 = (float) Math.toRadians((360 / 4d + i * 90d));
            float sin = (float) (Math.sin(rad1) * rad);
            float cos = (float) (Math.cos(rad1) * rad);
            bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F)
                    .color(cr, cg, cb, ca)  // 使用透明颜色
                    .endVertex();
        }

        RenderSystem.enableDepthTest();  // 恢复深度测试

        tesselator.end();  // 结束渲染
    }
}
