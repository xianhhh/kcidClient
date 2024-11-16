package net.minecraft.client.multiplayer;

import net.minecraft.Util;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.util.SampleLogger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PingDebugMonitor {
   private final ClientPacketListener connection;
   private final SampleLogger delayTimer;

   public PingDebugMonitor(ClientPacketListener p_300283_, SampleLogger p_300149_) {
      this.connection = p_300283_;
      this.delayTimer = p_300149_;
   }

   public void tick() {
      this.connection.send(new ServerboundPingRequestPacket(Util.getMillis()));
   }

   public void onPongReceived(ClientboundPongResponsePacket p_297641_) {
      this.delayTimer.logSample(Util.getMillis() - p_297641_.getTime());
   }
}