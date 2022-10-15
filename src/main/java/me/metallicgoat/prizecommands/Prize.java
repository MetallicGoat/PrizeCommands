package me.metallicgoat.prizecommands;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.picker.ArenaPickerAPI;
import de.marcely.bedwars.api.exception.ArenaConditionParseException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Prize {

    @Getter private final String prizeId;
    @Getter private final String permission;
    @Getter private final List<String> commands;
    @Getter private final List<String> broadcast;
    @Getter private final List<String> privateMessage;
    private final List<String> supportedArenasNames;

    public Prize(String prizeId,
            String permission,
            List<String> commands,
            List<String> broadcast,
            List<String> privateMessage,
            List<String> supportedArenasNames) {

        this.prizeId = prizeId;
        this.permission = permission;
        this.commands = commands != null ? commands : new ArrayList<>();
        this.broadcast = broadcast != null ? broadcast : new ArrayList<>();
        this.privateMessage = privateMessage != null ? broadcast : new ArrayList<>();
        this.supportedArenasNames = supportedArenasNames;

    }

    public List<Arena> getSupportedArenas(){
        final List<Arena> supportedArenas = new ArrayList<>();

        if(supportedArenasNames == null)
            return new ArrayList<>();

        for(String arenaName : supportedArenasNames){
            final Arena arena = GameAPI.get().getArenaByName(arenaName);
            if(arena != null)
                supportedArenas.add(arena);
            else {
                try {
                    final Collection<Arena> arenaList = ArenaPickerAPI.get().getArenasByCondition(arenaName);
                    supportedArenas.addAll(arenaList);
                } catch (ArenaConditionParseException ignored) {

                }
            }
        }

        return supportedArenas;
    }
}
