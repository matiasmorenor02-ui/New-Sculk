package com.scalk.worldgen;

import com.mojang.datafixers.util.Pair;
import com.scalk.ScalkBiomes;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

/**
 * Region de TerraBlender: copia el overworld vanilla y reemplaza el old growth birch forest
 * por el bosque scalk (mismos parametros climaticos que el bioma original).
 */
public class ScalkRegion extends Region {
    public ScalkRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry,
                          Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper,
                builder -> builder.replaceBiome(Biomes.OLD_GROWTH_BIRCH_FOREST, ScalkBiomes.SCALK_FOREST));
    }
}
