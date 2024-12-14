package xianhhh.Event.Events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import xianhhh.Event.Event;

public class RenderItemEvent extends Event {
    private PoseStack poseStack;
    private Camera camera;
    private float partialTick;

    public RenderItemEvent(PoseStack poseStack, Camera camera, float partialTick) {
        super("RenderItemEvent");
        this.poseStack = poseStack;
        this.camera = camera;
        this.partialTick = partialTick;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public Camera getCamera() {
        return camera;
    }

    public float getPartialTick() {
        return partialTick;
    }
}
