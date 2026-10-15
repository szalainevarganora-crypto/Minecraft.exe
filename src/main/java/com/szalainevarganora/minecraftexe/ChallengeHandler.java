package com.szalainevarganora.minecraftexe;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.List;
import java.util.Random;

public class ChallengeHandler {
    private static final Random RAND = new Random();
    private static final String[] WORDS = new String[]{"alma", "korte", "sarkany", "minecraft", "kod", "varazs", "suly", "feny", "zene"};
    private static String currentWord = null;

    public ChallengeHandler() {
        // ensure there's a word ready when the handler is registered
        generateNewWord();
    }

    private static void generateNewWord() {
        currentWord = WORDS[RAND.nextInt(WORDS.length)];
        broadcastMessage(TextFormatting.GREEN + "Type this word to continue: " + TextFormatting.YELLOW + currentWord);
    }

    private static void broadcastMessage(String message) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) return;

        List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
        for (EntityPlayerMP p : players) {
            p.sendMessage(new TextComponentString(message));
        }
    }

    private static String getAlignment(EntityPlayerMP player) {
        if (player.getTeam() != null) {
            String team = player.getTeam().getRegisteredName();
            if ("evil".equalsIgnoreCase(team) || "gonosz".equalsIgnoreCase(team)) return "evil";
            if ("good".equalsIgnoreCase(team) || "jo".equalsIgnoreCase(team) || "jó".equalsIgnoreCase(team)) return "good";
        }
        // default neutral
        return "neutral";
    }

    private static void playAccompanimentForPlayer(EntityPlayerMP player, String alignment) {
        try {
            String soundName = "note.harp";
            float pitch = 1.0f;
            String accompanimentDesc = "semleges dallam";

            if ("evil".equals(alignment)) {
                // evil: organ-like accompaniment (use bassattack as heavier tone)
                soundName = "note.bassattack";
                pitch = 0.6f;
                accompanimentDesc = "orgona kíséret";
            } else if ("good".equals(alignment)) {
                soundName = "note.harp";
                pitch = 1.4f;
                accompanimentDesc = "angyali kórus kíséret";
            } else {
                soundName = "note.hat";
                pitch = 1.0f;
                accompanimentDesc = "semleges dallam";
            }

            SoundEvent sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName));
            if (sound != null) {
                player.world.playSound(player, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.0f, pitch);
            }

            // Inform the player about the accompaniment
            player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Te " + ("evil".equals(alignment) ? "gonoszként" : ("good".equals(alignment) ? "jóként" : "semlegesként")) + " énekelsz: " + TextFormatting.DARK_AQUA + accompanimentDesc));

        } catch (Exception e) {
            // fail silently if sound can't be played
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        // Everyone "sings" when a player joins: broadcast a message
        broadcastMessage(TextFormatting.LIGHT_PURPLE + player.getName() + " elkezd énekelni: Lalala...");
        broadcastMessage(TextFormatting.LIGHT_PURPLE + "Mindenki énekel: Lalala... Lalala...");

        // For each online player, play accompaniment according to their alignment
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) return;
        List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
        for (EntityPlayerMP p : players) {
            String alignment = getAlignment(p);
            playAccompanimentForPlayer(p, alignment);
        }

        // Remind the joining player of the challenge word
        if (currentWord != null) {
            player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Type this word to continue: " + TextFormatting.YELLOW + currentWord));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerChat(ServerChatEvent event) {
        if (currentWord == null) return;
        String msg = event.getMessage().trim();
        if (msg.equalsIgnoreCase(currentWord)) {
            // Correct word: acknowledge, generate a new word and block the revealed word from normal chat
            EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
            player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Helyes! Következő szó készül..."));
            generateNewWord();
            event.setCanceled(true);
        } else {
            // Block the chat message and remind the player of the required word
            EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
            player.sendMessage(new TextComponentString(TextFormatting.RED + "Rossz szó — írd be pontosan: " + TextFormatting.YELLOW + currentWord));
            event.setCanceled(true);
        }
    }
}
