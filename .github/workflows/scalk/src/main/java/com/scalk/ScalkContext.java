package com.scalk;

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

    public static Block map(Block vanilla) {
        return ACTIVE.get() ? ScalkBlocks.scalkEquivalent(vanilla) : vanilla;
    }
}
