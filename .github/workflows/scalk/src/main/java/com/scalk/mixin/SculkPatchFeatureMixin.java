package com.scalk.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.scalk.ScalkBiomes;
import com.scalk.ScalkContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.SculkPatchFeature;
import org.spongepowered.asm.mixin.Mixin;

/** Los parches de sculk generados dentro del bioma scalk_forest se convierten en parches de scalk. */
@Mixin(SculkPatchFeature.class)
public abstract class SculkPatchFeatureMixin {
    @WrapMethod(method = "place")
    private boolean scalk$placeInScalkContext(WorldGenLevel level, ChunkGenerator generator, RandomSource random,
                                              BlockPos origin, Operation<Boolean> original) {
        boolean inScalkBiome = level.getBiome(origin).is(ScalkBiomes.SCALK_FOREST);
        boolean previous = ScalkContext.set(inScalkBiome || ScalkContext.isActive());
        try {
            return original.call(level, generator, random, origin);
        } finally {
            ScalkContext.set(previous);
        }
    }
}
