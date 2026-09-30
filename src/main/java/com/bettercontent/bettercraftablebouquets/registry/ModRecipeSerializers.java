package com.bettercontent.bettercraftablebouquets.registry;

import com.bettercontent.bettercraftablebouquets.ProceduralBouquets;
import com.bettercontent.bettercraftablebouquets.recipe.PottedBouquetRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProceduralBouquets.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PottedBouquetRecipe>> POTTED_BOUQUET =
        SERIALIZERS.register("potted_bouquet", () -> new SimpleCraftingRecipeSerializer<>(PottedBouquetRecipe::new));

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}
