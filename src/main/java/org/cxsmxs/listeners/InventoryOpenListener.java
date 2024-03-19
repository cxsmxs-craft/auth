package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryOpenListener implements Listener {

  private final Auth auth;

  public InventoryOpenListener(Auth plugin) {
    auth = plugin;

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent e) {
    if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false)) {
      e.setCancelled(true);
    }
  }

}