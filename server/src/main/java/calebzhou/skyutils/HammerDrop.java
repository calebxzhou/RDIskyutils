package calebzhou.skyutils;

import net.minecraft.world.item.ItemStack;

public class HammerDrop {
    public ItemStack dropItem;
    public int chance;

    public HammerDrop(ItemStack dropItem, int chance) {
        this.dropItem = dropItem;
        this.chance = chance;
    }
}
