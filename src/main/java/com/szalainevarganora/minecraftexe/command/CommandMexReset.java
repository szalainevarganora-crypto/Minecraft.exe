package com.szalainevarganora.minecraftexe.command;

import com.szalainevarganora.minecraftexe.story.StoryManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandMexReset extends CommandBase {
    @Override
    public String getName() {
        return "mexreset";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/mexreset - Reset the Minecraft.exe story state";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            StoryManager.resetStory(player.getEntityWorld());
            player.sendMessage(new net.minecraft.util.text.TextComponentString("Story reset."));
        } else {
            sender.sendMessage(new net.minecraft.util.text.TextComponentString("World story reset."));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
