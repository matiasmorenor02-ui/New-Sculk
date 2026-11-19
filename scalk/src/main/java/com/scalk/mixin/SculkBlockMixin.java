package com.scalk.mixin;

import com.llamalad7.mixinextras.injector.v2.ModifyExpressionValue;
import com.scalk.ScalkContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Sensores y aulladores que crece un bloque de scalk deben ser de scalk. */
@Mixin(SculkBlock.class)
public abstract class SculkBlockMixin {
    @ModifyExpressionValue(method = "getRandomGrowthState",
            at = @At(value = "FIELD", opcode = 178, // GETSTATIC
                    target = "Lnet/minecraft/world/level/block/Blocks;SCULK_SENSOR:Lnet/minecraft/world/level/block/Block;"))
    private static Block scalk$sensor(Block original) {
        return ScalkContext.map(original);
    }

    @ModifyExpressionValue(method = "getRandomGrowthState",
            at = @At(value = "FIELD", opcode = 178,
                    target = "Lnet/minecraft/world/level/block/Blocks;SCULK_SHRIEKER:Lnet/minecraft/world/level/block/Block;"))
    private static Block scalk$shrieker(Block original) {
        return ScalkContext.map(original);
    }
}
