package com.szalainevarganora.minecraftexe.story.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;

import com.szalainevarganora.minecraftexe.story.StoryProgress;

public class CommandMexStatus extends CommandBase {

    @Override
    public String getName() {
        return "mexstatus";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/mexstatus - Show story status";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        WorldServer ws = server.getEntityWorld();
        MapStorage storage = ws.getPerWorldStorage();
        StoryProgress data = (StoryProgress) storage.getOrLoadData(StoryProgress.class, StoryProgress.DATA_NAME);
        if (data == null) {
            sender.sendMessage(new TextComponentString("No story data found."));
            return;
        }
        sender.sendMessage(new TextComponentString("Story started: " + data.started + " | Chapter: " + data.chapter));
    }

}
