package xianhhh.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Vector3d;

import java.util.ArrayList;


public class WorldUtils {
    protected static final Minecraft minecraft = Minecraft.getInstance();


    public static ArrayList<Entity> getEntitiesA() {
        ArrayList<Entity> list = new ArrayList<Entity>();
        for (Entity e : Minecraft.getInstance().getSingleplayerServer().overworld().getAllEntities()) {
            list.add(e);
        }
        return list;
    }

    public static Iterable<Entity> getEntitiesI() {
        return Minecraft.getInstance().getSingleplayerServer().overworld().getAllEntities();
    }

    public static double PlayergetDistancetoEntity(Entity e) {
        Player v = Minecraft.getInstance().player;
        Vector3d en = new Vector3d(e.getX(), e.getY(), e.getZ());
        Vector3d p = new Vector3d(v.getX(), v.getY(), v.getZ());
        return en.distance(p);
    }

    public static void rotationTo(LocalPlayer player, Entity target) {
        if (!(target instanceof Player)) {
            Vec3 playerV = player.position().normalize();
            Vec3 targetV = target.position().normalize();
            Vec3 Pdiff = playerV.subtract(targetV).normalize();
            Vec3 Tdiff = targetV.subtract(playerV).normalize();
            //Pitch
            float pitch = (float) Math.toDegrees(Math.asin(Tdiff.y));
            pitch = Math.clamp(-90f,90f,pitch);
            player.setYRot(pitch);
            //Yaw
            float yaw = (float) Math.toDegrees(Math.atan2(Tdiff.y, Tdiff.x) / 360 * Math.PI);
            player.setXRot(yaw);
        }
    }

    public static void addParticle(Entity entity, ParticleTypes type){
        Minecraft.getInstance().particleEngine.createTrackingEmitter(entity,(ParticleOptions) type);
    }
}
