import calebzhou.skyutils.SkyutilsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KilnBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> inventory;
    private int burn_time = 0;
    private int cook_time = 0;
    private int progress = 0;
    private static final int INVENTORY_SIZE = 4;
    public static final int CHARCOAL_BURN_TIME = 1000;
    public static final int COBBLESTONE_COOK_TIME = 1000;
    public static final int COBBLESTONE_COST = 16;
    public static final int RAW_CRUCIBLE_COOK_TIME = 1000;
    protected final ContainerData propertyDelegate = new ContainerData() {
        @Override
        public int get(int key) {
            switch (key) {
                case 0:
                    return KilnBlockEntity.this.burn_time;
                case 1:
                    return KilnBlockEntity.this.cook_time;
                case 2:
                    return KilnBlockEntity.this.progress;
                default:
                    return 0;
            }
        }

        public void set(int key, int value) {
            switch (key) {
                case 0:
                    KilnBlockEntity.this.burn_time = value;
                    break;
                case 1:
                    KilnBlockEntity.this.cook_time = value;
                    break;
                case 2:
                    KilnBlockEntity.this.progress = value;
                    break;
            }
        }

        public int getCount() {
            return 3;
        }
    };

    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(SkyutilsMod.KILN_ENTITY_TYPE, pos, state);
        this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
        // this.propertyDelegate
    };

    /*
     * @Override public Text getDisplayName() { return new
     * TranslatableText(getCachedState().getBlock().getTranslationKey()); }
     */
    @Override
    protected Component getDefaultName() {
        return Component.literal("窑炉");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new KilnScreenHandler(syncId, playerInventory, (Container) this, this.propertyDelegate);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.inventory);
        this.burn_time = tag.getInt("burn_time");
        this.cook_time = tag.getInt("cook_time");
        this.progress = tag.getInt("progress");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("burn_time", this.burn_time);
        tag.putInt("cook_time", this.cook_time);
        tag.putInt("progress", this.progress);
        ContainerHelper.saveAllItems(tag, this.inventory);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, KilnBlockEntity blockEntity) {
        blockEntity.tick();
    }

    private void tick() {
        if (this.burn_time > 0) {
            this.burn_time--;
        }
        if (!this.level.isClientSide) {
            ItemStack item = (ItemStack) this.inventory.get(0);
            ItemStack crucible = (ItemStack) this.inventory.get(1);
            ItemStack fuel = (ItemStack) this.inventory.get(2);
            ItemStack out = (ItemStack) this.inventory.get(3);

            if (out.isEmpty()) {
                if (!crucible.isEmpty()) {
                    if (!item.isEmpty() && item.getItem() == Items.COBBLESTONE && item.getCount() >= COBBLESTONE_COST) {
                        cook(item, fuel, crucible, COBBLESTONE_COOK_TIME, COBBLESTONE_COST,
                                (Item) SkyutilsMod.LAVA_CRUCIBLE);
                    } else {
                        this.cook_time = 0;
                        this.progress = 0;
                    }
                } else {
                    if (!item.isEmpty() && item.getItem() == SkyutilsMod.RAW_CRUCIBLE) {
                        cook(item, fuel, crucible, RAW_CRUCIBLE_COOK_TIME, 1, SkyutilsMod.CRUCIBLE);
                    } else {
                        this.cook_time = 0;
                        this.progress = 0;
                    }
                }
            }
        }

        this.setChanged();
    }


    private boolean cook(ItemStack item, ItemStack fuel, ItemStack crucible, int total_cook_time, int dec, Item out) {
        if (burn_time == 0) {
            if (!fuel.isEmpty()) {
                this.burn_time = CHARCOAL_BURN_TIME;
                fuel.shrink(1);
            }
        }
        if (burn_time > 0) {

            if (this.cook_time == 0) {
                this.cook_time = total_cook_time;
            }
            if (this.cook_time > 0) {
                this.cook_time--;
                this.progress = (int) ((1.0f - (this.cook_time / (float) total_cook_time)) * 1000.0f);
                if (this.cook_time == 0) {
                    this.inventory.set(3, new ItemStack(out));
                    item.shrink(dec);
                    if (crucible != null)
                        crucible.shrink(1);
                    this.cook_time = 0;
                    this.progress = 0;
                    return false;
                }
            }
        }

        return this.cook_time > 0;
    }

}
