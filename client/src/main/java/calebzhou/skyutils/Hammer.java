package calebzhou.skyutils;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class Hammer extends DiggerItem {
    //private static final Tag<Block> EFFECTIVE_BLOCKS;
        ImmutableSet<Block> blocksEffective = ImmutableSet.of(Blocks.STONE, Blocks.COBBLESTONE, Blocks.SAND, Blocks.GRAVEL, Blocks.ACACIA_LOG,
                Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.GRASS_BLOCK,
                SkyutilsMod.CHARCOAL_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.NETHERRACK, Blocks.COAL_BLOCK);

    public Hammer(Tier material, int attackDamage, float attackSpeed, Item.Properties settings) {
        super((float) attackDamage, attackSpeed, material, BlockTags.MINEABLE_WITH_AXE, settings);

    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        Block block = state.getBlock();
        if (blocksEffective.stream().anyMatch(b->b==block)) {
            return true;
        }
        return false;
    }
}