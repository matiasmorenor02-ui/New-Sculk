package com.scalk.block;

import com.scalk.ScalkBlocks;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Bloque de scalk: se comporta exactamente como el sculk, pero propaga venas de scalk. */
public class ScalkBlock extends SculkBlock {
    public ScalkBlock(Properties properties) {
        super(properties);
    }

    // Copia del metodo por defecto de SculkBehaviour, cambiando la vena de sculk por la de scalk.
    @Override
    public boolean attemptSpreadVein(LevelAccessor level, BlockPos pos, BlockState state,
                                     Collection<Direction> directions, boolean markForPostprocessing) {
        return ((MultifaceSpreadeableBlock) ScalkBlocks.SCALK_VEIN.get())
                .getSpreader().spreadAll(state, level, pos, markForPostprocessing) > 0L;
    }
}
