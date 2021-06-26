package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import me.metallicgoat.PrizeCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Winners implements Listener {
    @EventHandler
    public void onGameOver(RoundEndEvent e){
        Main plugin = Main.getInstance();
        Arena arena = e.getArena();
        if(!e.getWinners().isEmpty()){
            for(Player p:arena.getPlayers()){
                String name = p.getName();
                if(e.getWinners().contains(p)){
                    for (String command : plugin.getWinPrize()) {
                        if (command != null) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                        }
                    }
                }else{
                    for (String command : plugin.getLosePrize()) {
                        if (command != null) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                        }
                    }
                }
            }
        }
    }
}
