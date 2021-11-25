package me.metallicgoat.PrizeCommands;

import me.metallicgoat.PrizeCommands.ConfigUpdater.ConfigUpdater;
import me.metallicgoat.PrizeCommands.Events.*;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private final Server server = getServer();
    private final List<String> bedBreakPrize = getConfig().getStringList("bed-break-prize.commands");
    private final List<String> killPrize = getConfig().getStringList("kill-prize.commands");
    private final List<String> finalKillPrize = getConfig().getStringList("final-kill-prize.commands");

    private final List<String> bedBreakPrizeBroadcast = getConfig().getStringList("bed-break-prize.broadcast");
    private final List<String> killPrizeBroadcast = getConfig().getStringList("kill-prize.broadcast");
    private final List<String> finalKillPrizeBroadcast = getConfig().getStringList("final-kill-prize.broadcast");

    private final List<String> winPrize = getConfig().getStringList("end-game-prizes.win-prize");
    private final List<String> losePrize = getConfig().getStringList("end-game-prizes.lose-prize");
    private final List<String> playtimeMessages = getConfig().getStringList("playtime-prize.message");
    private final List<String> playtimeCommands = getConfig().getStringList("playtime-prize.commands");

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

    public ConsoleCommandSender getConsole() {
        return console;
    }

    public List<String> getBedBreakPrize() {
        return bedBreakPrize;
    }

    public List<String> getKillPrize() {
        return killPrize;
    }

    public List<String> getFinalKillPrize() {
        return finalKillPrize;
    }

    public List<String> getBedBreakPrizeBroadcast() {
        return bedBreakPrizeBroadcast;
    }

    public List<String> getKillPrizeBroadcast() {
        return killPrizeBroadcast;
    }

    public List<String> getFinalKillPrizeBroadcast() {
        return finalKillPrizeBroadcast;
    }

    public List<String> getWinPrize() {
        return winPrize;
    }

    public List<String> getLosePrize() {
        return losePrize;
    }

    public List<String> getPlaytimeMessages() {
        return playtimeMessages;
    }

    public List<String> getPlaytimeCommands() {
        return playtimeCommands;
    }

    private void loadConfig(){
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", configFile, Arrays.asList("Nothing", "here"));
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