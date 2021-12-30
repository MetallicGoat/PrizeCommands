package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.PrizeCommands.Main;
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

            broadcastKill(arena, killBroadcast, killer, victim, killerTeam ,team);
            for (String command : killPrize) {
                if (command != null && !command.equals("")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Message.build(command)
                            .placeholder("killer", killer.getName())
                            .placeholder("victim", victim.getName())
                            .done());
                }
            }
        }
    }
    private static void broadcastKill(Arena arena, List<String> message, Player killer, Player victim, Team killerTeam, Team team){
        if(message != null) {
            if(message.size() == 1){
                if (message.get(0).equals("")) {
                    return;
                }
            }
            for(String s:message){
                if (s != null) {
                    arena.broadcast(Message.build(s).placeholder("killer", BedwarsAPI.getHelper().getPlayerDisplayName(killer))
                            .placeholder("victim", BedwarsAPI.getHelper().getPlayerDisplayName(victim))
                            .placeholder("killer-team", "&" + killerTeam.getChatColor().getChar() + killerTeam.getDisplayName())
                            .placeholder("victim-team", "&" + team.getChatColor().getChar() + team.getDisplayName()));
                }
            }
        }
    }
}
