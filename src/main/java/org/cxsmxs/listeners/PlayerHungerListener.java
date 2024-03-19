package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerListener implements Listener {

    private final Auth auth;

    public PlayerHungerListener(Auth plugin) {
        auth = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent e){
        if(e.getEntity() instanceof Player){
            if (!Auth.isLoggedIn.getOrDefault(e.getEntity().getUniqueId(), false)) {
                e.setCancelled(true);
            }
        }
    }

}