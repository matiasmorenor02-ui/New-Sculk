package com.scalk;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public final class ScalkBiomes {
    public static final ResourceKey<Biome> SCALK_FOREST =
            ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(ScalkMod.MOD_ID, "scalk_forest"));

    private ScalkBiomes() {}
}
