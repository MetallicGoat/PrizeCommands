package me.metallicgoat.prizecommands;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.arena.picker.ArenaPickerAPI;
import de.marcely.bedwars.api.exception.ArenaConditionParseException;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import lombok.Getter;
import me.metallicgoat.prizecommands.config.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Prize {

    @Getter private final String prizeId;
    @Getter private final String permission;
    @Getter private final List<String> commands;
    @Getter private final List<String> broadcast;
    @Getter private final List<String> privateMessage;
    private final List<String> supportedArenasNames;

    public Prize(String prizeId,
            String permission,
            List<String> commands,
            List<String> broadcast,
            List<String> privateMessage,
            List<String> supportedArenasNames) {

        this.prizeId = prizeId;
        this.permission = permission;
        this.commands = commands != null ? commands : new ArrayList<>();
        this.broadcast = broadcast != null ? broadcast : new ArrayList<>();
        this.privateMessage = privateMessage != null ? broadcast : new ArrayList<>();
        this.supportedArenasNames = supportedArenasNames;

    }

    public void earn(Arena arena, Player player, HashMap<String, String> placeholderReplacements){
        if(!ConfigValue.enabled)
            return;

        final List<Arena> supportedArenas = this.getSupportedArenas();

        // Only run prize for supported arenas (or all arenas if empty list)
        if(!supportedArenas.isEmpty() && !supportedArenas.contains(arena))
            return;

        if(this.getPermission() != null
                && !this.getPermission().equals("")
                && !player.hasPermission(this.getPermission()))
            return;

        runPrizeCommands(player, arena, this.getCommands(), placeholderReplacements);
        broadcastPrize(player, arena, this.getBroadcast(), placeholderReplacements);
        sendMessage(player, arena, this.getPrivateMessage(), placeholderReplacements);

    }

    private void runPrizeCommands(Player player, Arena arena, List<String> commands, HashMap<String, String> placeholderReplacements){
        for(String cmd : commands)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatString(player, arena, cmd, placeholderReplacements));
    }

    private void broadcastPrize(Player player, Arena arena, List<String> broadcast, HashMap<String, String> placeholderReplacements){
        for(String msg : broadcast)
            arena.broadcast(formatMessage(player, arena, msg, placeholderReplacements));
    }

    private void sendMessage(Player player, Arena arena, List<String> message, HashMap<String, String> placeholderReplacements){
        if(message.size() == 1 && message.get(0).equals(""))
            return;

        for(String msg : message)
            player.sendMessage(formatString(player, arena, msg, placeholderReplacements));
    }

    private String formatString(Player player, Arena arena, String string, HashMap<String, String> placeholderReplacements){
        return formatMessage(player, arena, string, placeholderReplacements).done();
    }


    private Message formatMessage(Player player, Arena arena, String string, HashMap<String, String> placeholderReplacements){
        final Message formattedString = Message.build(string);

        // Placeholder values (Supported by EVERY prize)
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
        return formattedString;
    }

    public List<Arena> getSupportedArenas(){
        final List<Arena> supportedArenas = new ArrayList<>();

        if(supportedArenasNames == null)
            return new ArrayList<>();

        for(String arenaName : supportedArenasNames){
            final Arena arena = GameAPI.get().getArenaByName(arenaName);
            if(arena != null)
                supportedArenas.add(arena);
            else {
                try {
                    final Collection<Arena> arenaList = ArenaPickerAPI.get().getArenasByCondition(arenaName);
                    supportedArenas.addAll(arenaList);
                } catch (ArenaConditionParseException ignored) {

                }
            }
        }

        return supportedArenas;
    }
}
