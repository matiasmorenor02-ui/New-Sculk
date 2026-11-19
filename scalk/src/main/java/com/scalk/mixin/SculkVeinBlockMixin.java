package com.scalk.mixin;

import com.llamalad7.mixinextras.injector.v2.ModifyExpressionValue;
import com.scalk.ScalkContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkVeinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Las venas de scalk convierten el terreno en scalk y se vuelven a plantar como venas de scalk. */
@Mixin(SculkVeinBlock.class)
public abstract class SculkVeinBlockMixin {
    @ModifyExpressionValue(method = {"attemptPlaceSculk", "onDischarged"},
            at = @At(value = "FIELD", opcode = 178,
                    target = "Lnet/minecraft/world/level/block/Blocks;SCULK:Lnet/minecraft/world/level/block/Block;"))
    private static Block scalk$sculk(Block original) {
        return ScalkContext.map(original);
    }

    @ModifyExpressionValue(method = {"regrow", "hasSubstrateAccess"},
            at = @At(value = "FIELD", opcode = 178,
                    target = "Lnet/minecraft/world/level/block/Blocks;SCULK_VEIN:Lnet/minecraft/world/level/block/Block;"))
    private static Block scalk$vein(Block original) {
        return ScalkContext.map(original);
    }
}
