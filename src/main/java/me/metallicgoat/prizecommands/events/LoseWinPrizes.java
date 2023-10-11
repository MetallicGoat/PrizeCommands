package me.metallicgoat.prizecommands.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerRejoinArenaEvent;
import me.metallicgoat.prizecommands.Prize;
import me.metallicgoat.prizecommands.config.ConfigValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LoseWinPrizes implements Listener {

	private final HashMap<Arena, List<Player>> playing = new HashMap<>();
	private final long time = ConfigValue.minimumPlayTime;

	// Add players to map on round start
	@EventHandler
	public void onGameStart(RoundStartEvent e) {
		playing.put(e.getArena(), new ArrayList<>(e.getArena().getPlayers()));
	}

	// Remove player from map on leave if playing less than time
	@EventHandler
	public void onLeaveArena(PlayerQuitArenaEvent e) {
		final Arena arena = e.getArena();

		if (arena.getStatus() == ArenaStatus.RUNNING && arena.getRunningTime() <= time)
			playing.get(arena).remove(e.getPlayer());
	}

	// Add players back if they rejoin
	@EventHandler
	public void onRejoin(PlayerRejoinArenaEvent e) {
		final Arena arena = e.getArena();

		if (arena.getStatus() == ArenaStatus.RUNNING && e.getIssues().isEmpty())
			playing.get(arena).add(e.getPlayer());
	}

	// Run commands on game end
	@EventHandler
	public void onGameEnd(RoundEndEvent e) {
		final Arena arena = e.getArena();
		final Collection<Player> activePlayers = playing.get(arena);

		if (activePlayers != null && time <= arena.getRunningTime()) {
			final HashMap<String, String> placeholderReplacements = new HashMap<>();

			if (e.getWinnerTeam() != null) {
				placeholderReplacements.put("winner-team-name", e.getWinnerTeam().getDisplayName());
				placeholderReplacements.put("winner-team-color", e.getWinnerTeam().name());
				placeholderReplacements.put("winner-team-color-code", "&" + e.getWinnerTeam().getChatColor().getChar());
			}

			for (Player player : activePlayers) {
				final List<Prize> endPrize = e.getWinners().contains(player) ? ConfigValue.playerWinPrize : ConfigValue.playerLosePrize;

				for (Prize prize : endPrize)
					prize.earn(arena, player, placeholderReplacements);
			}
		}
		playing.remove(arena);
	}
}
