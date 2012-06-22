package nl.live.lvk2.z64eyeswitch;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

public class EyeSwitchOff extends GenericCubeCustomBlock {
   
    public EyeSwitchOff(Plugin plugin, String tex_url) {
        super(plugin, "Eye Switch (powered)", tex_url, 32);
    }
}
