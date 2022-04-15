package me.metallicgoat.PrizeCommands;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class EarnPrize {

    // Extra placeholders that may be wanted for a certain prize
    private final HashMap<String, String> placeholderReplacements;

    public EarnPrize(Arena arena, Player player, Prize prize, HashMap<String, String> placeholderReplacements){
        this.placeholderReplacements = placeholderReplacements;

        if(!prize.supportedArenas.isEmpty() && !prize.supportedArenas.contains(arena))
            return;

        if(prize.getPermission() != null
                && !prize.getPermission().equals("")
                && !player.hasPermission(prize.getPermission()))
            return;

        runPrizeCommands(player, arena,prize.getCommands());
        broadcastPrize(player, arena, prize.getBroadcast());
        sendMessage(player, arena, prize.getPrivateMessage());

    }

    private void runPrizeCommands(Player player, Arena arena, List<String> commands){
        for(String cmd : commands)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatString(player, arena, cmd));
    }

    private void broadcastPrize(Player player, Arena arena, List<String> broadcast){
        if(broadcast.size() == 1 && broadcast.get(0).equals(""))
            return;

        for(String msg : broadcast)
            arena.broadcast(formatString(player, arena, msg));
    }

    private void sendMessage(Player player, Arena arena, List<String> message){
        if(message.size() == 1 && message.get(0).equals(""))
            return;

        for(String msg : message)
            player.sendMessage(formatString(player, arena, msg));
    }


    private String formatString(Player player, Arena arena, String string){
        final Message formattedString = Message.build(string);

        // Placeholder values
        final Team team = arena.getPlayerTeam(player);
        final String teamName = team != null ? team.getDisplayName() : "";
        final String teamColor = team != null ? team.name() : "";
        final String teamColorCode = team != null ? "&" + team.getChatColor().getChar() : "";
        final String arenaName = arena.getDisplayName();
        final String arenaWorld = arena.getGameWorld() != null ? arena.getGameWorld().getName() : "";
        final String playerRealName = player.getName();
        final String playerDisplayName = Helper.get().getPlayerDisplayName(player);
        final String playerX = String.valueOf(player.getLocation().getX());
        final String playerY = String.valueOf(player.getLocation().getY());
        final String playerZ = String.valueOf(player.getLocation().getZ());

        formattedString
                .placeholder("team-name", teamName)
                .placeholder("team-color", teamColor)
                .placeholder("team-color-code", teamColorCode)
                .placeholder("arena-name", arenaName)
                .placeholder("arena-world", arenaWorld)
                .placeholder("player-real-name", playerRealName)
                .placeholder("player-display-name", playerDisplayName)
                .placeholder("player-x", playerX)
                .placeholder("player-y", playerY)
                .placeholder("player-z", playerZ);

        // Translate event specific placeholders
        if(placeholderReplacements != null) {
            for (Map.Entry<String, String> stringSet : placeholderReplacements.entrySet()) {
                formattedString.placeholder(stringSet.getKey(), stringSet.getValue());
            }
        }

        return formattedString.done();
    }
}
