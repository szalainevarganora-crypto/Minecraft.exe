package com.szalainevarganora.minecraftexe.story.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import com.szalainevarganora.minecraftexe.story.StoryManager;

public class CommandMexStart extends CommandBase {

    @Override
    public String getName() {
        return "mexstart";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/mexstart - Start the Minecraft.exe story (server-side)";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean ok = StoryManager.startStory(player);
            if (ok) {
                sender.sendMessage(new TextComponentString("Story started."));
            }
        } else {
            sender.sendMessage(new TextComponentString("This command must be run by a player."));
        }
    }

}
