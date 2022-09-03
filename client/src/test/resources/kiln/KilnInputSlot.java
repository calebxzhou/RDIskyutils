package kiln;

import calebzhou.skyutils.SkyutilsMod;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class KilnInputSlot extends Slot {
   

   public KilnInputSlot(Container inventory, int invSlot, int xPosition, int yPosition) {
      super(inventory, invSlot, xPosition, yPosition);      
   }

   public boolean mayPlace(ItemStack stack) {       
      return (stack.getItem() == SkyutilsMod.RAW_CRUCIBLE)||(stack.getItem() == Items.COBBLESTONE);
   }
}
