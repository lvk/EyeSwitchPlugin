package nl.live.lvk2.z64eyeswitch;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

public class EyeSwitch extends GenericCubeCustomBlock {
 
    public EyeSwitch(Plugin plugin, String tex_url) {
        super(plugin, "Eye Switch", tex_url, 32);
    }
}
