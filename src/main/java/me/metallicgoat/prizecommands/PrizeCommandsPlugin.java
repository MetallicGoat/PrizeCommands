package me.metallicgoat.prizecommands;

import me.metallicgoat.prizecommands.events.LoseWinPrizes;
import me.metallicgoat.prizecommands.events.PlayTimePrize;
import me.metallicgoat.prizecommands.events.PlayerConnections;
import me.metallicgoat.prizecommands.events.*;
import me.metallicgoat.prizecommands.config.Config;
import me.metallicgoat.prizecommands.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PrizeCommandsPlugin extends JavaPlugin {

    public static final int MIN_MBEDWARS_API_VER = 15;
    public static final String MIN_MBEDWARS_VER_NAME = "5.1";

    private static PrizeCommandsAddon addon;
    private static PrizeCommandsPlugin instance;

    public void onEnable() {
        instance = this;

        if(!checkMBedwars()) return;
        if(!registerAddon()) return;

        new Metrics(this, 11774);

        registerEvents();
        Config.load();

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
        PluginManager manager = getServer().getPluginManager();
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

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }
}