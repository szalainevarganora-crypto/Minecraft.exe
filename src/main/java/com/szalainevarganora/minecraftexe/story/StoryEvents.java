package com.szalainevarganora.minecraftexe.story;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

import com.szalainevarganora.minecraftexe.story.StoryProgress;
import com.szalainevarganora.minecraftexe.story.StoryManager;
import com.szalainevarganora.minecraftexe.entities.TheDarkEntity;

/**
 * Handles periodic story checks and player arrival events.
 */
public class StoryEvents {

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player.world.isRemote) return;
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        StoryProgress prog = StoryManager.getProgress(player.world);
        if (prog != null && prog.started) {
            player.sendMessage(new TextComponentString("You feel a chill as you return to the world. The story continues."));
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.player.world.isRemote) return;
        if (!(event.player instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        StoryProgress prog = StoryManager.getProgress(player.world);
        if (prog == null || !prog.started) return;

        // If player moves far from the story center, consider prologue complete
        double dx = player.posX - prog.storyX;
        double dz = player.posZ - prog.storyZ;
        double distSq = dx * dx + dz * dz;
        if (prog.chapter == 0 && distSq > 1600) { // 40 blocks away
            prog.chapter = 1;
            prog.markDirty();
            player.sendMessage(new TextComponentString("You manage to slip away from the village as the shadows close behind you. Chapter 1 begins."));
        }

        // Spawn The Dark Entity once after chapter 1 starts
        if (prog.chapter >= 1 && !prog.theDarkSpawned) {
            // spawn near player but not on top
            TheDarkEntity dark = new TheDarkEntity(player.world);
            dark.setPosition(player.posX + 8.0D, player.posY, player.posZ + 8.0D);
            dark.setCustomNameTag("The Dark Entity");
            player.world.spawnEntity(dark);
            prog.theDarkSpawned = true;
            prog.markDirty();
            player.sendMessage(new TextComponentString("Something corrupt stirs in the distance..."));
        }
    }
}
