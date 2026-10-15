package com.szalainevarganora.minecraftexe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;

@Mod(
    modid = Reference.MOD_ID,
    name = Reference.NAME,
    version = Reference.VERSION,
    acceptedMinecraftVersions = "[1.12.2]"
)
public class ExampleMod {
    private static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("{} preInit started", Reference.NAME);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("{} init started", Reference.NAME);
        // Register the ChallengeHandler so it receives events
        MinecraftForge.EVENT_BUS.register(new ChallengeHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("{} postInit started", Reference.NAME);
    }
}
