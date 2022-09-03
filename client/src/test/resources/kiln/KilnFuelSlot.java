package kiln;

import calebzhou.skyutils.SkyutilsMod;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class KilnFuelSlot extends Slot {
   

   public KilnFuelSlot(Container inventory, int invSlot, int xPosition, int yPosition) {
      super(inventory, invSlot, xPosition, yPosition);      
   }

   public boolean mayPlace(ItemStack stack) {       
      return stack.getItem() == SkyutilsMod.CHARCOAL_BLOCK_ITEM.asItem();
   }
}
