package com.scalk.mixin;

import com.llamalad7.mixinextras.injector.v2.ModifyExpressionValue;
import com.scalk.ScalkContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * SculkBehaviour.DEFAULT es el comportamiento de un cursor que aun no esta sobre sculk
 * (por ejemplo, justo donde muere un mob junto a un catalizador): siembra la primera vena.
 */
@Mixin(targets = "net.minecraft.world.level.block.SculkBehaviour$1")
public abstract class SculkBehaviourDefaultMixin {
    @ModifyExpressionValue(method = "attemptSpreadVein",
            at = @At(value = "FIELD", opcode = 178,
                    target = "Lnet/minecraft/world/level/block/Blocks;SCULK_VEIN:Lnet/minecraft/world/level/block/Block;"))
    private static Block scalk$vein(Block original) {
        return ScalkContext.map(original);
    }
}
