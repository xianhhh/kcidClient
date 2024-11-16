package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ClientboundUpdateAttributesPacket implements Packet<ClientGamePacketListener> {
   private final int entityId;
   private final List<ClientboundUpdateAttributesPacket.AttributeSnapshot> attributes;

   public ClientboundUpdateAttributesPacket(int p_133580_, Collection<AttributeInstance> p_133581_) {
      this.entityId = p_133580_;
      this.attributes = Lists.newArrayList();

      for(AttributeInstance attributeinstance : p_133581_) {
         this.attributes.add(new ClientboundUpdateAttributesPacket.AttributeSnapshot(attributeinstance.getAttribute(), attributeinstance.getBaseValue(), attributeinstance.getModifiers()));
      }

   }

   public ClientboundUpdateAttributesPacket(FriendlyByteBuf p_179447_) {
      this.entityId = p_179447_.readVarInt();
      this.attributes = p_179447_.readList((p_258211_) -> {
         ResourceLocation resourcelocation = p_258211_.readResourceLocation();
         Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(resourcelocation);
         double d0 = p_258211_.readDouble();
         List<AttributeModifier> list = p_258211_.readList((p_179457_) -> {
            return new AttributeModifier(p_179457_.readUUID(), "Unknown synced attribute modifier", p_179457_.readDouble(), AttributeModifier.Operation.fromValue(p_179457_.readByte()));
         });
         return new ClientboundUpdateAttributesPacket.AttributeSnapshot(attribute, d0, list);
      });
   }

   public void write(FriendlyByteBuf p_133590_) {
      p_133590_.writeVarInt(this.entityId);
      p_133590_.writeCollection(this.attributes, (p_296409_, p_296410_) -> {
         p_296409_.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(p_296410_.getAttribute()));
         p_296409_.writeDouble(p_296410_.getBase());
         p_296409_.writeCollection(p_296410_.getModifiers(), (p_296411_, p_296412_) -> {
            p_296411_.writeUUID(p_296412_.getId());
            p_296411_.writeDouble(p_296412_.getAmount());
            p_296411_.writeByte(p_296412_.getOperation().toValue());
         });
      });
   }

   public void handle(ClientGamePacketListener p_133587_) {
      p_133587_.handleUpdateAttributes(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public List<ClientboundUpdateAttributesPacket.AttributeSnapshot> getValues() {
      return this.attributes;
   }

   public static class AttributeSnapshot {
      private final Attribute attribute;
      private final double base;
      private final Collection<AttributeModifier> modifiers;

      public AttributeSnapshot(Attribute p_179459_, double p_179460_, Collection<AttributeModifier> p_179461_) {
         this.attribute = p_179459_;
         this.base = p_179460_;
         this.modifiers = p_179461_;
      }

      public Attribute getAttribute() {
         return this.attribute;
      }

      public double getBase() {
         return this.base;
      }

      public Collection<AttributeModifier> getModifiers() {
         return this.modifiers;
      }
   }
}