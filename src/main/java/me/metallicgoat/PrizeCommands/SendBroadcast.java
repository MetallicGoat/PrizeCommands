package me.metallicgoat.PrizeCommands;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class SendBroadcast {
    public static void broadcastKill(Arena arena, List<String> message, Player killer, Player victim, Team killerTeam, Team team){
        if(message != null) {
            if(message.size() == 1){
                if (message.get(0).equals("")) {
                    message.remove(0);
                }
            }
            for(String s:message){
                if (s != null) {
                    arena.broadcast(Message.build(s).placeholder("killer", killer.getDisplayName())
                            .placeholder("victim", victim.getDisplayName())
                            .placeholder("killer-team", "&" + killerTeam.getChatColor().getChar() + killerTeam.getDisplayName())
                            .placeholder("victim-team", "&" + team.getChatColor().getChar() + team.getDisplayName()));
                }
            }
        }
    }
}
