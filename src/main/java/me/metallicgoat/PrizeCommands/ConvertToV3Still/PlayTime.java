package me.metallicgoat.PrizeCommands.ConvertToV3Still;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.PrizeCommands.PrizeCommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class PlayTime implements Listener {

    private BukkitTask task;

    @EventHandler
    public void gameStart(RoundStartEvent e){
        final PrizeCommandsPlugin plugin = PrizeCommandsPlugin.getInstance();
        final Arena a = e.getArena();
        if(plugin.getConfig().getBoolean("playtime-prize.enabled")) {
            final long time = plugin.getConfig().getLong("playtime-prize.interval");
            task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if(a.getStatus() == ArenaStatus.RUNNING) {
                    for(Player p:a.getPlayers()){
                        final String regularName = p.getName();
                        final String displayName = BedwarsAPI.getHelper().getPlayerDisplayName(p);
                        for (String command : plugin.getConfig().getStringList("playtime-prize.commands")) {
                            if (command != null && !command.equals("")) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                        Message.build(command).placeholder("player", regularName).done());
                            }
                        }
                        for (String msg : plugin.getConfig().getStringList("playtime-prize.message")) {
                            final String formatted = Message.build(msg).placeholder("player", displayName).done();
                            p.sendMessage(formatted);
                        }
                    }
                }else{
                    task.cancel();
                }
            }, time, time);
        }
    }
}
