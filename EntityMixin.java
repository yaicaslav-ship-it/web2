package com.example.jimhelper.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "canHit", at = @At("HEAD"), cancellable = true)
    private void ignoreEntityWhenHoldingCobweb(CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            boolean holdingWeb = client.player.getMainHandStack().isOf(Items.COBWEB)
                              || client.player.getOffHandStack().isOf(Items.COBWEB);

            if (holdingWeb) {
                cir.setReturnValue(false);
            }
        }
    }
}
