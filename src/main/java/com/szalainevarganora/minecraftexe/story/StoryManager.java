package com.szalainevarganora.minecraftexe.story;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.WorldServer;

import com.szalainevarganora.minecraftexe.Reference;
import com.szalainevarganora.minecraftexe.entities.TheDarkEntity;
import com.szalainevarganora.minecraftexe.entities.TheLordEntity;

import java.util.List;

/**
 * Coordinates story events and persistence.
 */
public class StoryManager {
    public static StoryProgress getProgress(World world) {
        if (world.isRemote) return null;
        WorldServer ws = (WorldServer) world;
        MapStorage storage = ws.getPerWorldStorage();
        StoryProgress data = (StoryProgress) storage.getOrLoadData(StoryProgress.class, StoryProgress.DATA_NAME);
        if (data == null) {
            data = new StoryProgress();
            storage.setData(StoryProgress.DATA_NAME, data);
        }
        return data;
    }

    public static boolean startStory(EntityPlayerMP player) {
        World world = player.world;
        StoryProgress prog = getProgress(world);
        if (prog == null) return false;
        if (prog.started) {
            player.sendMessage(new TextComponentString("The story is already running."));
            return false;
        }

        // mark started and save center near nearest villagers if any
        prog.started = true;
        prog.chapter = 0;

        // find nearby villagers to build the scene
        double cx = player.posX;
        double cy = player.posY;
        double cz = player.posZ;
        List<EntityVillager> villagers = world.getEntitiesWithinAABB(EntityVillager.class,
                new AxisAlignedBB(player.posX - 80, player.posY - 20, player.posZ - 80, player.posX + 80, player.posY + 20, player.posZ + 80));
        if (!villagers.isEmpty()) {
            EntityVillager v = villagers.get(0);
            cx = v.posX;
            cy = v.posY;
            cz = v.posZ;
            // mark villagers as panicked
            for (EntityVillager vill : villagers) {
                vill.setCustomNameTag("Panicked Villager");
                vill.setAlwaysRenderNameTag(true);
            }
            player.sendMessage(new TextComponentString("The villagers are panicking. Strange noises echo through the streets."));
        } else {
            player.sendMessage(new TextComponentString("You look around: the village is quiet... too quiet."));
        }

        prog.storyX = cx;
        prog.storyY = cy;
        prog.storyZ = cz;

        // Spawn The Lord at a location in front of the player to block escape
        TheLordEntity lord = new TheLordEntity(world);
        double lx = player.posX + player.getLookVec().x * 5.0D;
        double lz = player.posZ + player.getLookVec().z * 5.0D;
        lord.setPosition(lx, player.posY, lz);
        lord.setCustomNameTag("The Lord");
        lord.enablePersistence();
        lord.setAlwaysRenderNameTag(true);
        world.spawnEntity(lord);
        prog.theLordSpawned = true;

        player.sendMessage(new TextComponentString("A menacing figure steps into your path and blocks all exits.\n'You cannot leave,' a voice rasps. 'Not yet.'"));

        // Save
        prog.markDirty();
        return true;
    }

    public static void resetStory(World world) {
        if (world.isRemote) return;
        StoryProgress prog = getProgress(world);
        if (prog == null) return;
        prog.started = false;
        prog.chapter = 0;
        prog.theLordSpawned = false;
        prog.theDarkSpawned = false;
        prog.markDirty();
    }

}
