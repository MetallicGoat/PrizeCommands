package me.metallicgoat.prizecommands;

import de.marcely.bedwars.api.BedwarsAddon;

public class PrizeCommandsAddon extends BedwarsAddon {

	private final PrizeCommandsPlugin plugin;

	public PrizeCommandsAddon(PrizeCommandsPlugin plugin) {
		super(plugin);

		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "PrizeCommands";
	}
}
