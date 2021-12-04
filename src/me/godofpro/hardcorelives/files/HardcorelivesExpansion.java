package me.godofpro.hardcorelives.files;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.godofpro.hardcorelives.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class HardcorelivesExpansion extends PlaceholderExpansion {

    private Main plugin;
    public HardcorelivesExpansion(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "GodOfPro";
    }

    @Override
    public String getIdentifier() {
        return "hardcorelives";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("lives")){
            return Integer.toString(plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".lives"));
        }

        return null; // Placeholder is unknown by the expansion
    }

}