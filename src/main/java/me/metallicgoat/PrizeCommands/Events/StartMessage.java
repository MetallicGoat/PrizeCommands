package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import me.metallicgoat.PrizeCommands.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StartMessage implements Listener {
    @EventHandler
    public void onGameStart(RoundStartEvent e){
        Main plugin = Main.getInstance();
        boolean enabled = plugin.getConfig().getBoolean("start-message.enabled");
        if(enabled) {
            for (Player p : e.getArena().getPlayers()) {
                for (String msg : plugin.getStartMessage()) {
                    String formattedMessage = ChatColor.translateAlternateColorCodes('&', msg);
                    p.sendMessage(formattedMessage);
                }
            }
        }
    }
}
