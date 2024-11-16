package net.minecraft.network.protocol.common.custom;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public record BeeDebugPayload(BeeDebugPayload.BeeInfo beeInfo) implements CustomPacketPayload {
   public static final ResourceLocation ID = new ResourceLocation("debug/bee");

   public BeeDebugPayload(FriendlyByteBuf p_297658_) {
      this(new BeeDebugPayload.BeeInfo(p_297658_));
   }

   public void write(FriendlyByteBuf p_300191_) {
      this.beeInfo.write(p_300191_);
   }

   public ResourceLocation id() {
      return ID;
   }

   public static record BeeInfo(UUID uuid, int id, Vec3 pos, @Nullable Path path, @Nullable BlockPos hivePos, @Nullable BlockPos flowerPos, int travelTicks, Set<String> goals, List<BlockPos> blacklistedHives) {
      public BeeInfo(FriendlyByteBuf p_299863_) {
         this(p_299863_.readUUID(), p_299863_.readInt(), p_299863_.readVec3(), p_299863_.readNullable(Path::createFromStream), p_299863_.readNullable(FriendlyByteBuf::readBlockPos), p_299863_.readNullable(FriendlyByteBuf::readBlockPos), p_299863_.readInt(), p_299863_.readCollection(HashSet::new, FriendlyByteBuf::readUtf), p_299863_.readList(FriendlyByteBuf::readBlockPos));
      }

      public void write(FriendlyByteBuf p_299671_) {
         p_299671_.writeUUID(this.uuid);
         p_299671_.writeInt(this.id);
         p_299671_.writeVec3(this.pos);
         p_299671_.writeNullable(this.path, (p_297580_, p_297572_) -> {
            p_297572_.writeToStream(p_297580_);
         });
         p_299671_.writeNullable(this.hivePos, FriendlyByteBuf::writeBlockPos);
         p_299671_.writeNullable(this.flowerPos, FriendlyByteBuf::writeBlockPos);
         p_299671_.writeInt(this.travelTicks);
         p_299671_.writeCollection(this.goals, FriendlyByteBuf::writeUtf);
         p_299671_.writeCollection(this.blacklistedHives, FriendlyByteBuf::writeBlockPos);
      }

      public boolean hasHive(BlockPos p_300739_) {
         return Objects.equals(p_300739_, this.hivePos);
      }

      public String generateName() {
         return DebugEntityNameGenerator.getEntityName(this.uuid);
      }

      public String toString() {
         return this.generateName();
      }
   }
}