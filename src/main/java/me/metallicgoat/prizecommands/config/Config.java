package me.metallicgoat.prizecommands.config;

import me.metallicgoat.prizecommands.Prize;
import me.metallicgoat.prizecommands.PrizeCommandsPlugin;
import me.metallicgoat.prizecommands.config.updater.ConfigUpdater;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
            ConfigUpdater.update(plugin, "config.yml", file, Collections.singletonList("Prizes"));
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

        // Load Prizes
        final ConfigurationSection section = mainConfig.getConfigurationSection("Prizes");
        final List<Prize> prizes = new ArrayList<>();

        if(section != null) {

            for(String key : section.getKeys(false)){
                final ConfigurationSection prizeSection = section.getConfigurationSection(key);

                if(prizeSection == null || !prizeSection.getBoolean("Enabled"))
                    continue;

                final Prize prize = new Prize(
                        key, // Prize id
                        prizeSection.getString("Permission"),
                        prizeSection.getStringList("Commands"),
                        prizeSection.getStringList("Broadcast"),
                        prizeSection.getStringList("Player-Message"),
                        prizeSection.getStringList("Supported-Arenas")
                );

                prizes.add(prize);
            }
        }

        ConfigValue.prizes = prizes;

        // Enabled
        ConfigValue.enabled = mainConfig.getBoolean("Enabled");

        // Regular Prizes
        ConfigValue.playerKillPrize = buildPrizeList(mainConfig.getStringList("Kill-Prizes"));
        ConfigValue.playerFinalKillPrize = buildPrizeList(mainConfig.getStringList("Final-Kill-Prizes"));
        ConfigValue.playerBreakBreakBedPrize = buildPrizeList(mainConfig.getStringList("Bed-Break-Prizes"));
        ConfigValue.playerEarnAchievementPrize = buildPrizeList(mainConfig.getStringList("Earn-Achievement-Prizes"));
        ConfigValue.playerJoinArenaPrize= buildPrizeList(mainConfig.getStringList("Join-Arena-Prizes"));
        ConfigValue.playerLeaveArenaPrize = buildPrizeList(mainConfig.getStringList("Leave-Arena-Prizes"));
        ConfigValue.playerRejoinArenaPrize = buildPrizeList(mainConfig.getStringList("Rejoin-Arena-Prizes"));

        // Game End
        ConfigValue.minimumPlayTime = mainConfig.getLong("End-Game-Prizes.Minimum-Time");
        ConfigValue.playerWinPrize = buildPrizeList(mainConfig.getStringList("End-Game-Prizes.Win-Prizes"));
        ConfigValue.playerLosePrize = buildPrizeList(mainConfig.getStringList("End-Game-Prizes.Lose-Prizes"));

        // Play Time
        ConfigValue.playTimePrizeEnabled = mainConfig.getBoolean("Playtime-Prize.Enabled");
        ConfigValue.playTimeInterval = mainConfig.getLong("Playtime-Prize.Interval");
        ConfigValue.playTimePrizes = buildPrizeList(mainConfig.getStringList("Playtime-Prize.Prizes"));
    }

    private static List<Prize> buildPrizeList(List<String> stringPrizes){
        final List<Prize> supportedPrizes = new ArrayList<>();

        if(stringPrizes == null || ConfigValue.prizes == null)
            return supportedPrizes;

        for(Prize prize : ConfigValue.prizes){
            if(stringPrizes.contains(prize.getPrizeId()))
                supportedPrizes.add(prize);
        }

        return supportedPrizes;
    }
}
