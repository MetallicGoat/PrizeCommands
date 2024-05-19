package me.metallicgoat.prizecommands;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.arena.picker.ArenaPickerAPI;
import de.marcely.bedwars.api.exception.ArenaConditionParseException;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.prizecommands.config.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Prize {

  public final String prizeId;
  public final String permission;
  public final List<String> commands;
  public final List<String> playerCommands;
  public final List<String> broadcast;
  public final List<String> privateMessage;
  public final boolean enabled;
  public final List<String> supportedArenasNames;

  public Prize(String prizeId,
               String permission,
               @Nullable List<String> commands,
               @Nullable List<String> playerCommands,
               @Nullable List<String> broadcast,
               @Nullable List<String> privateMessage,
               @Nullable List<String> supportedArenasNames,
               boolean enabled) {

    this.prizeId = prizeId;
    this.permission = permission;
    this.commands = commands;
    this.playerCommands = playerCommands;
    this.broadcast = broadcast;
    this.privateMessage = privateMessage;
    this.enabled = enabled;
    this.supportedArenasNames = supportedArenasNames;
  }

  public void earn(Arena arena, Player player, Map<String, String> placeholderReplacements) {
    if (Bukkit.isPrimaryThread()) {
      earnUnsafe(arena, player, placeholderReplacements);
    } else {
      Bukkit.getScheduler().runTask(PrizeCommandsPlugin.getInstance(), () ->
          earnUnsafe(arena, player, placeholderReplacements)
      );
    }
  }

  private void earnUnsafe(Arena arena, Player player, Map<String, String> placeholderReplacements) {
    if (!ConfigValue.enabled)
      return;

    final List<Arena> supportedArenas = this.getSupportedArenas();

    // Only run prize for supported arenas (or all arenas if empty list)
    if (!supportedArenas.isEmpty() && !supportedArenas.contains(arena))
      return;

    if (this.permission != null
        && !this.permission.isEmpty()
        && !player.hasPermission(permission))
      return;

    if (commands != null) {
      for (String cmd : commands)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatString(player, arena, cmd, placeholderReplacements));
    }

    if (playerCommands != null) {
      for (String cmd : playerCommands)
        player.performCommand(formatString(player, arena, cmd, placeholderReplacements));
    }

    if (broadcast != null) {
      for (String msg : broadcast)
        arena.broadcast(formatMessage(player, arena, msg, placeholderReplacements));
    }

    if (privateMessage != null) {
      for (String msg : privateMessage)
        player.sendMessage(formatString(player, arena, msg, placeholderReplacements));
    }
  }

  private String formatString(Player player, Arena arena, String string, Map<String, String> placeholderReplacements) {
    return formatMessage(player, arena, string, placeholderReplacements).done();
  }


  private Message formatMessage(Player player, Arena arena, String string, Map<String, String> placeholderReplacements) {
    final Message formattedString = Message.build(string);

    // Placeholder values (Supported by EVERY prize)
    final Team team = arena.getPlayerTeam(player);
    final String teamName = team != null ? team.getDisplayName() : "";
    final String teamColor = team != null ? team.name() : "";
    final String teamColorCode = team != null ? team.getBungeeChatColor().toString() : "";
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
    if (placeholderReplacements != null) {
      for (Map.Entry<String, String> stringSet : placeholderReplacements.entrySet())
        formattedString.placeholder(stringSet.getKey(), stringSet.getValue());
    }

    return formattedString;
  }

  public List<Arena> getSupportedArenas() {
    final List<Arena> supportedArenas = new ArrayList<>();

    if (supportedArenasNames == null)
      return new ArrayList<>();

    for (String arenaName : supportedArenasNames) {
      final Arena arena = GameAPI.get().getArenaByName(arenaName);

      if (arena != null) {
        supportedArenas.add(arena);
        continue;
      }

      try {
        final Collection<Arena> arenaList = ArenaPickerAPI.get().getArenasByCondition(arenaName);
        supportedArenas.addAll(arenaList);

      } catch (ArenaConditionParseException ignored) {

      }
    }

    return supportedArenas;
  }
}
