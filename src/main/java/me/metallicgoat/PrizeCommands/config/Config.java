package me.metallicgoat.PrizeCommands.config;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.exception.ArenaConditionParseException;
import me.metallicgoat.PrizeCommands.Prize;
import me.metallicgoat.PrizeCommands.PrizeCommandsPlugin;
import me.metallicgoat.PrizeCommands.config.updater.ConfigUpdater;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Config {
    public static File getFile(){
        return new File(PrizeCommandsPlugin.getAddon().getDataFolder(), "config.yml");
    }

    public static void save(){
        PrizeCommandsPlugin.getAddon().getDataFolder().mkdirs();

        synchronized(Config.class){
            try{
                saveUnchecked();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void saveUnchecked() throws IOException {
        final PrizeCommandsPlugin plugin = PrizeCommandsPlugin.getInstance();

        final File file = getFile();

        if(!file.exists())
            plugin.copyResource("config.yml", file);

        try {
            ConfigUpdater.update(plugin, "config.yml", file, Collections.singletonList("Silverfish.Display-Name"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        load();
    }

    public static FileConfiguration getConfig(){
        final FileConfiguration config = new YamlConfiguration();

        try{
            config.load(getFile());
        }catch(Exception e){
            e.printStackTrace();
        }

        return config;
    }

    public static void load(){
        final FileConfiguration mainConfig = getConfig();

        final ConfigurationSection section = mainConfig.getConfigurationSection("Prizes");


        final List<Prize> prizes = new ArrayList<>();

        if(section != null) {

            for(String key : section.getKeys(false)){
                final String beginPath = "Prizes." + key + ".";
                final Prize prize = new Prize();

                prize.setPrizeId(key);
                prize.setEnabled(mainConfig.getBoolean(beginPath + "Enabled"));
                prize.setPermission(mainConfig.getString(beginPath + "Permissions"));
                prize.setCommands(mainConfig.getStringList(beginPath + "Commands"));
                prize.setBroadcast(mainConfig.getStringList(beginPath + "Broadcast"));
                prize.setPrivateMessage(mainConfig.getStringList(beginPath + "Player-Message"));
                prize.setSupportedArenas(buildSupportedArenaList(mainConfig.getStringList(beginPath + "Supported-Arenas")));

                prizes.add(prize);
            }
        }

        ConfigValue.prizes = prizes;

        ConfigValue.playerWinPrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerLosePrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerKillPrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerFinalKillPrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerBreakBreakBedPrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerEarnAchievementPrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerJoinArenaPrize= buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerLeaveArenaPrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playerRejoinArenaPrize = buildPrizeList(prizes, mainConfig.getStringList(""));
        ConfigValue.playTimePrizes = buildPrizeList(prizes, mainConfig.getStringList(""));

    }

    private static List<Prize> buildPrizeList(List<Prize> allPrizes, List<String> stringPrizes){
        final List<Prize> supportedPrizes = new ArrayList<>();

        if(stringPrizes == null)
            return supportedPrizes;

        for(Prize prize : allPrizes){
            if(stringPrizes.contains(prize.getPrizeId()))
                supportedPrizes.add(prize);
        }

        return supportedPrizes;
    }

    private static List<Arena> buildSupportedArenaList(List<String> supportedArenaNames){
        final List<Arena> supportedArenas = new ArrayList<>();

        if(supportedArenaNames == null)
            return supportedArenas;

        for(String arenaName : supportedArenaNames){
            final Arena arena = GameAPI.get().getArenaByName(arenaName);
            if(arena != null)
                supportedArenas.add(arena);
            else{
                try {
                    final Collection<Arena> arenaList = GameAPI.get().getArenasByPickerCondition(arenaName);
                    supportedArenas.addAll(arenaList);
                } catch (ArenaConditionParseException ignored) {
                }
            }
        }
        return supportedArenas;
    }
}
