package me.lightningz.lightningsb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lightningz.lightningsb.commands.testCommand;
import me.lightningz.lightningsb.commands.dungeonCommand;
import me.lightningz.lightningsb.config.LSMConfig;
import me.lightningz.lightningsb.listeners.EventListener;
import me.lightningz.lightningsb.overlays.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Mod(modid = Main.MODID, version = Main.VERSION, clientSideOnly = true)
public class Main
{
    public static final String MODID = "lightningsb";
    public static final String VERSION = "BETA-0.1";
    private EventListener listener;
    public static Main INSTANCE;
    public static final Gson serializeGson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    private File configDir;
    private File configFile;
    public static final Gson gson = new Gson();
    public static String guiToOpen = null;

    public Main() {
        INSTANCE = this;
    }

    private static class WrongForgeVersion extends RuntimeException {
        public WrongForgeVersion(String message) {
            super(message);
        }
    }

    public EventListener getListener() {
        return listener;
    }

    private LSMConfig config;

    public LSMConfig getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            writer.write(serializeGson.toJson(config));
            writer.close();
        } catch (IOException ignored) {}
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (ForgeVersion.getBuildVersion() != 2318)
            throw new WrongForgeVersion("[LSM] Incorrect forge version" + ForgeVersion.getVersion() + ", Please use this mod on 1.8.9 - 11.15.1.2318");
        configDir = new File(event.getModConfigurationDirectory(), "lsm");
        getConfigDir().mkdirs();
        configFile = new File(getConfigDir(), "config.json");
        loadConfig();
        listener = new EventListener();
        MinecraftForge.EVENT_BUS.register(listener);
        ClientCommandHandler.instance.registerCommand(new testCommand());
        ClientCommandHandler.instance.registerCommand(new dungeonCommand());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }



    public File getConfigDir() {
        return configDir;
    }

    public void loadConfig() {
        if (configFile.exists())
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(configFile.toPath()), StandardCharsets.UTF_8)) {
                config = gson.fromJson(reader, LSMConfig.class);
                config.processConfig();
            } catch (IOException ignored) {}
        if (config == null) {
            config = new LSMConfig();
            config.processConfig();
            saveConfig();
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (guiToOpen != null) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new Overlay());
            if ("displaygui".equals(guiToOpen)) {
                mc.displayGuiScreen(new Overlay());
            }
            guiToOpen = null;
        }
    }

}
