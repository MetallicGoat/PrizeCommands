package me.metallicgoat.prizecommands.config;

import me.metallicgoat.prizecommands.Prize;

import java.util.List;


public class ConfigValue {

    public static boolean enabled = false;
    public static List<Prize> prizes = null;

    public static List<Prize> playerKillPrize = null;
    public static List<Prize> playerFinalKillPrize = null;
    public static List<Prize> playerBreakBreakBedPrize = null;
    public static List<Prize> playerEarnAchievementPrize = null;
    public static List<Prize> playerJoinArenaPrize = null;
    public static List<Prize> playerLeaveArenaPrize = null;
    public static List<Prize> playerRejoinArenaPrize = null;

    // Round End Prizes
    public static long minimumPlayTime = 2400;
    public static List<Prize> playerWinPrize = null;
    public static List<Prize> playerLosePrize = null;

    // Play Time
    public static boolean playTimePrizeEnabled = false;
    public static long playTimeInterval = 2400;
    public static List<Prize> playTimePrizes = null;

}
