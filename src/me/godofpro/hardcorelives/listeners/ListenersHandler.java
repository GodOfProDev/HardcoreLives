package me.godofpro.hardcorelives.listeners;

import me.godofpro.hardcorelives.Main;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenersHandler implements Listener {
    public Main plugin;
    // when a player joins it will check if they have any data else it will make one for them
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        plugin = Main.getPlugin();
        Player player = event.getPlayer();
        // if the player joins and they don't have any data in the file it will create one for them
        if (!plugin.data.getConfig().contains("players." + player.getUniqueId().toString() + ".lives")) {
            plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".lives", plugin.getConfig().getInt("starting-lives"));
            plugin.data.saveConfig();
        }
        // if the player joins and they don't have any data the file it will create one for them
        if(!plugin.data.getConfig().contains("players." + player.getUniqueId().toString() + ".kills")) {
            plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".kills", 0);
            plugin.data.saveConfig();
        }
    }
    // when someone dies it will remove 1 live if they don't have hardcorelives.invincibility
    // when they have 0 lives it will ban them
    @EventHandler
    public void onDeath(EntityDeathEvent event){
        LuckPerms lp;
        int amount = 0;
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(!player.hasPermission("HardcoreLives.invincibility")) {
                if (plugin.data.getConfig().contains("players." + player.getUniqueId().toString() + ".lives")) {
                    amount = plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".lives");
                }
                plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".lives", (amount - 1));
                plugin.data.saveConfig();

                if(plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".lives") == 0) {
                    plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".lives", plugin.getConfig().getInt("lives-afterban"));
                    plugin.data.saveConfig();
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tempban " + player.getName() + " " + plugin.getConfig().getString("tempban-time") + " deathbanned!");
                }
            }
        }
    }
    // add 1 lives after x amount of player kills
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        // Check if the one that died is a player
        if (event.getEntity() instanceof Player){
            // Check if the killer of the player is a player
            if(event.getEntity().getKiller() instanceof Player){
                // Check if the player sucide him/her self
                Player killer = event.getEntity().getKiller();
                if(event.getEntity().getKiller().getUniqueId() != event.getEntity().getUniqueId()){
                    // add 1 kill to the player
                    int amount = 0;
                    if (plugin.data.getConfig().contains("players." + killer.getUniqueId().toString() + ".kills")) {
                        amount = plugin.data.getConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
                    }
                    plugin.data.getConfig().set("players." + killer.getUniqueId().toString() + ".kills", (amount + 1));
                    plugin.data.saveConfig();

                    // check if the killer has 2 or more kills to give him a live
                    int amounts = 0;
                    if (plugin.data.getConfig().getInt("players." + killer.getUniqueId().toString() + ".kills") >= plugin.getConfig().getInt("kills-forlive")){
                        if (plugin.data.getConfig().contains("players." + killer.getUniqueId().toString() + ".lives")) {
                            amounts = plugin.data.getConfig().getInt("players." + killer.getUniqueId().toString() + ".lives");
                        }
                        plugin.data.getConfig().set("players." + killer.getUniqueId().toString() + ".lives", (amounts + 1));
                        plugin.data.saveConfig();
                        // set back the kills to 0
                        plugin.data.getConfig().set("players." + killer.getUniqueId().toString() + ".kills", 0);
                        plugin.data.saveConfig();
                    }
                }
            }
        }
    }
}
