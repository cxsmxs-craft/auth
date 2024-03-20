package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

  private final Auth auth;

  public InventoryClickListener(Auth plugin) {
    auth = plugin;

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    if (!Auth.isLoggedIn.getOrDefault(e.getWhoClicked().getUniqueId(), false)) {
      e.setCancelled(true);
    }
  }
}