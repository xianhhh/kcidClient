package xianhhh.Event.Events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import xianhhh.Event.Event;

public class EntityRenderEvent<V extends Entity> extends Event {
    private final V entity;
    private final double x;
    private final double y;
    private final double z;
    private final float yrotation;
    private final float offset;
    private final PoseStack poseStack;
    private final MultiBufferSource multiBufferSource;
    private final int packedLightCoords;


    public <E extends Entity> EntityRenderEvent(E entity, double x, double y, double z, float yrotation, float offset, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightCoords) {
        super("EntityRenderEvent");
        this.entity = (V) entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yrotation = yrotation;
        this.offset = offset;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.packedLightCoords = packedLightCoords;
    }

    public Object getEntity() {
        return entity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYrotation() {
        return yrotation;
    }

    public float getOffset() {
        return offset;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public int getPackedLightCoords() {
        return packedLightCoords;
    }
    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }
}
