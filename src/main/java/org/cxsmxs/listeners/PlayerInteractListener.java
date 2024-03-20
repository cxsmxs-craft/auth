package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final Auth auth;

    public PlayerInteractListener(Auth plugin) {
        auth = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e) {
        if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false)) {
            e.setCancelled(true);
        }
    }
}