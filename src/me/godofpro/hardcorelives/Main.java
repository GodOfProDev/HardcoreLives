package me.godofpro.hardcorelives;

import me.godofpro.hardcorelives.files.DataManager;
import me.godofpro.hardcorelives.files.HardcorelivesExpansion;
import me.godofpro.hardcorelives.listeners.ListenersHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public class Main extends JavaPlugin{

    private static Main instance;
    private static Main plugin;

    public DataManager data;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new ListenersHandler(),this);
        // config stuff
        this.saveDefaultConfig();
        instance = this;
        plugin = this;
        this.data = new DataManager(this);

        Bukkit.getLogger().log(Level.INFO, "[Hardcorelives] Starting up the plugin");


        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HardcorelivesExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "[Hardcorelives] Shutting down the plugin");
    }

    public static Main getPlugin() {
        return plugin;
    }
    public static Main getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        //Give Live Command
        if(cmd.getName().equalsIgnoreCase("givelive")) {
            if (args.length >= 2) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    try {
                        if(target != null) {
                            if (sender.hasPermission("HardcoreLives.givelive")) {
                                int amount = Integer.parseInt(args[1]);
                                int amounts = 0;
                                if (this.data.getConfig().contains("players." + target.getUniqueId().toString() + ".lives")) {
                                    amounts = this.data.getConfig().getInt("players." + target.getUniqueId().toString() + ".lives");
                                }
                                data.getConfig().set("players." + target.getUniqueId().toString() + ".lives", (amounts + amount));
                                data.saveConfig();
                                sender.sendMessage("§e§l(!) §eThe Player Has Recived His Lives");
                            } else {
                                sender.sendMessage("§e§l(!) §cYou don't have permission to use this command");
                            }
                        } else {
                            sender.sendMessage("§e§l(!) §cThat is not a valid player");
                        }
                    } catch (IllegalArgumentException e){
                        sender.sendMessage("§e§l(!) §cThat is not a valid number");
                    }
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§e§l(!) §cThat is not a valid player");
                }


            } else {
                sender.sendMessage("§e§l(!) §c/givelive <player> <amount>");
            }
        }


        if(cmd.getName().equalsIgnoreCase("setlive")) {
            if (args.length >= 2) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    try {
                        if(target != null) {
                            if (sender.hasPermission("HardcoreLives.setlive")) {
                                int amounts = Integer.parseInt(args[1]);
                                data.getConfig().set("players." + target.getUniqueId().toString() + ".lives", (amounts));
                                data.saveConfig();
                                sender.sendMessage("§e§l(!) §eThe Player Has Recived Their Lives");
                            } else {
                                sender.sendMessage("§e§l(!) §cYou don't have permission to use this command");
                            }
                        } else {
                            sender.sendMessage("§e§l(!) §cThat is not a valid player");
                        }
                    } catch (IllegalArgumentException e){
                        sender.sendMessage("§e§l(!) §cThat is not a valid number");
                    }
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§e§l(!) §cThat is not a valid player");
                }


            } else {
                sender.sendMessage("§e§l(!) §c/setlive <player> <amount>");
            }
        }

        if(cmd.getName().equalsIgnoreCase("getlive")) {
            if (args.length >= 1) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        try {
                            if (sender.hasPermission("HardcoreLives.getlive")) {
                                int amounts = 0;
                                if (this.data.getConfig().contains("players." + target.getUniqueId().toString() + ".lives")) {
                                    amounts = this.data.getConfig().getInt("players." + target.getUniqueId().toString() + ".lives");
                                }
                                sender.sendMessage("§e§l(!) §eThis Player Has " + amounts + " Lives!");
                            } else {
                                sender.sendMessage("§e§l(!) §cYou don't have permission to use this command");
                            }
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage("§e§l(!) §cThat is not a valid number");
                        }
                    } else {
                        sender.sendMessage("§e§l(!) §cThat is not a valid player");
                    }
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§e§l(!) §cThat is not a valid player");
                }


            } else {
                sender.sendMessage("§e§l(!) §c/getlive <player>");
            }
        }


        if(cmd.getName().equalsIgnoreCase("lives")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only Players Can Use This Command");
                return true;
            }
            Player player = (Player) sender;
            if (args.length >= 0) {
                try {
                    if(player.hasPermission("HardcoreLives.lives")) {
                        int amountsss = 0;
                        if (this.data.getConfig().contains("players." + player.getUniqueId().toString() + ".lives")) {
                            amountsss = this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".lives");
                        }
                        player.sendMessage("§e§l(!) §eYou Have " + amountsss + " Lives!");
                    } else {
                        player.sendMessage("§e§l(!) §cYou don't have permission to use this command");
                    }
                } catch (IllegalArgumentException e){
                    player.sendMessage("§e§l(!) §cThat is not a valid number");
                }


            } else {
                player.sendMessage("§e§l(!) §c/lives");
            }
        }


        return true;
    }

}
