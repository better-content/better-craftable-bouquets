package com.bettercontent.bettercraftablebouquets.menu;

import com.bettercontent.bettercraftablebouquets.block.BouquetGridBlock;
import com.bettercontent.bettercraftablebouquets.blockentity.BouquetGridBlockEntity;
import com.bettercontent.bettercraftablebouquets.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class BouquetGridMenu extends AbstractContainerMenu {
    public static final int GRID_COLUMNS = 16;
    public static final int GRID_ROWS = 16;
    public static final int GRID_SLOT_COUNT = GRID_COLUMNS * GRID_ROWS;
    public static final int IMAGE_WIDTH = 304;
    public static final int IMAGE_HEIGHT = 406;
    public static final int GRID_LEFT = 8;
    public static final int GRID_TOP = 18;
    public static final int PLAYER_INVENTORY_TOP = 324;
    public static final int PLAYER_HOTBAR_TOP = 382;
    public static final int ROTATE_CLOCKWISE_BUTTON = 0;
    public static final int ROTATE_COUNTERCLOCKWISE_BUTTON = 1;

    private final Container grid;
    private void observe(Player player, String event, String result, String reason, int slot, int amount) {
        if (player.level().isClientSide) return;
        var log = com.bettercontent.bettercraftablebouquets.diagnostics.RuntimeDiagnostics.LOG;
        if (!log.enabled("menu")) return;
        com.bettercontent.bettercraftablebouquets.diagnostics.FeatureObservation.menu(log,
            () -> new com.bettercontent.bettercraftablebouquets.diagnostics.PlaytestLog.Context("server",
                player.level().dimension().location().toString(), player.level().getGameTime(), player.getUUID().toString(), ""),
            event, result, reason, containerId, slot, amount);
    }

    public BouquetGridMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, findGrid(inventory, data == null ? BlockPos.ZERO : data.readBlockPos()));
    }

    public BouquetGridMenu(int id, Inventory inventory, Container grid) {
        super(ModMenus.BOUQUET_GRID.get(), id);
        this.grid = grid;
        checkContainerSize(grid, GRID_SLOT_COUNT);
        grid.startOpen(inventory.player);

        for (int z = 0; z < GRID_ROWS; z++) {
            for (int x = 0; x < GRID_COLUMNS; x++) {
                int index = slotIndex(x, z);
                addSlot(new FlowerSlot(grid, index, GRID_LEFT + (x * 18), GRID_TOP + (z * 18)));
            }
        }

        int playerLeft = (IMAGE_WIDTH - 162) / 2;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + (row * 9) + 9, playerLeft + (column * 18), PLAYER_INVENTORY_TOP + (row * 18)));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, playerLeft + (column * 18), PLAYER_HOTBAR_TOP));
        }
    }

    private static Container findGrid(Inventory inventory, BlockPos pos) {
        if (inventory.player.level().getBlockEntity(pos) instanceof BouquetGridBlockEntity grid) {
            return grid;
        }
        return new SimpleContainer(GRID_SLOT_COUNT);
    }

    public static int slotIndex(int x, int z) {
        if (x < 0 || x >= GRID_COLUMNS || z < 0 || z >= GRID_ROWS) {
            throw new IndexOutOfBoundsException("Bouquet grid coordinate outside 16x16: " + x + "," + z);
        }
        return (z * GRID_COLUMNS) + x;
    }

    public static int xForSlot(int slot) {
        checkGridSlot(slot);
        return slot % GRID_COLUMNS;
    }

    public static int zForSlot(int slot) {
        checkGridSlot(slot);
        return slot / GRID_COLUMNS;
    }

    public int rotationForSlot(int slot) {
        checkGridSlot(slot);
        if (grid instanceof BouquetGridBlockEntity bouquetGrid) {
            return bouquetGrid.getAt(xForSlot(slot), zForSlot(slot))
                .map(entry -> entry.rotation())
                .orElse(0);
        }
        return 0;
    }

    private static void checkGridSlot(int slot) {
        if (slot < 0 || slot >= GRID_SLOT_COUNT) {
            throw new IndexOutOfBoundsException("Bouquet grid slot outside 0..255: " + slot);
        }
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (isRotationClick(slotId, button, clickType)) {
            if (!player.level().isClientSide && grid instanceof BouquetGridBlockEntity bouquetGrid) {
                int quarterTurns = button == ROTATE_COUNTERCLOCKWISE_BUTTON ? -1 : 1;
                boolean changed = bouquetGrid.rotateAt(xForSlot(slotId), zForSlot(slotId), quarterTurns);
                observe(player, "bouquet.rotate", changed ? "committed" : "rejected", changed ? "native_rotation" : "empty_slot", slotId, quarterTurns);
            }
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    private static boolean isRotationClick(int slotId, int button, ClickType clickType) {
        return slotId >= 0
            && slotId < GRID_SLOT_COUNT
            && (button == ROTATE_CLOCKWISE_BUTTON || button == ROTATE_COUNTERCLOCKWISE_BUTTON)
            && clickType == ClickType.CLONE;
    }

    @Override
    public boolean stillValid(Player player) {
        return grid.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        grid.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) {
            observe(player, "bouquet.transfer", "rejected", "slot_outside_menu", index, 0);
            return ItemStack.EMPTY;
        }
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            observe(player, "bouquet.transfer", "rejected", "empty_slot", index, 0);
            return ItemStack.EMPTY;
        }

        ItemStack source = slot.getItem();
        ItemStack original = source.copy();
        if (index < GRID_SLOT_COUNT) {
            if (!moveItemStackTo(source, GRID_SLOT_COUNT, slots.size(), true)) {
                observe(player, "bouquet.transfer", "rejected", "player_inventory_full", index, 0);
                return ItemStack.EMPTY;
            }
        } else {
            if (!BouquetGridBlock.isValidFlower(source)) {
                observe(player, "bouquet.transfer", "rejected", "not_flower", index, 0);
                return ItemStack.EMPTY;
            }
            boolean movedAny = false;
            while (!source.isEmpty()) {
                int before = source.getCount();
                if (!moveItemStackTo(source, 0, GRID_SLOT_COUNT, false) || source.getCount() == before) {
                    break;
                }
                movedAny = true;
            }
            if (!movedAny) {
                observe(player, "bouquet.transfer", "rejected", "grid_full_or_no_progress", index, 0);
                return ItemStack.EMPTY;
            }
        }

        if (source.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, source);
        if (com.bettercontent.bettercraftablebouquets.diagnostics.RuntimeDiagnostics.LOG.enabled("menu"))
            observe(player, "bouquet.transfer", "committed", "native_transfer", index, original.getCount() - source.getCount());
        return original;
    }

    private static final class FlowerSlot extends Slot {
        private FlowerSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return BouquetGridBlock.isValidFlower(stack) && container.canPlaceItem(index, stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return 1;
        }
    }
}
