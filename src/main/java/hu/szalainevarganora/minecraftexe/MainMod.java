package hu.szalainevarganora.minecraftexe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MainMod.MODID, name = MainMod.NAME, version = MainMod.VERSION)
public class MainMod {
    public static final String MODID = "minecraftexe";
    public static final String NAME = "Minecraft mód";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        // Regisztráljuk a parancsot, így parancsblokk-ból is használható lesz
        event.registerServerCommand(new hu.szalainevarganora.minecraftexe.command.CommandHello());
    }
}
