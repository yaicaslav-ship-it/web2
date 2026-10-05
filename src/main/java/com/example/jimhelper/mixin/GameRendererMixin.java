package com.example.jimhelper.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @WrapOperation(
        method = "updateCrosshairTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
        )
    )
    private EntityHitResult filterEntitiesForCobweb(
            Entity entity,
            Vec3d min,
            Vec3d max,
            Box box,
            Predicate<Entity> predicate,
            double maxDistance,
            Operation<EntityHitResult> original
    ) {
        if (this.client.player != null) {
            boolean holdingWeb = this.client.player.getMainHandStack().isOf(Items.COBWEB)
                              || this.client.player.getOffHandStack().isOf(Items.COBWEB);

            // Если держим паутину, принудительно исключаем захват сущностей
            if (holdingWeb) {
                return null;
            }
        }
        return original.call(entity, min, max, box, predicate, maxDistance);
    }
}
