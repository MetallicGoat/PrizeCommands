package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import me.metallicgoat.PrizeCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class PlayTime implements Listener {

    private BukkitTask task;

    @EventHandler
    public void gameStart(RoundStartEvent e){
        Main plugin = Main.getInstance();
        Arena a = e.getArena();
        if(plugin.getConfig().getBoolean("playtime-prize.enabled")) {
            long time = plugin.getConfig().getLong("playtime-prize.interval");
            task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if(a.getStatus() == ArenaStatus.RUNNING) {
                    for(Player p:a.getPlayers()){
                        String name = p.getName();
                        for (String command : plugin.getConfig().getStringList("playtime-prize.commands")) {
                            if (command != null && !command.equals("")) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                            }
                        }
                        for (String msg : plugin.getConfig().getStringList("playtime-prize.message")) {
                            String translatedMessage = msg.replace("%player%", name);
                            String formattedMessage = ChatColor.translateAlternateColorCodes('&', translatedMessage);
                            p.sendMessage(formattedMessage);
                        }
                    }
                }else{
                    task.cancel();
                }
            }, time, time);
        }
    }
}
