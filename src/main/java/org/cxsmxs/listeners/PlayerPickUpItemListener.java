package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickUpItemListener
  implements Listener {
  private final Auth auth;

  public PlayerPickUpItemListener(Auth plugin) {
    this.auth = plugin;

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerPickUpItem(PlayerPickupItemEvent e) {
    if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false))
      e.setCancelled(true);
  }
}