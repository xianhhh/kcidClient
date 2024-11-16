package com.mojang.realmsclient.dto;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PingResult extends ValueObject implements ReflectionBasedSerialization {
   @SerializedName("pingResults")
   public List<RegionPingResult> pingResults = Lists.newArrayList();
   @SerializedName("worldIds")
   public List<Long> worldIds = Lists.newArrayList();
}