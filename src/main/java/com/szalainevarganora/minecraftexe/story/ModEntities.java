package com.szalainevarganora.minecraftexe.story;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import com.szalainevarganora.minecraftexe.Reference;
import com.szalainevarganora.minecraftexe.ExampleMod;
import com.szalainevarganora.minecraftexe.entities.TheLordEntity;
import com.szalainevarganora.minecraftexe.entities.TheDarkEntity;

/**
 * Registers mod entities.
 */
public class ModEntities {
    private static int nextId = 0;

    public static void registerEntities() {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "thelord"), TheLordEntity.class, "TheLord", nextId++, ExampleMod.instance, 80, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "thedark"), TheDarkEntity.class, "TheDark", nextId++, ExampleMod.instance, 80, 3, true);
    }
}
