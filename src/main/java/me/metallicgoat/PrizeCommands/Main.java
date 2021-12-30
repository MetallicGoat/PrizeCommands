package me.metallicgoat.PrizeCommands;

import me.metallicgoat.PrizeCommands.ConfigUpdater.ConfigUpdater;
import me.metallicgoat.PrizeCommands.Events.*;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

public class Main extends JavaPlugin {

    private static Main instance;

    private final Server server = getServer();

    //First String: permission, 2nd String: Command
    public static HashMap<String, String> bedbreakprizes = new HashMap<String, String>();
    public static HashMap<String, String> endgameprizes = new HashMap<String, String>();
    public static HashMap<String, String> killprizes = new HashMap<String, String>();
    public static HashMap<String, String> playtimeprizes = new HashMap<String, String>();

    public void onEnable() {
        loadConfig();
        registerEvents();
        instance = this;
        PluginDescriptionFile pdf = this.getDescription();

        int pluginId = 11774;
        Metrics metrics = new Metrics(this, pluginId);

        log(
                "------------------------------",
                pdf.getName() + " For MBedwars",
                "By: " + pdf.getAuthors(),
                "Version: " + pdf.getVersion(),
                "------------------------------"
        );

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        killprizes.put("test", "test2");
        killprizes = (HashMap<String, String>) getConfig().get("kill-prize.vip-prizes");
        saveConfig();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        PluginManager manager = this.server.getPluginManager();
        manager.registerEvents(new BedBreakPrize(), this);
        manager.registerEvents(new KillPrize(), this); //Kill & Final Kill
        manager.registerEvents(new PlayTime(), this);
        manager.registerEvents(new EndGame(), this);
    }

    public static Main getInstance() {
        return instance;
    }

    private void loadConfig(){
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", configFile, Collections.singletonList("Nothing"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        reloadConfig();
    }

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }
}