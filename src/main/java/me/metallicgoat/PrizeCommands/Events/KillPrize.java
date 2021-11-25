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

import java.util.List;

public class KillPrize implements Listener {

    @EventHandler
    public void onFinalKill(PlayerDeathEvent e){
        Main plugin = Main.getInstance();
        Player victim = e.getEntity();
        Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(victim);
        if (arena != null && e.getEntity().getKiller() != null){
            Player killer = e.getEntity().getKiller();
            Team team = arena.getPlayerTeam(victim);
            Team killerTeam = arena.getPlayerTeam(killer);
            List<String> killPrize = arena.isBedDestroyed(team) ? plugin.getConfig().getStringList("final-kill-prize.commands"):plugin.getConfig().getStringList("kill-prize.commands");
            List<String> killBroadcast = arena.isBedDestroyed(team) ? plugin.getConfig().getStringList("final-kill-prize.broadcast"):plugin.getConfig().getStringList("kill-prize.broadcast");

            SendBroadcast.broadcastKill(arena, killBroadcast, killer, victim, killerTeam ,team);
            for (String command : killPrize) {
                if (command != null && !command.equals("")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                            .replace("%victim%", victim.getName())
                            .replace("%player%", killer.getName()));
                }
            }
        }
    }
}
