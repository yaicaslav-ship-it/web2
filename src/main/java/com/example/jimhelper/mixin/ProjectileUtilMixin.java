package com.example.jimhelper.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin {

    @Inject(
        method = "raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void ignoreEntitiesWhenHoldingCobweb(
            Entity entity,
            Vec3d min,
            Vec3d max,
            Box box,
            Predicate<Entity> predicate,
            double maxDistance,
            CallbackInfoReturnable<EntityHitResult> cir
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Проверяем, что рейкаст запущен от камеры локального игрока
        if (client.player != null && entity == client.getCameraEntity()) {
            boolean holdingWeb = client.player.getMainHandStack().isOf(Items.COBWEB)
                              || client.player.getOffHandStack().isOf(Items.COBWEB);

            if (holdingWeb) {
                // Возвращаем null, заставляя игру полностью игнорировать любые сущности
                cir.setReturnValue(null);
            }
        }
    }
}
