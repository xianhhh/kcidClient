package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class RealmsServerList implements Iterable<RealmsServer> {
   private final Minecraft minecraft;
   private final Set<RealmsServer> removedServers = new HashSet<>();
   private List<RealmsServer> servers = List.of();

   public RealmsServerList(Minecraft p_239233_) {
      this.minecraft = p_239233_;
   }

   public void updateServersList(List<RealmsServer> p_239869_) {
      List<RealmsServer> list = new ArrayList<>(p_239869_);
      list.sort(new RealmsServer.McoServerComparator(this.minecraft.getUser().getName()));
      boolean flag = list.removeAll(this.removedServers);
      if (!flag) {
         this.removedServers.clear();
      }

      this.servers = list;
   }

   public void removeItem(RealmsServer p_240077_) {
      this.servers.remove(p_240077_);
      this.removedServers.add(p_240077_);
   }

   public Iterator<RealmsServer> iterator() {
      return this.servers.iterator();
   }

   public boolean isEmpty() {
      return this.servers.isEmpty();
   }
}