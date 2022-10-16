package me.metallicgoat.prizecommands.config;

import me.metallicgoat.prizecommands.Prize;

import java.util.ArrayList;
import java.util.List;


public class ConfigValue {

    public static boolean enabled = false;
    public static List<Prize> prizes = new ArrayList<>();

    public static List<Prize> playerKillPrize = new ArrayList<>();
    public static List<Prize> playerFinalKillPrize = new ArrayList<>();
    public static List<Prize> playerBreakBreakBedPrize = new ArrayList<>();
    public static List<Prize> playerEarnAchievementPrize = new ArrayList<>();
    public static List<Prize> playerJoinArenaPrize = new ArrayList<>();
    public static List<Prize> playerLeaveArenaPrize = new ArrayList<>();
    public static List<Prize> playerRejoinArenaPrize = new ArrayList<>();

    // Round End Prizes
    public static long minimumPlayTime = 2400;
    public static List<Prize> playerWinPrize = new ArrayList<>();
    public static List<Prize> playerLosePrize = new ArrayList<>();

    // Play Time
    public static boolean playTimePrizeEnabled = false;
    public static long playTimeInterval = 2400;
    public static List<Prize> playTimePrizes = new ArrayList<>();

}
