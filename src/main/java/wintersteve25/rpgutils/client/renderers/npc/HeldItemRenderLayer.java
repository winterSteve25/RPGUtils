package wintersteve25.rpgutils.client.renderers.npc;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import wintersteve25.rpgutils.common.data.loaded.npc.property.EnumNPCProperty;
import wintersteve25.rpgutils.common.data.loaded.npc.property.FloatNPCProperty;
import wintersteve25.rpgutils.common.entities.NPCEntity;

public class HeldItemRenderLayer extends GeoLayerRenderer<NPCEntity> {

    public enum RenderType {
        PLAYER {
            @Override
            public void render(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int light, NPCEntity npcEntity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch) {
                boolean flag = npcEntity.getMainArm() == HandSide.RIGHT;
                ItemStack rightHand = flag ? npcEntity.getOffhandItem() : npcEntity.getMainHandItem();
                ItemStack leftHand = flag ? npcEntity.getMainHandItem() : npcEntity.getOffhandItem();
                if (!rightHand.isEmpty() || !leftHand.isEmpty()) {
                    stack.pushPose();
                    this.renderArmWithItem(npcEntity, leftHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, stack, renderTypeBuffer, light);
                    this.renderArmWithItem(npcEntity, rightHand, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, stack, renderTypeBuffer, light);
                    stack.popPose();
                }
            }

            private void renderArmWithItem(NPCEntity npcEntity, ItemStack stack, ItemCameraTransforms.TransformType transformType, HandSide handSide, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i) {
                if (!stack.isEmpty()) {
                    matrixStack.pushPose();
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                    boolean flag = handSide == HandSide.LEFT;
                    float xOffset = (float) npcEntity.getNPCType().getProperty(FloatNPCProperty.HELD_ITEM_OFFSET_X);
                    float yOffset = (float) npcEntity.getNPCType().getProperty(FloatNPCProperty.HELD_ITEM_OFFSET_Y);
                    matrixStack.translate((flag ? xOffset : -xOffset) / 16.0F, yOffset, -0.625D);
                    Minecraft.getInstance().getItemInHandRenderer().renderItem(npcEntity, stack, transformType, flag, matrixStack, renderTypeBuffer, i);
                    matrixStack.popPose();
                }
            }
        },
        VILLAGER {
            @Override
            public void render(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int light, NPCEntity npcEntity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch) {
                stack.pushPose();
                stack.translate(0.0D, npcEntity.getEyeHeight() * 0.5, -0.4F);
                ItemStack itemStack = npcEntity.getItemBySlot(EquipmentSlotType.MAINHAND);
                Minecraft.getInstance().getItemInHandRenderer().renderItem(npcEntity, itemStack, ItemCameraTransforms.TransformType.GROUND, false, stack, renderTypeBuffer, light);
                stack.popPose();
            }
        };

        public abstract void render(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int light, NPCEntity npcEntity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch);
    }

    public HeldItemRenderLayer(IGeoRenderer<NPCEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int light, NPCEntity npcEntity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch) {
        RenderType renderType = (RenderType) npcEntity.getNPCType().getProperty(EnumNPCProperty.ITEM_RENDER_TYPE);
        renderType.render(stack, renderTypeBuffer, light, npcEntity, limbSwing, limbSwingAmount, partialTicks, age, headYaw, headPitch);
    }
}
