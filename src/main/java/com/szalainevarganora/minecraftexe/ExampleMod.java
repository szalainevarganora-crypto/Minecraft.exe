package com.szalainevarganora.minecraftexe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.szalainevarganora.minecraftexe.story.ModEntities;
import com.szalainevarganora.minecraftexe.story.StoryEvents;
import com.szalainevarganora.minecraftexe.story.commands.CommandMexReset;
import com.szalainevarganora.minecraftexe.story.commands.CommandMexStart;
import com.szalainevarganora.minecraftexe.story.commands.CommandMexStatus;

@Mod(
    modid = Reference.MOD_ID,
    name = Reference.NAME,
    version = Reference.VERSION,
    acceptedMinecraftVersions = "[1.12.2]"
)
public class ExampleMod {
    private static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

    @Mod.Instance(Reference.MOD_ID)
    public static ExampleMod instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("{} preInit started", Reference.NAME);
        MinecraftForge.EVENT_BUS.register(new StoryEvents());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("{} init started", Reference.NAME);
        ModEntities.registerEntities();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("{} postInit started", Reference.NAME);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        // Register story commands
        event.registerServerCommand(new CommandMexStart());
        event.registerServerCommand(new CommandMexStatus());
        event.registerServerCommand(new CommandMexReset());
        LOGGER.info("Story commands registered");
    }
}
