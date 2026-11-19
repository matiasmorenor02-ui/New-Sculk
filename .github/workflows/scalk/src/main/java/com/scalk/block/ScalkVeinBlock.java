package com.scalk.block;

import com.scalk.ScalkBlocks;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
import net.minecraft.world.level.block.SculkVeinBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Vena de scalk: igual que la vena de sculk, pero convierte el terreno en scalk. */
public class ScalkVeinBlock extends SculkVeinBlock {
    public ScalkVeinBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean attemptSpreadVein(LevelAccessor level, BlockPos pos, BlockState state,
                                     Collection<Direction> directions, boolean markForPostprocessing) {
        return ((MultifaceSpreadeableBlock) ScalkBlocks.SCALK_VEIN.get())
                .getSpreader().spreadAll(state, level, pos, markForPostprocessing) > 0L;
    }
}
