package com.scalk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;

/**
 * Indica si el codigo de propagacion del sculk (vanilla) esta corriendo "en modo scalk".
 * Los mixins lo usan para cambiar los bloques de sculk que vanilla coloca (sculk, vena, sensor, aullador)
 * por sus equivalentes de scalk. Se activa cuando:
 *  - un catalizador de scalk actualiza sus cursores, o
 *  - se genera un parche de sculk en el bioma scalk_forest.
 */
public final class ScalkContext {
    private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private static final ThreadLocal<Boolean> WORLDGEN = ThreadLocal.withInitial(() -> Boolean.FALSE);

    /**
     * Probabilidad (0.0 a 1.0) de que, durante la generacion del bioma, nazca una vena de scalk
     * sobre la cara de un bloque de scalk. Bajala para menos venas, subela para mas.
     * Las venas sobre otros bloques (tierra, troncos) no se tocan, y los catalizadores en el juego tampoco.
     */
    public static final double WORLDGEN_VEIN_ON_SCALK_CHANCE = 0.2;

    private ScalkContext() {}

    public static boolean isActive() {
        return ACTIVE.get();
    }

    /** Fija el estado y devuelve el anterior, para poder restaurarlo en un finally. */
    public static boolean set(boolean value) {
        boolean previous = ACTIVE.get();
        ACTIVE.set(value);
        return previous;
    }

    /** Verdadero mientras se genera un parche de scalk del bioma (no cuando un catalizador propaga en el juego). */
    public static boolean isWorldgen() {
        return WORLDGEN.get();
    }

    public static boolean setWorldgen(boolean value) {
        boolean previous = WORLDGEN.get();
        WORLDGEN.set(value);
        return previous;
    }

    /** Decision estable por posicion y cara: siempre da el mismo resultado para la misma vena. */
    public static boolean allowWorldgenVein(BlockPos pos, Direction face) {
        long h = pos.asLong() * 0x9E3779B97F4A7C15L + face.ordinal() * 0xC2B2AE3D27D4EB4FL;
        h ^= h >>> 29;
        h *= 0xBF58476D1CE4E5B9L;
        h ^= h >>> 32;
        double r = (h >>> 11) * (1.0 / (1L << 53));
        return r < WORLDGEN_VEIN_ON_SCALK_CHANCE;
    }

    public static Block map(Block vanilla) {
        return ACTIVE.get() ? ScalkBlocks.scalkEquivalent(vanilla) : vanilla;
    }
}
