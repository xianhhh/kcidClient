package net.minecraft.network.protocol.common;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundResourcePackPacket implements Packet<ClientCommonPacketListener> {
   public static final int MAX_HASH_LENGTH = 40;
   private final String url;
   private final String hash;
   private final boolean required;
   @Nullable
   private final Component prompt;

   public ClientboundResourcePackPacket(String p_299539_, String p_299667_, boolean p_299281_, @Nullable Component p_298259_) {
      if (p_299667_.length() > 40) {
         throw new IllegalArgumentException("Hash is too long (max 40, was " + p_299667_.length() + ")");
      } else {
         this.url = p_299539_;
         this.hash = p_299667_;
         this.required = p_299281_;
         this.prompt = p_298259_;
      }
   }

   public ClientboundResourcePackPacket(FriendlyByteBuf p_299105_) {
      this.url = p_299105_.readUtf();
      this.hash = p_299105_.readUtf(40);
      this.required = p_299105_.readBoolean();
      this.prompt = p_299105_.readNullable(FriendlyByteBuf::readComponent);
   }

   public void write(FriendlyByteBuf p_300009_) {
      p_300009_.writeUtf(this.url);
      p_300009_.writeUtf(this.hash);
      p_300009_.writeBoolean(this.required);
      p_300009_.writeNullable(this.prompt, FriendlyByteBuf::writeComponent);
   }

   public void handle(ClientCommonPacketListener p_300088_) {
      p_300088_.handleResourcePack(this);
   }

   public String getUrl() {
      return this.url;
   }

   public String getHash() {
      return this.hash;
   }

   public boolean isRequired() {
      return this.required;
   }

   @Nullable
   public Component getPrompt() {
      return this.prompt;
   }
}