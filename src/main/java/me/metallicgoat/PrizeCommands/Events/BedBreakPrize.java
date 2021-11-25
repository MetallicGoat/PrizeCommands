package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.event.arena.ArenaBedBreakEvent;
import me.metallicgoat.PrizeCommands.Main;
import me.metallicgoat.PrizeCommands.SendBroadcast;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BedBreakPrize implements Listener {
    @EventHandler
    public void onBedDestroy(ArenaBedBreakEvent e){
        Main plugin = Main.getInstance();
        Player p = e.getPlayer();
        if(p != null) {
            SendBroadcast.broadcastBedBreak(e.getArena(), plugin.getConfig().getStringList("bed-break-prize.broadcast"), e);
            String name = p.getName();
            for (String command : plugin.getConfig().getStringList("bed-break-prize.commands")) {
                if (command != null && !command.equals("")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                }
            }
        }
    }
}