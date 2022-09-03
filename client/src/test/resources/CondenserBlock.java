import java.util.Optional;

import calebzhou.skyutils.SkyutilsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class CondenserBlock extends HorizontalDirectionalBlock implements EntityBlock, BucketPickup {
    public static final IntegerProperty LEVEL;

    public CondenserBlock() {
        super(Properties.of(Material.WOOD).noOcclusion());
        registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0).setValue(FACING, Direction.NORTH));
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
            BlockEntityType<T> type) {
        return !world.isClientSide ? checkType(type, SkyutilsMod.CONDENSER_ENTITY, CondenserEntity::tick) : null;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.getPickupSound();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(LEVEL, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CondenserEntity(pos, state);
    }

    public void setLevel(Level world, BlockPos pos, BlockState state, int level) {

        world.setBlock(pos, (BlockState) state.setValue(LEVEL, Mth.clamp(level, 0, 7)), 2);
        world.blockUpdated(pos, this);
    }

    public void incLevel(Level world, BlockPos pos, BlockState state) {
        int level = ((Integer) state.getValue(LEVEL)) + 1;
        world.setBlock(pos, (BlockState) state.setValue(LEVEL, Mth.clamp(level, 0, 7)), 2);
        world.blockUpdated(pos, this);
    }

    public int getLevel(BlockState state) {
        return (Integer) state.getValue(LEVEL);
    }

    // @Override
    // public void precipitationTick(World world, BlockPos pos) {
    /*
     * System.out.println("condenser rainTick "); if (!world.isClient()) { //if
     * (world.random.nextInt(10) <= 3) { BlockState state =
     * world.getBlockState(pos); if ((Integer)state.get(LEVEL) < 7) {
     * world.setBlockState(pos, (BlockState) state.cycle(LEVEL), 2);
     * System.out.println("condenser level rain " + (Integer) state.get(LEVEL)); } }
     * }
     */
    // }

    @Override
    public ItemStack pickupBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        if (!world.isClientSide()) {
            int i = (Integer) state.getValue(LEVEL);
            System.out.println(" tryDrainFluid condenser level " + i);
            if (i == 7) {
                this.setLevel((Level) world, pos, state, 0);
                CondenserEntity e = (CondenserEntity) world.getBlockEntity(pos);
                if (e != null) {
                    e.empty();
                }
                // world.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BUCKET_FILL,
                // SoundCategory.BLOCKS, 1.0F,1.0F);
                return new ItemStack(SkyutilsMod.WATER_CRUCIBLE);
            }
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType,
            BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (@Nullable BlockEntityTicker<A>) ticker : null;
    }

    static {
        LEVEL = IntegerProperty.create("level", 0, 7);
        // FACING = Properties.HORIZONTAL_FACING;
    }
}
