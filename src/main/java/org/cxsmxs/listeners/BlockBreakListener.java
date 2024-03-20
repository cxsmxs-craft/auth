package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final Auth auth;

    public BlockBreakListener(Auth plugin) {
        auth = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false)) {
            e.setCancelled(true);
        }
    }

}
