package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerRejoinArenaEvent;
import me.metallicgoat.PrizeCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.HashMap;

public class EndGame implements Listener {

    private final HashMap<Arena, Collection<Player>> playing = new HashMap<>();
    private final long time = plugin().getConfig().getLong("end-game-prizes.minimum-time") * 1000;

    //Add players to map on round start
    @EventHandler
    public void onGameStart(RoundStartEvent e){
        final Arena arena = e.getArena();
        playing.put(arena, arena.getPlayers());
    }

    //remove player from map on leave if playing less than time
    @EventHandler
    public void onLeaveArena(PlayerQuitArenaEvent e){
        final Arena arena = e.getArena();
        if(arena.getStatus() == ArenaStatus.RUNNING && arena.getRunningTime() <= time){
            Collection<Player> activePlayers = playing.get(arena);
            activePlayers.remove(e.getPlayer());
            playing.replace(arena, activePlayers);
        }
    }

    //Add players back if they rejoin
    @EventHandler
    public void onRejoin(PlayerRejoinArenaEvent e){
        Arena arena = e.getArena();
        if(e.getIssues().isEmpty()){
            Collection<Player> activePlayers = playing.get(arena);
            activePlayers.add(e.getPlayer());
            playing.replace(arena, activePlayers);
        }
    }

    //Run commands on game end
    @EventHandler
    public void onGameEnd(RoundEndEvent e){
        Arena arena = e.getArena();
        Collection<Player> activePlayers = playing.get(arena);
        if(activePlayers != null && time < arena.getRunningTime()) {
            String arenaName = arena.getDisplayName();
            for(Player player:activePlayers){
                String name = player.getName();
                if (e.getWinners().contains(player)) {
                    for (String command : plugin().getConfig().getStringList("end-game-prizes.win-prize")) {
                        if (command != null && !command.equals("")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("%player%", name)
                                    .replace("%arena-name%", arenaName)
                            );
                        }
                    }
                } else {
                    for (String command : plugin().getConfig().getStringList("end-game-prizes.lose-prize")) {
                        if (command != null && !command.equals("")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("%player%", name)
                                    .replace("%arena-name%", arenaName)
                            );
                        }
                    }
                }
            }
        }
        playing.remove(arena);
    }
    private static Main plugin(){
        return Main.getInstance();
    }
}
