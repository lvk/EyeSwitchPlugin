package nl.live.lvk2.z64eyeswitch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.getspout.spoutapi.SpoutWorld;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.material.CustomBlock;

public class RunnableArrowThread implements Runnable {

    ProjectileHitEvent event;
    private ArrayList<Material> ignore_blocks = new ArrayList<Material>();
    private ArrayList<Location> blockdirs = new ArrayList<Location>();

    public RunnableArrowThread(ProjectileHitEvent eventr) {
        this.event = eventr;
        ignore_blocks.add(Material.AIR);
        ignore_blocks.add(Material.GLASS);
        ignore_blocks.add(Material.STATIONARY_WATER);
        ignore_blocks.add(Material.WATER);
        ignore_blocks.add(Material.STATIONARY_LAVA);
        ignore_blocks.add(Material.LAVA);
        ignore_blocks.add(Material.REDSTONE_WIRE);
        ignore_blocks.add(Material.REDSTONE_TORCH_ON);
    }

    @Override
    public void run() {
        Projectile projectile = this.event.getEntity();
        if (projectile instanceof Arrow) {
            //Arrow block hit detection code by Bukkit forums user mushroomhostage
            //initial findings of bukkit's arrow hit detection by bergerkiller
            Field fieldX, fieldY, fieldZ;
            int x, y, z;
            Arrow arrow = (Arrow) this.event.getEntity();

            World world = arrow.getWorld();
            net.minecraft.server.EntityArrow entityArrow = ((CraftArrow) arrow).getHandle();
            try {
                fieldX = net.minecraft.server.EntityArrow.class.getDeclaredField("e");
                fieldY = net.minecraft.server.EntityArrow.class.getDeclaredField("f");
                fieldZ = net.minecraft.server.EntityArrow.class.getDeclaredField("g");
            } catch (Exception e) {
                return;
            }

            fieldX.setAccessible(true);
            fieldY.setAccessible(true);
            fieldZ.setAccessible(true);

            try {
                x = fieldX.getInt(entityArrow);
                y = fieldY.getInt(entityArrow);
                z = fieldZ.getInt(entityArrow);
            } catch (Exception e) {
                return;
            }
            SpoutBlock block = new SpoutWorld(world).getBlockAt(x, y, z);
            //Bukkit.getLogger().info(block.getCustomBlock().getName());
            CustomBlock cb = block.getCustomBlock();
            if (cb instanceof EyeSwitch || cb instanceof EyeSwitchOff) {
                //four directions from block attached directly
                blockdirs.add(new Location(world, x, y, z - 1));
                blockdirs.add(new Location(world, x, y, z + 1));
                blockdirs.add(new Location(world, x + 1, y, z));
                blockdirs.add(new Location(world, x - 1, y, z));
                //two blocks away in four directions
                blockdirs.add(new Location(world, x, y, z - 2));
                blockdirs.add(new Location(world, x, y, z + 2));
                blockdirs.add(new Location(world, x + 2, y, z));
                blockdirs.add(new Location(world, x - 2, y, z));
                //switch power on block
                for (int i = 0; i < 4; i++) {
                    Location l = blockdirs.get(i);
                    Block temp_block = world.getBlockAt(l);
                    //if the block is not in the ignore list, look ahead one block
                    if (!ignore_blocks.contains(temp_block.getType())) {
                        l = blockdirs.get(i + 4);
                        temp_block = world.getBlockAt(l);
                    }
                    //boolean variable to check switch power
                    boolean pwr = !eyePower(cb);
                    //replace wire with torch and veni vidi visa versa
                    if (temp_block.getType() == Material.REDSTONE_WIRE && pwr) {
                        temp_block.setType(Material.REDSTONE_TORCH_ON);
                    } else if (temp_block.getType() == Material.REDSTONE_TORCH_ON && !pwr) {
                        temp_block.setType(Material.REDSTONE_WIRE);
                    }
                    //block.setData();
                    CustomBlock toSet = EyeSwitchPlugin.eyeswitch;
                    if(pwr){
                        toSet = EyeSwitchPlugin.eyeswitchoff;
                    }
                    new SpoutWorld(world).getBlockAt(block.getLocation()).setCustomBlock(toSet);
                }
            }
        }
    }
    
    //checks if the custom block is a powered or unpowered eye switch
    boolean eyePower(CustomBlock cb){
        if(cb instanceof EyeSwitch){
            return false;
        }
        else if(cb instanceof EyeSwitchOff){
            return true;
        }
        return false;
    }
}
