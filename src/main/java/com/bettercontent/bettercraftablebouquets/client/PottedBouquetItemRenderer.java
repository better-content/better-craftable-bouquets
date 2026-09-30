package com.bettercontent.bettercraftablebouquets.client;

import com.bettercontent.bettercraftablebouquets.config.ModClientConfig;
import com.bettercontent.bettercraftablebouquets.data.BouquetData;
import com.bettercontent.bettercraftablebouquets.data.BouquetEntry;
import com.bettercontent.bettercraftablebouquets.item.PottedBouquetItem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public final class PottedBouquetItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static PottedBouquetItemRenderer instance;

    private PottedBouquetItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    public static PottedBouquetItemRenderer get() {
        if (instance == null) {
            Minecraft minecraft = Minecraft.getInstance();
            instance = new PottedBouquetItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
        return instance;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        List<BouquetEntry> entries = PottedBouquetItem.readEntries(stack);
        Minecraft minecraft = Minecraft.getInstance();

        poseStack.pushPose();
        minecraft.getBlockRenderer().renderSingleBlock(
            Blocks.FLOWER_POT.defaultBlockState(),
            poseStack,
            buffer,
            packedLight,
            packedOverlay
        );
        if (!entries.isEmpty()) {
            poseStack.translate(0.5F, 0.0F, 0.5F);
            float flowerLayerScale = BouquetRenderUtil.pottedItemFlowerLayerScale();
            poseStack.scale(flowerLayerScale, flowerLayerScale, flowerLayerScale);
            BouquetRenderUtil.renderPottedBouquet(
                entries,
                poseStack,
                buffer,
                packedLight,
                packedOverlay,
                null,
                BouquetData.stableHash(entries),
                ModClientConfig.MAX_RENDERED_FLOWERS.get()
            );
        }
        poseStack.popPose();
    }

}
