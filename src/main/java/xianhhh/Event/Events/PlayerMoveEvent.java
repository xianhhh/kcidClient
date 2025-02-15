package xianhhh.Event.Events;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import xianhhh.Event.Event;

public class PlayerMoveEvent extends Event {
    private final MoverType moverType;
    private final Vec3 vec3;
    public PlayerMoveEvent(MoverType moverType, Vec3 vec3) {
        this.moverType = moverType;
        this.vec3 = vec3;
    }

    public MoverType getMoverType() {
        return moverType;
    }

    public Vec3 getVec3() {
        return vec3;
    }
}
