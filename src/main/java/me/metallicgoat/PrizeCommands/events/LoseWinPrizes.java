package me.metallicgoat.PrizeCommands.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerRejoinArenaEvent;
import me.metallicgoat.PrizeCommands.EarnPrize;
import me.metallicgoat.PrizeCommands.Prize;
import me.metallicgoat.PrizeCommands.config.ConfigValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LoseWinPrizes implements Listener {

    private final HashMap<Arena, Collection<Player>> playing = new HashMap<>();
    private final long time = ConfigValue.minimumPlayTime;

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
            final Collection<Player> activePlayers = playing.get(arena);
            activePlayers.remove(e.getPlayer());
            playing.replace(arena, activePlayers);
        }
    }

    //Add players back if they rejoin
    @EventHandler
    public void onRejoin(PlayerRejoinArenaEvent e){
        final Arena arena = e.getArena();
        if(e.getIssues().isEmpty()){
            final Collection<Player> activePlayers = playing.get(arena);
            activePlayers.add(e.getPlayer());
            playing.replace(arena, activePlayers);
        }
    }

    //Run commands on game end
    @EventHandler
    public void onGameEnd(RoundEndEvent e){
        final Arena arena = e.getArena();
        final Collection<Player> activePlayers = playing.get(arena);

        if(activePlayers != null && time <= arena.getRunningTime()) {

            final HashMap<String, String> placeholderReplacements = new HashMap<>();
            if(e.getWinnerTeam() != null) {
                placeholderReplacements.put("winner-team-name", e.getWinnerTeam().getDisplayName());
                placeholderReplacements.put("winner-team-color", e.getWinnerTeam().name());
                placeholderReplacements.put("winner-team-color-code", "&" + e.getWinnerTeam().getChatColor().getChar());
            }

            for(Player player:activePlayers){

                final List<Prize> endPrize = e.getWinners().contains(player) ? ConfigValue.playerWinPrize : ConfigValue.playerLosePrize;

                for (Prize prize: endPrize) {
                    new EarnPrize(arena, player, prize, placeholderReplacements);
                }
            }
        }
        playing.remove(arena);
    }
}
