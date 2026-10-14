package hu.szalainevarganora.minecraftexe.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandHello extends CommandBase {

    @Override
    public String getName() {
        return "sayhi"; // /sayhi [name]
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/sayhi [name] - üdvözli a megadott nevet vagy a parancs kiadóját";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String name = sender.getName();
        if (args.length > 0) {
            name = args[0];
        }
        sender.sendMessage(new TextComponentString("Szia, " + name + "! \uD83D\uDC4B"));
    }

    @Override
    public int getRequiredPermissionLevel() {
        // Parancsblokkok alapból engedélyezettek; op jogkör szintje 2.
        return 2;
    }
}
