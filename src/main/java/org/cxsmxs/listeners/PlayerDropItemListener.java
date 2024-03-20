package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    private final Auth auth;

    public PlayerDropItemListener(Auth plugin) {
        auth = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false)) {
            e.setCancelled(true);
        }
    }

}