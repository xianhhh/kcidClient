package com.mojang.realmsclient.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.net.Proxy;

@OnlyIn(Dist.CLIENT)
public class RealmsClientConfig {
   @Nullable
   private static Proxy proxy;

   @Nullable
   public static Proxy getProxy() {
      return proxy;
   }

   public static void setProxy(Proxy p_87294_) {
      if (proxy == null) {
         proxy = p_87294_;
      }

   }
}