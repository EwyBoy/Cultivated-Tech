package com.ewyboy.cultivatedtech.client;

import com.ewyboy.cultivatedtech.CultivatedTech;
import com.ewyboy.cultivatedtech.ModLogger;
import com.ewyboy.cultivatedtech.register.Registry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CultivatedTech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientInitialization {

    @OnlyIn(Dist.CLIENT)
    public static void initRenderTypes(FMLClientSetupEvent ignoredEvent) {
        Registry.BLOCKS.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof IHasRenderType).forEach(
                block -> {
                    ModLogger.info("Setting render type for " + block.getName() + " to " + ((IHasRenderType) block).getRenderType());
                    //ItemBlockRenderTypes.setRenderLayer(block, ((IHasRenderType) block).getRenderType());
                }
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static void initSpecialRenders(FMLClientSetupEvent ignoredEvent) {
        Registry.BLOCKS.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof IHasSpecialRenderer).forEach(
                block -> ((IHasSpecialRenderer) block).initSpecialRenderer()
        );
    }

}
