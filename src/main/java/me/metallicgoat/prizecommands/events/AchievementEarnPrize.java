package me.metallicgoat.prizecommands.events;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.player.PlayerEarnAchievementEvent;
import me.metallicgoat.prizecommands.Prize;
import me.metallicgoat.prizecommands.config.ConfigValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AchievementEarnPrize implements Listener {

  @EventHandler
  public void onPlayerEarnAchievement(PlayerEarnAchievementEvent event) {
    final Player player = event.getPlayer();
    final Arena arena = GameAPI.get().getArenaByPlayer(player);

    if (arena == null)
      return;

    for (Prize prize : ConfigValue.playerLeaveArenaPrize)
      prize.earn(arena, player, null);
  }
}
