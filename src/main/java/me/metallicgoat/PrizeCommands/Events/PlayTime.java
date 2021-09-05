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

public class PlayTime implements Listener {

    @EventHandler
    public void gameStart(RoundStartEvent e){
        Main plugin = Main.getInstance();
        Arena a = e.getArena();
        if(plugin.getConfig().getBoolean("playtime-prize.enabled")) {
            schedulePrize(plugin, a);
        }
    }

    private void schedulePrize(Main plugin, Arena a){
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        long time = plugin.getConfig().getLong("playtime-prize.interval");
        scheduler.scheduleSyncDelayedTask(plugin, () -> givePrize(plugin, a), time);
    }

    private void givePrize(Main plugin, Arena a){
        if(a.getStatus() == ArenaStatus.RUNNING) {
            for(Player p:a.getPlayers()){
                String name = p.getName();
                for (String command : plugin.getPlaytimeCommands()) {
                    if (command != null && !command.equals("")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                    }
                }
                for (String msg : plugin.getPlaytimeMessages()) {
                    String translatedMessage = msg.replace("%player%", name);
                    String formattedMessage = ChatColor.translateAlternateColorCodes('&', translatedMessage);
                    p.sendMessage(formattedMessage);
                }
            }
            schedulePrize(plugin, a);
        }
    }
}
