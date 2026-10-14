package com.szalainevarganora.minecraftexe.story.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import com.szalainevarganora.minecraftexe.story.StoryManager;

public class CommandMexReset extends CommandBase {

    @Override
    public String getName() {
        return "mexreset";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/mexreset - Reset story state for this world";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        StoryManager.resetStory(server.getEntityWorld());
        sender.sendMessage(new TextComponentString("Story state reset."));
    }

}
