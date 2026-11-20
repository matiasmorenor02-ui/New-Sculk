package com.scalk;

import com.scalk.block.ScalkBlock;
import com.scalk.block.ScalkVeinBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SculkCatalystBlock;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScalkBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ScalkMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ScalkMod.MOD_ID);

    // --- Scalk: mismas propiedades que el sculk original, comportamiento heredado de las clases vanilla
    public static final DeferredBlock<ScalkBlock> SCALK =
            BLOCKS.registerBlock("scalk", ScalkBlock::new, () -> Properties.ofFullCopy(Blocks.SCULK));
    public static final DeferredBlock<ScalkVeinBlock> SCALK_VEIN =
            BLOCKS.registerBlock("scalk_vein", ScalkVeinBlock::new, () -> Properties.ofFullCopy(Blocks.SCULK_VEIN));
    public static final DeferredBlock<SculkCatalystBlock> SCALK_CATALYST =
            BLOCKS.registerBlock("scalk_catalyst", SculkCatalystBlock::new, () -> Properties.ofFullCopy(Blocks.SCULK_CATALYST));

    // --- Abedul drenado
    public static final DeferredBlock<RotatedPillarBlock> DRAINED_BIRCH_LOG =
            BLOCKS.registerBlock("drained_birch_log", RotatedPillarBlock::new, () -> Properties.ofFullCopy(Blocks.BIRCH_LOG));
    public static final DeferredBlock<TintedParticleLeavesBlock> DRAINED_BIRCH_LEAVES =
            BLOCKS.registerBlock("drained_birch_leaves", p -> new TintedParticleLeavesBlock(0.0F, p),
                    () -> Properties.ofFullCopy(Blocks.BIRCH_LEAVES));

    // --- Tierra drenada: el suelo del bioma (reemplaza al pasto y la tierra)
    public static final DeferredBlock<Block> DRAINED_DIRT =
            BLOCKS.registerBlock("drained_dirt", Block::new, () -> Properties.ofFullCopy(Blocks.DIRT));

    static {
        ITEMS.registerSimpleBlockItem(SCALK);
        ITEMS.registerSimpleBlockItem(SCALK_VEIN);
        ITEMS.registerSimpleBlockItem(SCALK_CATALYST);
        ITEMS.registerSimpleBlockItem(DRAINED_BIRCH_LOG);
        ITEMS.registerSimpleBlockItem(DRAINED_BIRCH_LEAVES);
        ITEMS.registerSimpleBlockItem(DRAINED_DIRT);
    }

    private ScalkBlocks() {}

    /** Devuelve el equivalente scalk de un bloque de sculk vanilla, o el mismo bloque si no tiene. */
    public static Block scalkEquivalent(Block vanilla) {
        if (vanilla == Blocks.SCULK) return SCALK.get();
        if (vanilla == Blocks.SCULK_VEIN) return SCALK_VEIN.get();
        if (vanilla == Blocks.SCULK_CATALYST) return SCALK_CATALYST.get();
        return vanilla;
    }
}
