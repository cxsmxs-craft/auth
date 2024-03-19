package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAttackListener implements Listener {

  private final Auth auth;

  public PlayerAttackListener(Auth plugin) {
    auth = plugin;

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerAttack(EntityDamageByEntityEvent e) {
    if (e.getDamager() instanceof Player) {
      if (!Auth.isLoggedIn.getOrDefault(e.getDamager().getUniqueId(), false)) {
        e.setCancelled(true);
      }
    }
  }
}