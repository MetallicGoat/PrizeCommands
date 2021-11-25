package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import me.metallicgoat.PrizeCommands.Main;
import me.metallicgoat.PrizeCommands.SendBroadcast;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillPrize implements Listener {

    @EventHandler
    public void onFinalKill(PlayerDeathEvent e){
        Main plugin = Main.getInstance();
        Player v = e.getEntity();
        String vname = v.getName();
        Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(v);
        if (arena != null){
            Team team = arena.getPlayerTeam(v);
            if(e.getEntity().getKiller() != null){
                Player p = e.getEntity().getKiller();
                String name = p.getName();
                if(arena.isBedDestroyed(team)) {
                    SendBroadcast.broadcastKill(arena, plugin.getConfig().getStringList("final-kill-prize.broadcast"), e);
                    for (String command : plugin.getConfig().getStringList("final-kill-prize.commands")) {
                        if (command != null && !command.equals("")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("%victim%", vname)
                                    .replace("%player%", name));
                        }
                    }
                }else if(!arena.isBedDestroyed(team)){
                    SendBroadcast.broadcastKill(arena, plugin.getConfig().getStringList("kill-prize.broadcast"), e);
                    for (String command : plugin.getConfig().getStringList("final-kill-prize.commands")) {
                        if (command != null && !command.equals("")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("%victim%", vname)
                                    .replace("%player%", name));
                        }
                    }
                }
            }
        }
    }
}