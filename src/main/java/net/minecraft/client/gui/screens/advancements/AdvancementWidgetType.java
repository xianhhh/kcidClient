package net.minecraft.client.gui.screens.advancements;

import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum AdvancementWidgetType {
   OBTAINED(new ResourceLocation("advancements/box_obtained"), new ResourceLocation("advancements/task_frame_obtained"), new ResourceLocation("advancements/challenge_frame_obtained"), new ResourceLocation("advancements/goal_frame_obtained")),
   UNOBTAINED(new ResourceLocation("advancements/box_unobtained"), new ResourceLocation("advancements/task_frame_unobtained"), new ResourceLocation("advancements/challenge_frame_unobtained"), new ResourceLocation("advancements/goal_frame_unobtained"));

   private final ResourceLocation boxSprite;
   private final ResourceLocation taskFrameSprite;
   private final ResourceLocation challengeFrameSprite;
   private final ResourceLocation goalFrameSprite;

   private AdvancementWidgetType(ResourceLocation p_300112_, ResourceLocation p_300140_, ResourceLocation p_299008_, ResourceLocation p_301311_) {
      this.boxSprite = p_300112_;
      this.taskFrameSprite = p_300140_;
      this.challengeFrameSprite = p_299008_;
      this.goalFrameSprite = p_301311_;
   }

   public ResourceLocation boxSprite() {
      return this.boxSprite;
   }

   public ResourceLocation frameSprite(FrameType p_300394_) {
      ResourceLocation resourcelocation;
      switch (p_300394_) {
         case TASK:
            resourcelocation = this.taskFrameSprite;
            break;
         case CHALLENGE:
            resourcelocation = this.challengeFrameSprite;
            break;
         case GOAL:
            resourcelocation = this.goalFrameSprite;
            break;
         default:
            throw new IncompatibleClassChangeError();
      }

      return resourcelocation;
   }
}