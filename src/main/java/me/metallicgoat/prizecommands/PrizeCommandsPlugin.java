package me.metallicgoat.prizecommands;

import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.prizecommands.events.LoseWinPrizes;
import me.metallicgoat.prizecommands.events.PlayTimePrize;
import me.metallicgoat.prizecommands.events.PlayerConnections;
import me.metallicgoat.prizecommands.events.*;
import me.metallicgoat.prizecommands.config.Config;
import me.metallicgoat.prizecommands.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrizeCommandsPlugin extends JavaPlugin {

    public static final int MIN_MBEDWARS_API_VER = 15;
    public static final String MIN_MBEDWARS_VER_NAME = "5.1";

    private static PrizeCommandsAddon addon;
    private static PrizeCommandsPlugin instance;
    private final Server server = getServer();

    public void onEnable() {
        instance = this;

        if(!checkMBedwars()) return;
        if(!registerAddon()) return;

        new Metrics(this, 11774);

        registerEvents();
        Config.save();

        final PluginDescriptionFile pdf = this.getDescription();

        log(
                "------------------------------",
                pdf.getName() + " For MBedwars",
                "By: " + pdf.getAuthors(),
                "Version: " + pdf.getVersion(),
                "------------------------------"
        );
    }

    private void registerEvents() {
        PluginManager manager = this.server.getPluginManager();
        manager.registerEvents(new PlayerBreakBedPrize(), this);
        manager.registerEvents(new PlayerKillPrize(), this); // Kill & Final Kill
        manager.registerEvents(new PlayTimePrize(), this);
        manager.registerEvents(new LoseWinPrizes(), this);
        manager.registerEvents(new PlayerConnections(), this); // Join & Leave & Rejoin
        manager.registerEvents(new AchievementEarnPrize(), this);
    }

    public static PrizeCommandsPlugin getInstance() {
        return instance;
    }

    public static PrizeCommandsAddon getAddon() {
        return addon;
    }

    private boolean checkMBedwars(){
        try{
            final Class<?> apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
            final int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

            if(apiVersion < MIN_MBEDWARS_API_VER)
                throw new IllegalStateException();
        }catch(Exception e){
            getLogger().warning("Sorry, your installed version of MBedwars is not supported. Please install at least v" + MIN_MBEDWARS_VER_NAME);
            Bukkit.getPluginManager().disablePlugin(this);

            return false;
        }

        return true;
    }

    private boolean registerAddon(){
        addon = new PrizeCommandsAddon(this);

        if(!addon.register()){
            getLogger().warning("It seems like this addon has already been loaded. Please delete duplicates and try again.");
            Bukkit.getPluginManager().disablePlugin(this);

            return false;
        }

        return true;
    }

    public boolean copyResource(String internalPath, File out) throws IOException {
        if(!out.exists() || out.length() == 0){
            try(InputStream is = getResource(internalPath)){
                if(is == null){
                    getLogger().warning("Your plugin seems to be broken (Failed to find internal file " + internalPath + ")");
                    return false;
                }

                out.createNewFile();

                try(FileOutputStream os = new FileOutputStream(out)){
                    Helper.get().copy(is, os);
                }

                return true;
            }
        }

        return false;
    }

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }
}