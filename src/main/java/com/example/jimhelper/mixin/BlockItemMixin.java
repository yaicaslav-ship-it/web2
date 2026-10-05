package com.example.jimhelper.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Inject(method = "canPlace", at = @At("HEAD"), cancellable = true)
    private void allowCobwebInEntity(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (context.getStack().isOf(Items.COBWEB)) {
            // Если опорный блок позволяет размещение паутины — одобряем установку
            if (state.canPlaceAt(context.getWorld(), context.getBlockPos())) {
                cir.setReturnValue(true);
            }
        }
    }
}
