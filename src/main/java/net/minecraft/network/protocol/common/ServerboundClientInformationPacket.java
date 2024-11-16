package net.minecraft.network.protocol.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ClientInformation;

public record ServerboundClientInformationPacket(ClientInformation information) implements Packet<ServerCommonPacketListener> {
   public ServerboundClientInformationPacket(FriendlyByteBuf p_299808_) {
      this(new ClientInformation(p_299808_));
   }

   public void write(FriendlyByteBuf p_298054_) {
      this.information.write(p_298054_);
   }

   public void handle(ServerCommonPacketListener p_300686_) {
      p_300686_.handleClientInformation(this);
   }
}