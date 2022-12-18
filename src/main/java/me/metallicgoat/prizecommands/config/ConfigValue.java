package me.metallicgoat.prizecommands.config;

import me.metallicgoat.prizecommands.Prize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ConfigValue {

    public static boolean enabled = false;
    public static List<Prize> allPrizes = buildExampleList("Prize-1", "Prize-2", "Prize-3");

    public static List<Prize> playerKillPrize = buildExampleList("Prize-1", "Prize-3");
    public static List<Prize> playerFinalKillPrize = buildExampleList("Prize-2", "Prize-3");
    public static List<Prize> playerBreakBreakBedPrize = buildExampleList("Prize-1");
    public static List<Prize> playerEarnAchievementPrize = buildExampleList("Prize-1", "Prize-2");
    public static List<Prize> playerJoinArenaPrize = buildExampleList("Prize-1", "Prize-2", "Prize-3");
    public static List<Prize> playerLeaveArenaPrize = buildExampleList("Prize-1", "Prize-3");
    public static List<Prize> playerRejoinArenaPrize = buildExampleList("Prize-2", "Prize-3");

    // Round End Prizes
    public static long minimumPlayTime = 2400;
    public static List<Prize> playerWinPrize = buildExampleList("Prize-1", "Prize-2", "Prize-3");
    public static List<Prize> playerLosePrize = buildExampleList("Prize-1", "Prize-2", "Prize-3");

    // Play Time
    public static boolean playTimePrizeEnabled = false;
    public static long playTimeInterval = 2400;
    public static List<Prize> playTimePrizes = buildExampleList("Prize-1", "Prize-2", "Prize-3");

    private static List<Prize> buildExampleList(String... prizeIds){
        final List<Prize> prizes = new ArrayList<>();

        for(String prizeId : prizeIds){
            prizes.add(new Prize(
                    prizeId,
                    "",
                    Collections.singletonList(""),
                    Collections.singletonList(""),
                    Collections.singletonList(""),
                    Collections.singletonList(""),
                    true));
        }

        return prizes;
    }
}
