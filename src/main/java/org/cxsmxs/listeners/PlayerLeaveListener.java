package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.cxsmxs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final Auth auth;

    public PlayerLeaveListener(Auth plugin) {
        this.auth = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent e) {
        if (Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false)) {
            String uuid = (Auth.getInstance().getConf().getInt("player_identifier", 0) == 0) ? e.getPlayer().getName() : e.getPlayer().getUniqueId().toString();
            if(Auth.getInstance().getConf().getBoolean("teleport_player_last_location", true)){
                Utils.saveLastPlayerLocation(uuid, e.getPlayer().getLocation());
            }
        }
    }
}