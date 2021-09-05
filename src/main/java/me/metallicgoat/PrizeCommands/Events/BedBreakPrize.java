package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.event.arena.ArenaBedBreakEvent;
import me.metallicgoat.PrizeCommands.Main;
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
            String name = p.getName();
            for (String command : plugin.getBedBreakPrize()) {
                if (command != null && !command.equals("")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                }
            }
        }
    }
}