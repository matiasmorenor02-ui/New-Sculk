package com.scalk.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.scalk.ScalkBlocks;
import com.scalk.ScalkContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SculkSpreader;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Cuando el spreader lo mueve un catalizador de scalk (pos = posicion del catalizador),
 * todo lo que se coloque durante esa actualizacion sera scalk en vez de sculk.
 */
@Mixin(SculkSpreader.class)
public abstract class SculkSpreaderMixin {
    @WrapMethod(method = "updateCursors")
    private void scalk$updateInScalkContext(LevelAccessor level, BlockPos catalystPos, RandomSource random,
                                            boolean spreadVeins, Operation<Void> original) {
        boolean scalk = ScalkContext.isActive() || level.getBlockState(catalystPos).is(ScalkBlocks.SCALK_CATALYST.get());
        boolean previous = ScalkContext.set(scalk);
        try {
            original.call(level, catalystPos, random, spreadVeins);
        } finally {
            ScalkContext.set(previous);
        }
    }
}
