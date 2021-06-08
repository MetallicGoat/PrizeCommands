package me.metallicgoat.PrizeCommands;

import me.metallicgoat.PrizeCommands.Events.BedBreakPrize;
import me.metallicgoat.PrizeCommands.Events.KillPrize;
import me.metallicgoat.PrizeCommands.Events.StartMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private final Server server = getServer();
    private final List<String> bedBreakPrize = getConfig().getStringList("bed-break-prize");
    private final List<String> killPrize = getConfig().getStringList("kill-prize");
    private final List<String> finalKillPrize = getConfig().getStringList("final-kill-prize");
    private final List<String> startMessage = getConfig().getStringList("start-message.enabled");

    public void onEnable() {
        registerEvents();
        instance = this;
        PluginDescriptionFile pdf = this.getDescription();

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

    public List<String> getStartMessage() {
        return startMessage;
    }

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }
}