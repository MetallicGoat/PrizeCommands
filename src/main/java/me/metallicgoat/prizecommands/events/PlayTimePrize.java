package me.metallicgoat.prizecommands.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import me.metallicgoat.prizecommands.Prize;
import me.metallicgoat.prizecommands.PrizeCommandsPlugin;
import me.metallicgoat.prizecommands.config.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class PlayTimePrize implements Listener {

	private BukkitTask task;

	@EventHandler
	public void onRoundStart(RoundStartEvent e) {
		final Arena arena = e.getArena();

		if (ConfigValue.playTimePrizeEnabled) {
			task = Bukkit.getScheduler().runTaskTimer(PrizeCommandsPlugin.getInstance(), () -> {
				if (arena.getStatus() != ArenaStatus.RUNNING) {
					task.cancel();
					return;
				}

				for (Prize prize : ConfigValue.playTimePrizes) {
					for (Player player : arena.getPlayers())
						prize.earn(arena, player, null);
				}

			}, ConfigValue.playTimeInterval, ConfigValue.playTimeInterval);
		}
	}
}