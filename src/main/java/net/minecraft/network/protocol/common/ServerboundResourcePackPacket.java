package net.minecraft.network.protocol.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundResourcePackPacket implements Packet<ServerCommonPacketListener> {
   private final ServerboundResourcePackPacket.Action action;

   public ServerboundResourcePackPacket(ServerboundResourcePackPacket.Action p_299664_) {
      this.action = p_299664_;
   }

   public ServerboundResourcePackPacket(FriendlyByteBuf p_299426_) {
      this.action = p_299426_.readEnum(ServerboundResourcePackPacket.Action.class);
   }

   public void write(FriendlyByteBuf p_298279_) {
      p_298279_.writeEnum(this.action);
   }

   public void handle(ServerCommonPacketListener p_298138_) {
      p_298138_.handleResourcePackResponse(this);
   }

   public ServerboundResourcePackPacket.Action getAction() {
      return this.action;
   }

   public static enum Action {
      SUCCESSFULLY_LOADED,
      DECLINED,
      FAILED_DOWNLOAD,
      ACCEPTED;
   }
}