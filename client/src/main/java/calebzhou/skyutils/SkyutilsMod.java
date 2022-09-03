package calebzhou.skyutils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class SkyutilsMod implements ModInitializer {

	//创造模式物品归类
	public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.build(
			new ResourceLocation(SkyutilsMod.MOD_ID, "general"),
			SkyutilsMod::getItemGroupStackIcon);



	public static final String MOD_ID = "skyutils";
	/*public static final ResourceLocation KILN = new ResourceLocation(SkyutilsMod.MOD_ID, "kiln");
	public static final ResourceLocation CONDENSER = new ResourceLocation(SkyutilsMod.MOD_ID, "condenser");*/
	public static final ResourceLocation CHARCOAL_BLOCK_ID = new ResourceLocation(SkyutilsMod.MOD_ID, "charcoal_block");

	//木炭块
	public static final Block CHARCOAL_BLOCK = new Block(
			Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).strength(5.0F, 6.0F));
	public static final BlockItem CHARCOAL_BLOCK_ITEM = new BlockItem(CHARCOAL_BLOCK,
			new Item.Properties().tab(ITEM_GROUP));
	//钻石粒
	public static final Item DIAMOND_NUGGET = new Item(new Item.Properties().tab(ITEM_GROUP));
	//木片
	public static final Item WOODCHIPS = new Item(new Item.Properties().tab(ITEM_GROUP));
	//小石子
	public static final Item PEBBLE = new Item(new Item.Properties().tab(ITEM_GROUP));
	//生坩埚
	//public static final Item RAW_CRUCIBLE = new Item(new Item.Properties().tab(ITEM_GROUP));
	/*//熟坩埚
	public static final Crucible CRUCIBLE = new Crucible(Fluids.EMPTY, new Item.Properties().tab(ITEM_GROUP));
	//水坩埚
	public static final Crucible WATER_CRUCIBLE = new Crucible(Fluids.WATER,
			new Item.Properties().tab(ITEM_GROUP).stacksTo(1));
	//岩浆坩埚
	public static final Crucible LAVA_CRUCIBLE = new Crucible(Fluids.LAVA,
			new Item.Properties().tab(ITEM_GROUP).stacksTo(1));*/

	public static final Hammer WOODEN_HAMMER = new Hammer(Tiers.WOOD, 1, -2.8F,
			(new Item.Properties()).tab(ITEM_GROUP));
	public static final Hammer STONE_HAMMER = new Hammer(Tiers.STONE, 2, -2.8F,
			(new Item.Properties()).tab(ITEM_GROUP));
	public static final Hammer IRON_HAMMER = new Hammer(Tiers.IRON, 4, -2.8F,
			(new Item.Properties()).tab(ITEM_GROUP));
	public static final Hammer DIAMOND_HAMMER = new Hammer(Tiers.DIAMOND, 8, -2.8F,
			(new Item.Properties()).tab(ITEM_GROUP));
	/*public static final Hammer NETHERITE_HAMMER = new Hammer(Tiers.NETHERITE, 12, -2.8F,
			(new Item.Properties()).tab(ITEM_GROUP));*/

/*	//窑炉
	public static BlockEntityType<KilnBlockEntity> KILN_ENTITY_TYPE;
	public static final Block KILN_BLOCK = new KilnBlock(Properties.of(Material.STONE).strength(3.5F, 3.5F));
	public static final BlockEntityType<KilnBlockEntity> KILN_ENTITY = FabricBlockEntityTypeBuilder
			.create(KilnBlockEntity::new, KILN_BLOCK).build(null);
	public static final BlockItem KILN_BLOCK_ITEM = new BlockItem(KILN_BLOCK,
			new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE));
	public static final MenuType<KilnScreenHandler> KILN_SCREEN_HANDLER = ScreenHandlerRegistry
			.registerSimple(KILN, KilnScreenHandler::new);

	// 冷凝板
	public static final Block CONDENSER_BLOCK = new CondenserBlock();
	public static BlockEntityType<CondenserEntity> CONDENSER_ENTITY = FabricBlockEntityTypeBuilder
			.create(CondenserEntity::new, CONDENSER_BLOCK).build(null);*/
/*	public static final BlockItem CONDENSER_BLOCK_ITEM = new BlockItem(CONDENSER_BLOCK,
			new Item.Properties().tab(ITEM_GROUP));*/



	@Override
	public void onInitialize() {

		// items
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "wooden_hammer"), WOODEN_HAMMER);
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "stone_hammer"), STONE_HAMMER);
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "iron_hammer"), IRON_HAMMER);
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "diamond_hammer"), DIAMOND_HAMMER);
//		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "netherite_hammer"), NETHERITE_HAMMER);
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "woodchips"), WOODCHIPS);
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "pebble"), PEBBLE);
//		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "raw_crucible"), RAW_CRUCIBLE);
/*		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "crucible"), CRUCIBLE);
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "water_crucible"), WATER_CRUCIBLE);
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "lava_crucible"), LAVA_CRUCIBLE);*/
		Registry.register(Registry.ITEM, new ResourceLocation(SkyutilsMod.MOD_ID, "diamond_nugget"), DIAMOND_NUGGET);

		// blocks

		// charcoal block
		Registry.register(Registry.BLOCK, CHARCOAL_BLOCK_ID, CHARCOAL_BLOCK);
		Registry.register(Registry.ITEM, CHARCOAL_BLOCK_ID, CHARCOAL_BLOCK_ITEM);
		//8000是煤炭块时间的一半
		FuelRegistry.INSTANCE.add(CHARCOAL_BLOCK_ITEM,8000);

		// 窑炉
		/*Registry.register(Registry.BLOCK, KILN, KILN_BLOCK);
		Registry.register(Registry.ITEM, KILN, KILN_BLOCK_ITEM);
		KILN_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, KILN, KILN_ENTITY);

		//冷凝板
		Registry.register(Registry.BLOCK, CONDENSER, CONDENSER_BLOCK);
		Registry.register(Registry.ITEM, CONDENSER, CONDENSER_BLOCK_ITEM);
		CONDENSER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CONDENSER, CONDENSER_ENTITY);*/

	}
	private static ItemStack getItemGroupStackIcon() {
		return WOODEN_HAMMER.getDefaultInstance();
	}
}