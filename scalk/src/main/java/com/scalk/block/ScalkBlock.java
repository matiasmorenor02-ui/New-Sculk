package com.scalk.block;

import com.scalk.ScalkBlocks;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.SculkSpreader;
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

    /**
     * Vanilla hace crecer sensores y aulladores sobre el sculk. Aqui no los queremos: si justo se acaba
     * de colocar uno encima de este bloque, se devuelve lo que habia antes (aire o agua).
     */
    @Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos catalystPos,
                                RandomSource random, SculkSpreader spreader, boolean spreadVeins) {
        BlockPos above = cursor.getPos().above();
        BlockState before = level.getBlockState(above);
        int charge = super.attemptUseCharge(cursor, level, catalystPos, random, spreader, spreadVeins);
        BlockState after = level.getBlockState(above);
        if (after != before && (after.is(Blocks.SCULK_SENSOR) || after.is(Blocks.SCULK_SHRIEKER))) {
            level.setBlock(above, before, 3);
        }
        return charge;
    }
}
