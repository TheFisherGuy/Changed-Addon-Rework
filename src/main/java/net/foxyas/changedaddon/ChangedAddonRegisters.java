package net.foxyas.changedaddon;


import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.procedures.LaethinPropertyValueProviderProcedure;
import net.foxyas.changedaddon.procedures.TransfurTotemItemInInventoryProcedure;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.world.features.structures.FacilityPieces;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityRoomPiece;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonRegisters extends ChangedAddonModItems {

		public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChangedAddonMod.MODID);

		public static final RegistryObject<Item> LATEX_SNOW_FOX_SPAWN_EGG = ITEMS_REGISTRY.register("latex_snow_fox_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> LATEX_SNOW_FOX_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("latex_snow_fox_female_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> DAZED_LATEX_SPAWN_EGG = ITEMS_REGISTRY.register("latex_dazed_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.DAZED, 0xFFFFFFF, 0xffCFCFCF, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> PURO_KIND = ITEMS_REGISTRY.register("puro_kind_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.PURO_KIND, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> PURO_KIND_FEMALE = ITEMS_REGISTRY.register("puro_kind_female_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.PURO_KIND_FEMALE, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> BUNY = ITEMS_REGISTRY.register("buny_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.BUNY, Color3.getColor("#fee9c8").toInt(), Color3.getColor("#9c8c73").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_MALE_SPAWN_EGG = ITEMS_REGISTRY.register("snow_leopard_male_organic_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("snow_leopard_female_organic_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
		public static final RegistryObject<Item> MIRROR_WHITE_TIGER = ITEMS_REGISTRY.register("mirror_white_tiger_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.MIRROR_WHITE_TIGER, Color3.getColor("#FFFFFF").toInt(), Color3.getColor("#ACACAC").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> WOLFY_SPAWN_EGG = ITEMS_REGISTRY.register("wolfy_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.WOLFY, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> EXP2_MALE_SPAWN_EGG = ITEMS_REGISTRY.register("exp_2_male_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_2_MALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> EXP2_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("exp_2_female_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_2_FEMALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> EXP6_SPAWN_EGG = ITEMS_REGISTRY.register("exp_6_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_6, Color3.getColor("#B2B1B9").toInt(), Color3.getColor("#CAA2E6").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> EXP10_SPAWN_EGG = ITEMS_REGISTRY.register("experiment_10_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXPERIMENT_10, Color3.getColor("#181818").toInt(), Color3.getColor("#ed1c24").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> KET_SPAWN_EGG = ITEMS_REGISTRY.register("ket_experiment_009_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.KET_EXPERIMENT_009, Color3.getColor("#E9E9E9").toInt(), Color3.getColor("#66FFFF").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> PARTIAL_SNOW_LEOPARD_SPAWN_EGG = ITEMS_REGISTRY.register("latex_snow_leopard_partial_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonEntitys.SNOW_LEOPARD_PARTIAL, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		public static final RegistryObject<Item> REYN_TRANSFUR_SPAWN_EGG = ITEMS_REGISTRY.register("reyn_spawn_egg",
				() -> new ForgeSpawnEggItem(ChangedAddonModEntities.REYN, Color3.getColor("#4C4C4C").toInt(), Color3.getColor("#464646").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

		@SubscribeEvent
		public static void registerItems(FMLConstructModEvent event) {
			ChangedAddonRegisters.ITEMS_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		}

		private static RegistryObject<Item> RegisterBlockItem(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
		}

		public static String SpawnEggIdFromTransfurVariant(LatexVariant latexVariant){
			String Util = latexVariant.getFormId().toString().replace("changed_addon:form_","");
			return Util + "_spawn_egg";
		}

	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			//Add code
		});
	}

}

	@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	class ChangedAddonFacilityPieces extends FacilityPieces {
		static {
			FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:experiment_009_facility_piece"), new ResourceLocation("changed_addon:chests/destroy_structure_experiment_009_loot")));
		}
	}



