package nl.live.lvk2.z64eyeswitch;

import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;

public class EyeSwitchPlugin extends JavaPlugin {

    public static EyeSwitch eyeswitch;
    public static EyeSwitchOff eyeswitchoff;
    public String tex_eyeopen;
    public String tex_eyeclosed;
    public static EyeSwitchPlugin instance;

    /*@Override
    public void onDisable(){ 
    
    }*/
    
    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
        tex_eyeopen = getConfig().getString("eyeswitchplugin.textureurls.eyeopen");
        tex_eyeclosed = getConfig().getString("eyeswitchplugin.textureurls.eyeclosed");
    }
    
    private void pli(String tgt){
        SpoutManager.getFileManager().addToPreLoginCache(this, tgt);
    }
    
    public void preLoginInit(){
        pli(tex_eyeopen);
        pli(tex_eyeclosed);
    }
    
    @Override
    public void onEnable() {
        loadConfig();
        preLoginInit();
        instance = this;
        eyeswitch = new EyeSwitch(this, tex_eyeopen);
        eyeswitchoff = new EyeSwitchOff(this, tex_eyeclosed);
        getServer().getPluginManager().registerEvents(new ProjectileListener(), this);
    }
}
