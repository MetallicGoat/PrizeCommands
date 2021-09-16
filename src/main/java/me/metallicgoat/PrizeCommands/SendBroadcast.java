package me.metallicgoat.PrizeCommands;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.ArenaBedBreakEvent;
import de.marcely.bedwars.api.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class SendBroadcast {
    public static void broadcastBedBreak(Arena arena, List<String> message, ArenaBedBreakEvent e){
        Player p = e.getPlayer();
        if(message != null) {
            if(message.size() == 1){
                if (message.get(0).equals("")) {
                    message.remove(0);
                }
            }
            message.forEach(s -> {
                if (s != null) {
                    assert p != null;
                    arena.broadcast(Message.build(s).placeholder("player", p.getDisplayName())
                            .placeholder("player-team", arena.getPlayerTeam(p).getDisplayName())
                            .placeholder("victim-team", e.getTeam().getDisplayName()));
                }
            });
        }
    }
    public static void broadcastKill(Arena arena, List<String> message, PlayerDeathEvent e){
        Player p = e.getEntity().getPlayer();
        Player k = e.getEntity().getKiller();
        if(message != null) {
            if(message.size() == 1){
                if (message.get(0).equals("")) {
                    message.remove(0);
                }
            }
            message.forEach(s -> {
                if (s != null) {
                    assert p != null;
                    arena.broadcast(Message.build(s).placeholder("killer", p.getDisplayName())
                            .placeholder("victim", p.getDisplayName())
                            .placeholder("killler-team", arena.getPlayerTeam(k).getDisplayName())
                            .placeholder("victim-team", arena.getPlayerTeam(p).getDisplayName()));
                }
            });
        }
    }
}
