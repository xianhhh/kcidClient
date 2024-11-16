package net.minecraft.network;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.SampleLogger;

public class BandwidthDebugMonitor {
   private final AtomicInteger bytesReceived = new AtomicInteger();
   private final SampleLogger bandwidthLogger;

   public BandwidthDebugMonitor(SampleLogger p_300882_) {
      this.bandwidthLogger = p_300882_;
   }

   public void onReceive(int p_300834_) {
      this.bytesReceived.getAndAdd(p_300834_);
   }

   public void tick() {
      this.bandwidthLogger.logSample((long)this.bytesReceived.getAndSet(0));
   }
}