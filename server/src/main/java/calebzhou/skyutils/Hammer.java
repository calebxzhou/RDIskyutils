package calebzhou.skyutils;

import com.google.common.collect.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Hammer extends DiggerItem {
    //private static final Tag<Block> EFFECTIVE_BLOCKS;
    ImmutableSet<Block> blocksEffective = ImmutableSet.of(Blocks.STONE, Blocks.COBBLESTONE, Blocks.SAND, Blocks.GRAVEL, Blocks.ACACIA_LOG,
            Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.GRASS_BLOCK,
            SkyutilsMod.CHARCOAL_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.NETHERRACK, Blocks.COAL_BLOCK);

    /*private static final Item[] DIRT_DROPS = { Items.OAK_SAPLING, Items.ACACIA_SAPLING, Items.SPRUCE_SAPLING,
            Items.JUNGLE_SAPLING, Items.DARK_OAK_SAPLING, Items.BIRCH_SAPLING, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS,
            Items.BEETROOT_SEEDS, Items.COCOA_BEANS, Items.SWEET_BERRIES, Items.BAMBOO, Items.SUGAR_CANE };*/

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
    static final int baseChance=100;
    static final int halfChance = baseChance/2;
    static final int quarterChance = baseChance/4;
    static final int bitChance = baseChance/10;
    static final Multimap<Block, HammerDrop> hammerDropMap = HashMultimap.create();
    static {
        HammerDrop dropGravel = new HammerDrop(new ItemStack(Items.GRAVEL, 1), baseChance);
        HammerDrop dropSand = new HammerDrop(new ItemStack(Items.SAND, 1), baseChance);
        HammerDrop dropIronNugget = new HammerDrop(new ItemStack(Items.IRON_NUGGET, 1), quarterChance);
        HammerDrop dropGoldNugget = new HammerDrop(new ItemStack(Items.GOLD_NUGGET, 1), bitChance);
        HammerDrop dropClayBall = new HammerDrop(new ItemStack(Items.CLAY_BALL, 1), baseChance);
        HammerDrop dropWoodChip = new HammerDrop(new ItemStack(SkyutilsMod.WOODCHIPS, 1), baseChance);
        HammerDrop dropPebble = new HammerDrop(new ItemStack(SkyutilsMod.PEBBLE, 1), baseChance);
        HammerDrop dropMushroom = new HammerDrop(new ItemStack(Items.RED_MUSHROOM, 1), halfChance);
        hammerDropMap.put(Blocks.COBBLESTONE, dropGravel);
        hammerDropMap.put(Blocks.STONE, dropGravel);
        hammerDropMap.put(Blocks.GRAVEL, dropSand);
        hammerDropMap.put(Blocks.GRAVEL, dropIronNugget);
        hammerDropMap.put(Blocks.GRAVEL, dropGoldNugget);
        hammerDropMap.put(Blocks.SAND, dropClayBall);
        hammerDropMap.put(Blocks.OAK_LOG, dropWoodChip);
        hammerDropMap.put(Blocks.DIRT, dropPebble);
        hammerDropMap.put(Blocks.DIRT, dropMushroom);
        hammerDropMap.put(Blocks.PODZOL, dropPebble);
        hammerDropMap.put(Blocks.GRASS_BLOCK, dropPebble);
    }
    public static void remap_drop(Level world, Player player, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        Collection<HammerDrop> hammerDrops = hammerDropMap.get(block);
        for (HammerDrop hammerDrop : hammerDrops) {
            player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
            player.causeFoodExhaustion(0.1f);
            if(world.random.nextInt(0,baseChance) < hammerDrop.chance){
                Block.popResource(world, pos, hammerDrop.dropItem);
            }
        }
        /*if(block == Blocks.COBBLESTONE)


            if (path.equals("cobblestone") || path.equals("stone")) {
                stackToDrop = new ItemStack(Items.GRAVEL, (int) chance);
            } else if (path.equals("gravel")) {
                stackToDrop = new ItemStack(Items.SAND, (int) chance);
                if (world.random.nextFloat() < 0.25 * chance) {
                    stackToDrop2 = new ItemStack(Items.IRON_NUGGET, (int) chance);
                }
            } else if (path.equals("sand")) {
                stackToDrop = new ItemStack(Items.CLAY_BALL, (int) chance + 1);
                if (world.random.nextFloat() < 0.1 * chance) {
                    if (world.random.nextFloat() < 0.5) {
                        stackToDrop2 = new ItemStack(Items.CACTUS, 1);
                    } else {
                        stackToDrop2 = new ItemStack(Items.KELP, 1);
                    }
                }
            } else if (path.contains("_log")) {
                stackToDrop = new ItemStack(SkyutilsMod.WOODCHIPS, (int) chance);
            } else if (path.equals("dirt")) {
                stackToDrop = new ItemStack(SkyutilsMod.PEBBLE, (int) chance + 1);
            } else if (path.equals("podzol")) {
                stackToDrop = new ItemStack(SkyutilsMod.PEBBLE, (int) chance + 1);
                if (world.random.nextFloat() < 0.2) {
                    if (world.random.nextFloat() < 0.5) {
                        stackToDrop2 = new ItemStack(Items.BROWN_MUSHROOM, (int) chance);
                    } else {
                        stackToDrop2 = new ItemStack(Items.RED_MUSHROOM, (int) chance);
                    }
                }
            } else if (path.equals("grass_block")) {
                stackToDrop = new ItemStack(SkyutilsMod.PEBBLE, (int) chance + 1);
                if (world.random.nextFloat() < 0.2 * chance) {
                    int r = (int) Math.ceil(world.random.nextFloat() * (DIRT_DROPS.length - 1));
                    if (r >= 0 && r < DIRT_DROPS.length) {
                        stackToDrop2 = new ItemStack(DIRT_DROPS[r], 1);
                    }
                }
            } else if (path.equals("charcoal_block")) {
                if (world.random.nextFloat() < 0.05f * chance) {
                    stackToDrop = new ItemStack(SkyutilsMod.DIAMOND_NUGGET);
                    // stack = new ItemStack(Items.DIAMOND);
                } else {
                    stackToDrop = new ItemStack(Items.CHARCOAL, 8);
                }
            } else if (path.equals("coal_block")) {
                if (world.random.nextFloat() < 0.05f * chance) {
                    stackToDrop = new ItemStack(SkyutilsMod.DIAMOND_NUGGET);
                } else {
                    stackToDrop = new ItemStack(Items.COAL, 8);
                }
            } else if (path.equals("quartz_block")) {
                stackToDrop = new ItemStack(Items.QUARTZ, 4);
            } else if (path.equals("netherrack")) {
                if (world.random.nextFloat() < 0.1f * chance) {
                    stackToDrop = new ItemStack(Items.NETHER_WART, 1);
                }
                if (world.random.nextFloat() < 0.01 * chance) {
                    stackToDrop2 = new ItemStack(Items.NETHERITE_SCRAP, 1);
                } else {
                    if (world.random.nextFloat() < 0.2f * chance) {
                        if (world.random.nextFloat() < 0.5f * chance) {
                            stackToDrop2 = new ItemStack(Items.CRIMSON_ROOTS, 1);
                        } else {
                            stackToDrop2 = new ItemStack(Items.WARPED_ROOTS, 1);
                        }
                    }
                }
            }

        if (stackToDrop != null) {

            player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
            player.causeFoodExhaustion(0.005F);
            Block.popResource(world, pos, stackToDrop);
            if (stackToDrop2 != null) {
                Block.popResource(world, pos, stackToDrop2);
            }
        }
        return true;*/
    }

    /*static {
        EFFECTIVE_BLOCKS = TagBuilder.create().add().of(ImmutableSet.of(Blocks.STONE, Blocks.COBBLESTONE, Blocks.SAND, Blocks.GRAVEL, Blocks.ACACIA_LOG,
                        Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.GRASS_BLOCK,
                        SkyutilsMod.CHARCOAL_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.NETHERRACK, Blocks.COAL_BLOCK));

    }*/
}