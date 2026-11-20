package com.scalk;

import com.scalk.worldgen.ScalkRegion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import terrablender.api.MaterialRuleManager;
import terrablender.api.Regions;

@Mod(ScalkMod.MOD_ID)
public class ScalkMod {
    public static final String MOD_ID = "scalk";

    /** Peso de la region en TerraBlender (el overworld vanilla pesa 10 por defecto). */
    private static final int REGION_WEIGHT = 10;

    public ScalkMod(IEventBus modBus) {
        ScalkBlocks.BLOCKS.register(modBus);
        ScalkBlocks.ITEMS.register(modBus);

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::addBlockEntityBlocks);
        modBus.addListener(this::addCreativeTabItems);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Regions.register(new ScalkRegion(Identifier.fromNamespaceAndPath(MOD_ID, "overworld"), REGION_WEIGHT));
            // Suelo del bioma: data/scalk/worldgen/material_rule/scalk_forest.json (solo afecta a biomas "scalk:")
            MaterialRuleManager.addRules(MaterialRuleManager.RuleCategory.OVERWORLD, MOD_ID,
                    ResourceKey.create(Registries.MATERIAL_RULE, Identifier.fromNamespaceAndPath(MOD_ID, "scalk_forest")));
        });
    }

    /**
     * El catalizador de scalk reutiliza la block entity del catalizador de vanilla,
     * asi que solo hay que decirle que tambien es valido para ese tipo.
     */
    private void addBlockEntityBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityTypes.SCULK_CATALYST, ScalkBlocks.SCALK_CATALYST.get());
    }

    private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        // Las claves de CreativeModeTabs son privadas en 26.3, asi que se compara por identificador.
        if (event.getTabKey().identifier().equals(Identifier.withDefaultNamespace("natural_blocks"))) {
            event.accept(ScalkBlocks.SCALK.get());
            event.accept(ScalkBlocks.SCALK_VEIN.get());
            event.accept(ScalkBlocks.SCALK_CATALYST.get());
            event.accept(ScalkBlocks.DRAINED_BIRCH_LOG.get());
            event.accept(ScalkBlocks.DRAINED_BIRCH_LEAVES.get());
            event.accept(ScalkBlocks.DRAINED_DIRT.get());
        }
    }
}
