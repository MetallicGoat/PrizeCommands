package me.metallicgoat.prizecommands;

import de.marcely.bedwars.api.arena.Arena;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Prize {

    @Getter @Setter public boolean enabled = false;
    @Getter @Setter public String prizeId = null;
    @Getter @Setter public String permission = null;
    @Getter @Setter public List<String> commands = null;
    @Getter @Setter public List<String> broadcast = null;
    @Getter @Setter public List<String> privateMessage = null;
    @Getter @Setter public List<Arena> supportedArenas = null;

}
