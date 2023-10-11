package me.metallicgoat.prizecommands.config;

import de.marcely.bedwars.tools.YamlConfigurationDescriptor;
import me.metallicgoat.prizecommands.Prize;
import me.metallicgoat.prizecommands.PrizeCommandsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static final String ADDON_VERSION = PrizeCommandsPlugin.getInstance().getDescription().getVersion();
    public static String CURRENT_CONFIG_VERSION = null;

    public static File getFile(){
        return new File(PrizeCommandsPlugin.getAddon().getDataFolder(), "config.yml");
    }

    public static void load(){
        synchronized(Config.class){
            try{
                loadUnchecked();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void loadUnchecked() throws Exception {
        final File file = getFile();

        if(!file.exists()){
            save();
            return;
        }

        // load it
        final FileConfiguration config = new YamlConfiguration();

        try{
            config.load(file);
        }catch(Exception e){
            e.printStackTrace();
        }

        // Load Prizes
        final ConfigurationSection section = config.getConfigurationSection("Prizes");
        final List<Prize> prizes = new ArrayList<>();

        if(section != null) {

            for(String key : section.getKeys(false)){
                final ConfigurationSection prizeSection = section.getConfigurationSection(key);

                if(prizeSection == null)
                    continue;

                final Prize prize = new Prize(
                        key, // Prize id
                        prizeSection.getString("Permission"),
                        prizeSection.getStringList("Commands"),
                        prizeSection.getStringList("Player-Commands"),
                        prizeSection.getStringList("Broadcast"),
                        prizeSection.getStringList("Player-Message"),
                        prizeSection.getStringList("Supported-Arenas"),
                        prizeSection.getBoolean("Enabled")
                );

                prizes.add(prize);
            }
        }

        ConfigValue.allPrizes = prizes;

        // Enabled
        ConfigValue.enabled = config.getBoolean("Enabled");

        // Regular Prizes
        ConfigValue.playerKillPrize = buildPrizeList(config.getStringList("Kill-Prizes"));
        ConfigValue.playerFinalKillPrize = buildPrizeList(config.getStringList("Final-Kill-Prizes"));
        ConfigValue.playerBreakBreakBedPrize = buildPrizeList(config.getStringList("Bed-Break-Prizes"));
        ConfigValue.playerEarnAchievementPrize = buildPrizeList(config.getStringList("Earn-Achievement-Prizes"));
        ConfigValue.playerJoinArenaPrize = buildPrizeList(config.getStringList("Join-Arena-Prizes"));
        ConfigValue.playerStartGamePrize = buildPrizeList(config.getStringList("Start-Game-Prizes"));
        ConfigValue.playerLeaveArenaPrize = buildPrizeList(config.getStringList("Leave-Arena-Prizes"));
        ConfigValue.playerRejoinArenaPrize = buildPrizeList(config.getStringList("Rejoin-Arena-Prizes"));

        // Game End
        ConfigValue.minimumPlayTime = config.getLong("End-Game-Prizes.Minimum-Time");
        ConfigValue.playerWinPrize = buildPrizeList(config.getStringList("End-Game-Prizes.Win-Prizes"));
        ConfigValue.playerLosePrize = buildPrizeList(config.getStringList("End-Game-Prizes.Lose-Prizes"));

        // Play Time
        ConfigValue.playTimePrizeEnabled = config.getBoolean("Playtime-Prize.Enabled");
        ConfigValue.playTimeInterval = config.getLong("Playtime-Prize.Interval");
        ConfigValue.playTimePrizes = buildPrizeList(config.getStringList("Playtime-Prize.Prizes"));

        // auto update file if newer version
        {
            CURRENT_CONFIG_VERSION = config.getString("file-version");

            if(CURRENT_CONFIG_VERSION == null || !CURRENT_CONFIG_VERSION.equals(ADDON_VERSION)) {
                loadOldConfigs(config);
                save();
            }
        }
    }

    private static void save() throws Exception {
        final YamlConfigurationDescriptor config = new YamlConfigurationDescriptor();

        config.addComment("Used for auto-updating the config file. Ignore it");
        config.set("file-version", ADDON_VERSION);

        config.addEmptyLine();

        config.addComment("Join our discord for support: https://discord.gg/3mJuxUW");
        config.addComment("Please read the setup instructions here: https://github.com/MetallicGoat/PrizeCommands");
        config.addComment("Before asking for help on discord");
        config.set("Enabled", ConfigValue.enabled);

        config.addEmptyLine();

        config.addComment("Add Prize ID's here, NOT commands");
        config.set("Kill-Prizes", buildPrizeIdList(ConfigValue.playerKillPrize));
        config.set("Final-Kill-Prizes", buildPrizeIdList(ConfigValue.playerFinalKillPrize));
        config.set("Bed-Break-Prizes", buildPrizeIdList(ConfigValue.playerBreakBreakBedPrize));
        config.set("Earn-Achievement-Prizes", buildPrizeIdList(ConfigValue.playerEarnAchievementPrize));
        config.set("Join-Arena-Prizes", buildPrizeIdList(ConfigValue.playerJoinArenaPrize));
        config.set("Start-Game-Prizes", buildPrizeIdList(ConfigValue.playerStartGamePrize));
        config.set("Leave-Arena-Prizes", buildPrizeIdList(ConfigValue.playerLeaveArenaPrize));
        config.set("Rejoin-Arena-Prizes", buildPrizeIdList(ConfigValue.playerRejoinArenaPrize));

        config.addEmptyLine();

        config.addComment("Minimum-Time = Minimum time a player must play to be eligible for this prize (in ticks)");
        config.set("End-Game-Prizes.Minimum-Time", ConfigValue.minimumPlayTime);
        config.set("End-Game-Prizes.Win-Prizes", buildPrizeIdList(ConfigValue.playerWinPrize));
        config.set("End-Game-Prizes.Lose-Prizes", buildPrizeIdList(ConfigValue.playerLosePrize));

        config.addEmptyLine();

        config.addComment("Interval = How often a player receives this prize (In ticks)");
        config.set("Playtime-Prize.Enabled", ConfigValue.playTimePrizeEnabled);
        config.set("Playtime-Prize.Interval", ConfigValue.playTimeInterval);
        config.set("Playtime-Prize.Prizes", buildPrizeIdList(ConfigValue.playTimePrizes));

        config.addEmptyLine();

        config.addComment("Add Prize handlers here (These can be named anything, just dont break yml format)");
        // TODO when 5.1.1 comes out we can use the addEmptyLine function on config sections
        for(Prize prize : ConfigValue.allPrizes){
            final String path = "Prizes." + prize.prizeId + ".";

            config.set(path + "Enabled", prize.enabled);
            config.set(path + "Permission", prize.permission);
            config.set(path + "Commands", prize.commands);
            config.set(path + "Player-Commands", prize.playerCommands);
            config.set(path + "Broadcast", prize.broadcast);
            config.set(path + "Player-Message", prize.privateMessage);
            config.set(path + "Supported-Arenas", prize.supportedArenasNames);
        }

        config.save(getFile());
    }

    public static void loadOldConfigs(FileConfiguration config) {
        // Nothing here yet :)
    }

    private static List<String> buildPrizeIdList(List<Prize> prizes){
        final List<String> prizeIds = new ArrayList<>();

        for(Prize prize : prizes)
            prizeIds.add(prize.prizeId);

        return prizeIds;
    }

    private static List<Prize> buildPrizeList(List<String> stringPrizes){
        final List<Prize> supportedPrizes = new ArrayList<>();

        if(stringPrizes == null || ConfigValue.allPrizes == null)
            return supportedPrizes;

        for(Prize prize : ConfigValue.allPrizes){
            if(stringPrizes.contains(prize.prizeId))
                supportedPrizes.add(prize);
        }

        return supportedPrizes;
    }
}
