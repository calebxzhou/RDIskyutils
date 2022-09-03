package kiln;

import calebzhou.skyutils.SkyutilsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class KilnScreenHandler extends AbstractContainerMenu {
    private final Container inventory; 
    private static final int INVENTORY_SIZE = 4; 
    ContainerData propertyDelegate;

    public KilnScreenHandler(int syncId, Inventory playerInventory) {
        
        this(syncId, playerInventory, new SimpleContainer(INVENTORY_SIZE),new SimpleContainerData(3));
    }
 
    protected KilnScreenHandler(int syncId, Inventory playerInventory, Container inventory,ContainerData pd) {
        super(SkyutilsMod.KILN_SCREEN_HANDLER, syncId); // Since we didn't create a ContainerType, we will place null here.
        this.inventory = inventory;
        checkContainerSize(inventory, INVENTORY_SIZE);
        //checkContainerDataCount(pd, 3);
        this.propertyDelegate = pd;
        inventory.startOpen(playerInventory.player);
        
        this.addSlot(new KilnInputSlot(inventory, 0, 56, 17));//input
        this.addSlot(new KilnCrucibleSlot(inventory, 1, 23, 17));//crucible
        this.addSlot(new KilnFuelSlot(inventory, 2, 56, 53));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 3, 116, 35));
         
        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }
        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
        this.addDataSlots(propertyDelegate);
    }
 
    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }
    @Environment(EnvType.CLIENT)
    public int getBurnTime() {
        return this.propertyDelegate.get(0);
    }
    @Environment(EnvType.CLIENT)
    public int getProgress() {
        return this.propertyDelegate.get(2);
    }
    @Environment(EnvType.CLIENT)
    public int getCoocktime() {
        return this.propertyDelegate.get(1);        
    }
    
 
    // Shift + Player Inv Slot
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
 
            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
 
        return newStack;
    }
}