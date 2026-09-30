package com.bettercontent.bettercraftablebouquets;

import com.bettercontent.bettercraftablebouquets.config.ModClientConfig;
import com.bettercontent.bettercraftablebouquets.config.ModCommonConfig;
import com.bettercontent.bettercraftablebouquets.registry.ModBlockEntities;
import com.bettercontent.bettercraftablebouquets.registry.ModBlocks;
import com.bettercontent.bettercraftablebouquets.registry.ModCreativeTabs;
import com.bettercontent.bettercraftablebouquets.registry.ModItems;
import com.bettercontent.bettercraftablebouquets.registry.ModMenus;
import com.bettercontent.bettercraftablebouquets.registry.ModRecipeSerializers;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ProceduralBouquets.MOD_ID)
public final class ProceduralBouquets {
    public static final String MOD_ID = "better_craftable_bouquets";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ProceduralBouquets() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ModMenus.register(modBus);
        ModRecipeSerializers.register(modBus);
        modBus.addListener(ModCreativeTabs::onBuildCreativeTabContents);

        ModCommonConfig.register();
        ModClientConfig.register();
    }
}
