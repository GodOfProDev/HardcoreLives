package me.godofpro.hardcorelives;

import me.godofpro.hardcorelives.files.DataManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener{

    private static Main instance;

    public DataManager data;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this,this);
        // config stuff
        this.saveDefaultConfig();
        instance = this;
        this.data = new DataManager(this);

        Bukkit.getLogger().log(Level.INFO, "Starting Up");

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "Shutting down");
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        // if the player joins and they don't have any data in the file it will create one for them
        if (!this.data.getConfig().contains("players." + player.getUniqueId().toString() + ".lives")) {
            data.getConfig().set("players." + player.getUniqueId().toString() + ".lives", this.getConfig().getInt("starting-lives"));
            data.saveConfig();
        }
        // if the player joins and they don't have any data the file it will create one for them
        if(!this.data.getConfig().contains("players." + player.getUniqueId().toString() + ".kills")) {
            data.getConfig().set("players." + player.getUniqueId().toString() + ".kills", 0);
            data.saveConfig();
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event){
        LuckPerms lp;
        int amount = 0;
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(!player.hasPermission("HardcoreLives.invincibility")) {
                if (this.data.getConfig().contains("players." + player.getUniqueId().toString() + ".lives")) {
                    amount = this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".lives");
                }
                data.getConfig().set("players." + player.getUniqueId().toString() + ".lives", (amount - 1));
                data.saveConfig();

                if(data.getConfig().getInt("players." + player.getUniqueId().toString() + ".lives") == 0) {
                    data.getConfig().set("players." + player.getUniqueId().toString() + ".lives", this.getConfig().getInt("lives-afterban"));
                    data.saveConfig();
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tempban " + player.getName() + " "+ getConfig().getString("tempban-time") + " deathbanned!");
                }
            }
        }
    }

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
                    if (this.data.getConfig().contains("players." + killer.getUniqueId().toString() + ".kills")) {
                        amount = this.data.getConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
                    }
                    data.getConfig().set("players." + killer.getUniqueId().toString() + ".kills", (amount + 1));
                    data.saveConfig();

                    // check if the killer has 2 or more kills to give him a live
                    int amounts = 0;
                    if (data.getConfig().getInt("players." + killer.getUniqueId().toString() + ".kills") >= this.getConfig().getInt("kills-forlive")){
                        if (this.data.getConfig().contains("players." + killer.getUniqueId().toString() + ".lives")) {
                            amounts = this.data.getConfig().getInt("players." + killer.getUniqueId().toString() + ".lives");
                        }
                        data.getConfig().set("players." + killer.getUniqueId().toString() + ".lives", (amounts + 1));
                        data.saveConfig();
                        // set back the kills to 0
                        data.getConfig().set("players." + killer.getUniqueId().toString() + ".kills", 0);
                        data.saveConfig();
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command");
            return true;
        }
        Player player = (Player) sender;

        //Give Live Command
        if(cmd.getName().equalsIgnoreCase("givelive")) {
            if (args.length >= 2) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    try {
                        if(player.hasPermission("HardcoreLives.givelive")) {
                            int amount = Integer.parseInt(args[1]);
                            int amounts = 0;
                            if (this.data.getConfig().contains("players." + target.getUniqueId().toString() + ".lives")) {
                                amounts = this.data.getConfig().getInt("players." + target.getUniqueId().toString() + ".lives");
                            }
                            data.getConfig().set("players." + target.getUniqueId().toString() + ".lives", (amounts + amount));
                            data.saveConfig();
                            player.sendMessage("§e§l(!) §eThe Player Has Recived His Lives");
                        } else {
                            player.sendMessage("§e§l(!) §cYou don't have permission to use this command");
                        }
                    } catch (IllegalArgumentException e){
                        player.sendMessage("§e§l(!) §cThat is not a valid number");
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§e§l(!) §cThat is not a valid player");
                }


            } else {
                player.sendMessage("§e§l(!) §c/givelive <player> <amount>");
            }
        }


        if(cmd.getName().equalsIgnoreCase("setlive")) {
            if (args.length >= 2) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    try {
                        if(player.hasPermission("HardcoreLives.setlive")) {
                            int amounts = Integer.parseInt(args[1]);
                            data.getConfig().set("players." + target.getUniqueId().toString() + ".lives", (amounts));
                            data.saveConfig();
                            player.sendMessage("§e§l(!) §eThe Player Has Recived Their Lives");
                        } else {
                            player.sendMessage("§e§l(!) §cYou don't have permission to use this command");
                        }
                    } catch (IllegalArgumentException e){
                        player.sendMessage("§e§l(!) §cThat is not a valid number");
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§e§l(!) §cThat is not a valid player");
                }


            } else {
                player.sendMessage("§e§l(!) §c/setlive <player> <amount>");
            }
        }

        if(cmd.getName().equalsIgnoreCase("getlive")) {
            if (args.length >= 1) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    try {
                        if(player.hasPermission("HardcoreLives.getlive")) {
                            int amounts = 0;
                            if (this.data.getConfig().contains("players." + target.getUniqueId().toString() + ".lives")) {
                                amounts = this.data.getConfig().getInt("players." + target.getUniqueId().toString() + ".lives");
                            }
                            player.sendMessage("§e§l(!) §eThis Player Has " + amounts + " Lives!");
                        } else {
                            player.sendMessage("§e§l(!) §cYou don't have permission to use this command");
                        }
                    } catch (IllegalArgumentException e){
                        player.sendMessage("§e§l(!) §cThat is not a valid number");
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§e§l(!) §cThat is not a valid player");
                }


            } else {
                player.sendMessage("§e§l(!) §c/getlive <player>");
            }
        }


        return true;
    }
}
