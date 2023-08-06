package com.ewyboy.cultivatedtech.register;

import com.ewyboy.cultivatedtech.content.block.TerraCombustorBlock;
import com.ewyboy.cultivatedtech.content.entity.TerraCombustorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.ewyboy.cultivatedtech.CultivatedTech.MOD_ID;

public class Registry {
    
    public static void init(IEventBus bus) {
        BLOCKS.BLOCKS.register(bus);
        ITEMS.ITEMS.register(bus);
        BLOCK_ENTITIES.BLOCK_ENTITIES.register(bus);
        CREATIVE_TABS.CREATIVE_TABS.register(bus);
    }

    public static final class CREATIVE_TABS {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

        public static final RegistryObject<CreativeModeTab> NEO_TAB = CREATIVE_TABS.register("neo_tab", () -> CreativeModeTab.builder()
                .title(Component.literal("Cultivated Tech"))
                .icon(() -> ITEMS.TERRA_COMBUSTOR_ITEMBLOCK.get().getDefaultInstance())
                .displayItems(
                        (parameters, output) -> {
                            output.accept(ITEMS.NEO_ITEM.get().asItem().getDefaultInstance());
                            output.accept(ITEMS.TERRA_COMBUSTOR_ITEMBLOCK.get().getDefaultInstance());
                        })
                .build()
        );

    }

    public static final class BLOCKS {

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

        public static final RegistryObject<Block> TERRA_COMBUSTOR_BLOCK = BLOCKS.register("terra_combustor", () ->
                new TerraCombustorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
        );

    }

    public static final class ITEMS {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

        public static final RegistryObject<Item> TERRA_COMBUSTOR_ITEMBLOCK = ITEMS.register("terra_combustor",
                () -> new BlockItem(BLOCKS.TERRA_COMBUSTOR_BLOCK.get(), new Item.Properties())
        );

        public static final RegistryObject<Item> NEO_ITEM = ITEMS.register("neo_item",
                () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(1).saturationMod(2f).build()))
        );

    }

    public static final class BLOCK_ENTITIES {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

        public static final RegistryObject<BlockEntityType<TerraCombustorBlockEntity>> TERRA_COMBUSTOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("terra_combustor",
                () -> BlockEntityType.Builder.of(TerraCombustorBlockEntity :: new, BLOCKS.TERRA_COMBUSTOR_BLOCK.get()).build(null)
        );
    }
    
}
