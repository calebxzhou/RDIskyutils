import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Crucible extends Item {
    private final Fluid fluid;

    public Crucible(Fluid fluid, Item.Properties settings) {
        super(settings);
        this.fluid = fluid;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(world, user, this.fluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY
                : ClipContext.Fluid.NONE);

        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        } else if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos2, direction, itemStack)) {
                BlockState blockState;
                if (this.fluid == Fluids.EMPTY) {
                    blockState = world.getBlockState(blockPos);
                    if (blockState.getBlock() instanceof BucketPickup) {
                        ItemStack fluid_item = ((BucketPickup) blockState.getBlock()).pickupBlock(world, blockPos,
                                blockState);
                        //System.out.println("fluid: "+fluid_item);

                        if (fluid_item != ItemStack.EMPTY) {
                            user.awardStat(Stats.ITEM_USED.get(this));
                            ItemStack item = null;
                            if (fluid_item.getItem() == Items.LAVA_BUCKET || fluid_item.getItem() == SkyutilsMod.LAVA_CRUCIBLE) {
                                item = new ItemStack(SkyutilsMod.LAVA_CRUCIBLE);
                            } else if (fluid_item.getItem() == Items.WATER_BUCKET || fluid_item.getItem() == SkyutilsMod.WATER_CRUCIBLE) {
                                item = new ItemStack(SkyutilsMod.WATER_CRUCIBLE);
                            }
                            if(item!=null){
                                user.playSound(fluid_item.getItem() == Items.LAVA_BUCKET ? SoundEvents.BUCKET_FILL_LAVA
                                        : SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
                                ItemStack itemStack2 = this.getFilledStack(itemStack, user, item.getItem());
                                if (!world.isClientSide) {
                                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) user, item);
                                }
                                return InteractionResultHolder.success(itemStack2);
                            }
                        }
                    }

                    return InteractionResultHolder.fail(itemStack);
                } else {
                    blockState = world.getBlockState(blockPos);
                    BlockPos blockPos3 = blockState.getBlock() instanceof LiquidBlockContainer && this.fluid == Fluids.WATER
                            ? blockPos
                            : blockPos2;
                    if (this.placeFluid(user, world, blockPos3, blockHitResult)) {
                        this.onEmptied(world, itemStack, blockPos3);
                        if (user instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) user, blockPos3, itemStack);
                        }

                        user.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.success(this.getEmptiedStack(itemStack, user));
                    } else {
                        return InteractionResultHolder.fail(itemStack);
                    }
                }
            } else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }

    protected ItemStack getEmptiedStack(ItemStack stack, Player player) {
        // return !player.abilities.creativeMode ? new ItemStack(Items.BUCKET) : stack;
        return !player.getAbilities().instabuild ? new ItemStack(SkyutilsMod.CRUCIBLE) : stack;
    }

    public void onEmptied(Level world, ItemStack stack, BlockPos pos) {
    }

    private ItemStack getFilledStack(ItemStack stack, Player player, Item filledBucket) {
        if (player.getAbilities().instabuild) {
            return stack;
        } else {
            stack.shrink(1);
            if (stack.isEmpty()) {
                return new ItemStack(filledBucket);
            } else {
                if (!player.getInventory().add(new ItemStack(filledBucket))) {
                    player.drop(new ItemStack(filledBucket), false);
                }

                return stack;
            }
        }
    }

    public boolean placeFluid(Player player, Level world, BlockPos pos, BlockHitResult hitResult) {
        if (!(this.fluid instanceof Fluid)) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(pos);
            Material material = blockState.getMaterial();
            boolean bl = blockState.canBeReplaced(this.fluid);
            if (!blockState.isAir() && !bl && (!(blockState.getBlock() instanceof LiquidBlockContainer)
                    || !((LiquidBlockContainer) blockState.getBlock()).canPlaceLiquid(world, pos, blockState, this.fluid))) {
                return hitResult == null ? false
                        : this.placeFluid(player, world, hitResult.getBlockPos().relative(hitResult.getDirection()),
                                (BlockHitResult) null);
            } else {
                if (world.dimensionType().ultraWarm() && this.fluid.isSame(Fluids.WATER)) {
                    int i = pos.getX();
                    int j = pos.getY();
                    int k = pos.getZ();
                    world.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F,
                            2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        world.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(),
                                (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else if (blockState.getBlock() instanceof LiquidBlockContainer && this.fluid == Fluids.WATER) {
                    if (((LiquidBlockContainer) blockState.getBlock()).placeLiquid(world, pos, blockState,
                            this.fluid.defaultFluidState())) {
                        this.playEmptyingSound(player, world, pos);
                    }
                } else {
                    if (!world.isClientSide && bl && !material.isLiquid()) {
                        world.destroyBlock(pos, true);
                    }

                    this.playEmptyingSound(player, world, pos);
                    world.setBlock(pos, this.fluid.defaultFluidState().createLegacyBlock(), 11);
                }

                return true;
            }
        }
    }

    protected void playEmptyingSound(Player player, Level world, BlockPos pos) {
        SoundEvent soundEvent = this.fluid.isSame(Fluids.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA
                : SoundEvents.BUCKET_EMPTY;
        world.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
