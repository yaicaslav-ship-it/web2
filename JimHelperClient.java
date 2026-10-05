package com.example.jimhelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class JimHelperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Перехватываем клик ПКМ по сущности (включая стойки брони и мобов/игроков)
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient()) return ActionResult.PASS;

            // Проверяем, держит ли игрок паутину в активной руке
            if (!player.getStackInHand(hand).isOf(Items.COBWEB)) {
                return ActionResult.PASS;
            }

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.interactionManager == null) return ActionResult.PASS;

            // Вычисляем дистанцию взаимодействия с блоками для 1.21.4
            double reach = player.getAttributeValue(EntityAttributes.BLOCK_INTERACTION_RANGE);
            Vec3d eyePos = player.getEyePos();
            Vec3d rot = player.getRotationVec(1.0F);
            Vec3d end = eyePos.add(rot.multiply(reach));

            // Рейкаст строго сквозь сущность к опорному блоку
            BlockHitResult blockHit = world.raycast(new RaycastContext(
                eyePos,
                end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
            ));

            if (blockHit.getType() == HitResult.Type.BLOCK) {
                // Ставим блок паутины на опорную грань
                ActionResult result = client.interactionManager.interactBlock(client.player, hand, blockHit);
                if (result.isAccepted()) {
                    client.player.swingHand(hand);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
    }
}
