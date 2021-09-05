package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import me.metallicgoat.PrizeCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class EndGame implements Listener {

    private final HashMap<Arena, Player> playing = new HashMap<>();
    private final HashMap<Arena, Player> clearPlaying = new HashMap<>();

    @EventHandler
    public void onGameStart(RoundStartEvent e){
        Main plugin = Main.getInstance();

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        final Arena arena = e.getArena();
        final long time = plugin.getConfig().getLong("end-game-prizes.minimum-time") * 1200;

        for(Player p: arena.getPlayers()){
            playing.put(arena, p);
        }

        if(time != 0) {
            scheduler.runTaskLater(plugin, () -> {
                if(arena.getStatus() == ArenaStatus.RUNNING) {
                    playing.forEach((arena1, player) -> {
                        if (arena1 == arena) {
                            if (!player.isOnline() || !arena.isInside(player.getLocation())) {
                                clearPlaying.remove(arena, player);
                            }
                        }
                    });
                }
                cleanMaps();
            }, time);
        }
    }

    @EventHandler
    public void onGameEnd(RoundEndEvent e){
        Main plugin = Main.getInstance();

        playing.forEach((arena, player) -> {
            if(arena == e.getArena()){
                String name = player.getName();
                String arenaName = arena.getDisplayName();
                if(e.getWinners().contains(player)){
                    for (String command : plugin.getWinPrize()) {
                        if (command != null && !command.equals("")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("%player%", name)
                                    .replace("%arena-name%", arenaName)
                            );
                        }
                    }
                }else{
                    for (String command : plugin.getLosePrize()) {
                        if (command != null && !command.equals("")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("%player%", name)
                                    .replace("%arena-name%", arenaName)
                            );
                        }
                    }
                }
                clearPlaying.remove(arena, player);
            }
        });
        cleanMaps();
    }
    private void cleanMaps(){
        clearPlaying.forEach((playing::remove));
        clearPlaying.clear();
    }
}
