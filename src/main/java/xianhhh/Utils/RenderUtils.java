package xianhhh.Utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
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

    public static void renderFaceBox(PoseStack poseStack, AABB aabb, Color color) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();

        // 获取AABB的边界
        float x1 = (float) aabb.minX;
        float y1 = (float) aabb.minY;
        float z1 = (float) aabb.minZ;

        float x2 = (float) aabb.maxX;
        float y2 = (float) aabb.maxY;
        float z2 = (float) aabb.maxZ;

        float r = color.getRed()/255f;
        float g = color.getGreen()/255f;
        float b = color.getBlue()/255f;
        float a = color.getAlpha()/255f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // 开始绘制面
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();

        bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);


        // 正面
        bufferBuilder.vertex(matrix4f, x1, y1, z2).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y1, z2).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y2, z2).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

        // 背面
        bufferBuilder.vertex(matrix4f, x2, y1, z1).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, z1).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y2, z1).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();

        // 左面
        bufferBuilder.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y1, z2).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y2, z1).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y2, z2).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();

        // 右面
        bufferBuilder.vertex(matrix4f, x2, y1, z2).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y1, z1).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, z1).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();

        // 顶面
        bufferBuilder.vertex(matrix4f, x1, y2, z2).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y2, z1).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, z1).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();

        // 底面
        bufferBuilder.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y1, z1).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y1, z2).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y1, z2).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

        tesselator.end();

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
