package me.metallicgoat.PrizeCommands.Events;

import de.marcely.bedwars.api.event.arena.ArenaBedBreakEvent;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.PrizeCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BedBreakPrize implements Listener {
    @EventHandler
    public void onBedDestroy(ArenaBedBreakEvent e){
        final Main plugin = Main.getInstance();
        final Player p = e.getPlayer();
        if(p != null) {
            final String name = p.getName();
            for (String command : plugin.getConfig().getStringList("bed-break-prize.commands")) {
                if (command != null && !command.equals("")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Message.build(command).placeholder("destroyer", name).done());
                }
            }
        }
    }
}