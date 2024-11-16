package net.minecraft.network.protocol.handshake;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientIntentionPacket(int protocolVersion, String hostName, int port, ClientIntent intention) implements Packet<ServerHandshakePacketListener> {
   private static final int MAX_HOST_LENGTH = 255;

   public ClientIntentionPacket(FriendlyByteBuf p_179801_) {
      this(p_179801_.readVarInt(), p_179801_.readUtf(255), p_179801_.readUnsignedShort(), ClientIntent.byId(p_179801_.readVarInt()));
   }

   public void write(FriendlyByteBuf p_134737_) {
      p_134737_.writeVarInt(this.protocolVersion);
      p_134737_.writeUtf(this.hostName);
      p_134737_.writeShort(this.port);
      p_134737_.writeVarInt(this.intention.id());
   }

   public void handle(ServerHandshakePacketListener p_134734_) {
      p_134734_.handleIntention(this);
   }

   public ConnectionProtocol nextProtocol() {
      return this.intention.protocol();
   }
}