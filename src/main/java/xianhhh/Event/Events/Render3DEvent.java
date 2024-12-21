package xianhhh.Event.Events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import xianhhh.Event.Event;

public class Render3DEvent extends Event {
    private final LevelRenderer levelRenderer;
    private final PoseStack poseStack;
    private final float partialTick;
    private final Matrix4f projectionMatrix;
    private final long startNanos;

    public Render3DEvent(LevelRenderer levelRenderer, PoseStack poseStack, float partialTick, Matrix4f projectionMatrix, long startNanos) {
        super("Render3DEvent");
        this.levelRenderer = levelRenderer;
        this.poseStack = poseStack;
        this.partialTick = partialTick;
        this.projectionMatrix = projectionMatrix;
        this.startNanos = startNanos;
    }

    public LevelRenderer getLevelRenderer() {
        return levelRenderer;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public long getStartNanos() {
        return startNanos;
    }
}
