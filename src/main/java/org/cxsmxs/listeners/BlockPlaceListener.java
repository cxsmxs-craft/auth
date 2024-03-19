package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

  private final Auth auth;

  public BlockPlaceListener(Auth plugin) {
    auth = plugin;

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false)) {
      e.setCancelled(true);
    }
  }

}