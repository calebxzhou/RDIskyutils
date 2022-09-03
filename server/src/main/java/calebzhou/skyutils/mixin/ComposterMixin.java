package calebzhou.skyutils.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.class)
public abstract class ComposterMixin {
  //堆肥桶倒出来一个草方块，instead of 骨粉
        @Redirect(method = "extractProduce",
        at=@At(value = "NEW",target = "net.minecraft.world.item.ItemStack")
        )
        private static ItemStack extractGrassBlockFromComposterBlock(ItemLike itemLike){
          return new ItemStack(Items.GRASS_BLOCK);
        }

  /*
  private static boolean addToComposter(int level, BlockState state, LevelAccessor world, BlockPos pos, ItemStack item) {

    int j = level + 1;
    world.setBlock(pos, (BlockState) state.setValue(ComposterBlock.LEVEL, j), 3);
    if (j == 7) {
      world.scheduleTick(pos, state.getBlock(), 20);
    }
    return true;

  }

  @Shadow
  native static BlockState empty(BlockState state, LevelAccessor world, BlockPos pos);

  @Inject(at = @At("HEAD"), method = "use", cancellable = true)

  public void use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit,
      CallbackInfoReturnable<InteractionResult> info) {
    int i = (Integer) state.getValue(ComposterBlock.LEVEL);
    ItemStack itemStack = player.getItemInHand(hand);
    if (i < 8 && ComposterBlock.COMPOSTABLES.containsKey(itemStack.getItem())) {
      if (i < 7 && !world.isClientSide) {
        boolean bl = ComposterMixin.addToComposter(i, state, world, pos, itemStack);
        world.levelEvent(1500, pos, bl ? 1 : 0);
        if (!player.getAbilities().instabuild) {
          itemStack.shrink(1);
        }
      }

      info.setReturnValue(InteractionResult.SUCCESS);
    } else if (i == 8) {
      if (!world.isClientSide) {

        double d = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
        double e = (double) (world.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
        double g = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
        ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + d, (double) pos.getY() + e,
            (double) pos.getZ() + g, new ItemStack(Items.GRASS_BLOCK));
        itemEntity.setDefaultPickUpDelay();
        world.addFreshEntity(itemEntity);
      }

      empty(state, world, pos);
      world.playSound((Player) null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

      info.setReturnValue(InteractionResult.SUCCESS);
    } else {

      info.setReturnValue(InteractionResult.PASS);
    }
    info.cancel();
    return;
  }*/

}
