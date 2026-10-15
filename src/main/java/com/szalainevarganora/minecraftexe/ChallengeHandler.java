package com.szalainevarganora.minecraftexe;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChallengeHandler {
    private static final Random RAND = new Random();
    private static final String[] WORDS = new String[]{"alma", "korte", "sarkany", "minecraft", "kod", "varazs", "suly", "feny", "zene"};
    private static String currentWord = null;

    // runtime alignment map (player UUID -> "good"/"evil"/"neutral")
    private static final Map<UUID, String> ALIGNMENTS = new ConcurrentHashMap<>();

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

    private static String findNearestAlignmentNpc(EntityPlayerMP player, double radius) {
        List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
                new AxisAlignedBB(player.posX - radius, player.posY - radius, player.posZ - radius,
                        player.posX + radius, player.posY + radius, player.posZ + radius));

        double bestDist = Double.MAX_VALUE;
        String result = null;
        for (EntityLivingBase e : entities) {
            if (!e.hasCustomName()) continue;
            String name = e.getCustomNameTag().toLowerCase();
            double dist = player.getDistanceSq(e);
            if (dist >= bestDist) continue;

            if (name.contains("gonosz") || name.contains("evil") || name.contains("align:evil")) {
                bestDist = dist;
                result = "evil";
            } else if (name.contains("jó") || name.contains("jo") || name.contains("good") || name.contains("align:good")) {
                bestDist = dist;
                result = "good";
            } else if (name.contains("semleges") || name.contains("neutral") || name.contains("align:neutral")) {
                bestDist = dist;
                result = "neutral";
            }
        }
        return result;
    }

    private static String getAlignment(EntityPlayerMP player) {
        UUID id = player.getUniqueID();
        if (ALIGNMENTS.containsKey(id)) return ALIGNMENTS.get(id);

        // Try to determine alignment from nearby NPCs within 8 blocks
        String found = findNearestAlignmentNpc(player, 8.0);
        if (found != null) {
            ALIGNMENTS.put(id, found);
            return found;
        }

        // fallback: neutral
        ALIGNMENTS.put(id, "neutral");
        return "neutral";
    }

    private static void setPlayerAlignment(EntityPlayerMP player, String alignment) {
        if (alignment == null) alignment = "neutral";
        ALIGNMENTS.put(player.getUniqueID(), alignment);
        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Beállítva az alignmented: " + TextFormatting.DARK_AQUA + alignment));
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
                player.world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.0f, pitch);
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

        // For each online player, determine alignment (NPC-based if available) and play accompaniment
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

    /**
     * Right-click an NPC with a name containing "align:evil" / "align:good" / "align:neutral"
     * to set your alignment. Useful if you want NPCs to assign alignment.
     */
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getEntityPlayer() instanceof EntityPlayerMP)) return;
        if (!(event.getTarget() instanceof EntityLivingBase)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
        EntityLivingBase target = (EntityLivingBase) event.getTarget();

        if (!target.hasCustomName()) return;
        String name = target.getCustomNameTag().toLowerCase();
        if (name.contains("align:evil") || name.contains("gonosz") || name.contains("evil")) {
            setPlayerAlignment(player, "evil");
            playAccompanimentForPlayer(player, "evil");
            event.setCanceled(true);
        } else if (name.contains("align:good") || name.contains("jó") || name.contains("jo") || name.contains("good")) {
            setPlayerAlignment(player, "good");
            playAccompanimentForPlayer(player, "good");
            event.setCanceled(true);
        } else if (name.contains("align:neutral") || name.contains("semleges") || name.contains("neutral")) {
            setPlayerAlignment(player, "neutral");
            playAccompanimentForPlayer(player, "neutral");
            event.setCanceled(true);
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
