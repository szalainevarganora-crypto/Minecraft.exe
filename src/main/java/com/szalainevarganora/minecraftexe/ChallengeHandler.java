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

import java.util.List;
import java.util.Random;

public class ChallengeHandler {
    private static final Random RAND = new Random();
    private static final String[] WORDS = new String[]{"alma", "korte", "sarkany", "minecraft", "kod", "varazs", "suly", "fény", "zene"};
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

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        // Everyone "sings" when a player joins
        broadcastMessage(TextFormatting.LIGHT_PURPLE + player.getName() + " elkezd énekelni: Lalala...");
        broadcastMessage(TextFormatting.LIGHT_PURPLE + "Mindenki énekel: Lalala... Lalala...");
        // Remind the challenge word on join
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
