package nl.live.lvk2.z64eyeswitch;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileListener implements Listener {
    @EventHandler
    public void ProjectileHandler(ProjectileHitEvent event) {
        /*
         * Scheduled task with a delay of 0, as described by bukkit forums user
         * mushroomhostage, will make the RunnableArrowThread return the
         * correct value, rather than 0 in all fields.
         */
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                EyeSwitchPlugin.instance,
                new RunnableArrowThread(event),
                0L);
    }
}
