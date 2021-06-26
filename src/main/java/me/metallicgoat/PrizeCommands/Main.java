package me.metallicgoat.PrizeCommands;

import me.metallicgoat.PrizeCommands.Events.BedBreakPrize;
import me.metallicgoat.PrizeCommands.Events.KillPrize;
import me.metallicgoat.PrizeCommands.Events.StartMessage;
import me.metallicgoat.PrizeCommands.Events.Winners;
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
    private final List<String> bedBreakPrize = getConfig().getStringList("bed-break-prize");
    private final List<String> killPrize = getConfig().getStringList("kill-prize");
    private final List<String> finalKillPrize = getConfig().getStringList("final-kill-prize");
    private final List<String> winPrize = getConfig().getStringList("win-prize");
    private final List<String> losePrize = getConfig().getStringList("lose-prize");
    private final List<String> startMessage = getConfig().getStringList("start-message.enabled");

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
        manager.registerEvents(new StartMessage(), this);
        manager.registerEvents(new Winners(), this);
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

    public List<String> getWinPrize() {
        return winPrize;
    }

    public List<String> getLosePrize() {
        return losePrize;
    }

    public List<String> getStartMessage() {
        return startMessage;
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