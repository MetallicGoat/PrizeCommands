package me.metallicgoat.prizecommands.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import me.metallicgoat.prizecommands.Prize;
import me.metallicgoat.prizecommands.config.ConfigValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStartPrize implements Listener {

	@EventHandler
	public void onRoundStart(RoundStartEvent event) {
		if (ConfigValue.playerStartGamePrize.isEmpty())
			return;

		final Arena arena = event.getArena();

		for (Player player : arena.getPlayers())
			for(Prize prize : ConfigValue.playerStartGamePrize)
				prize.earn(arena, player, null);

	}
}
