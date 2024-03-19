package org.cxsmxs.listeners;

import org.cxsmxs.Auth;
import org.cxsmxs.utils.Session;
import org.cxsmxs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PlayerJoinListener implements Listener {

  private final Auth auth;

  public PlayerJoinListener(Auth plugin) {
    this.auth = plugin;

    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent e) {

    if (Auth.getInstance().getConf().getBoolean("kick_illegal_usernames", true)) {
      if (!e.getPlayer().getName().matches("[a-zA-Z0-9_]+") || e.getPlayer().getName().length() < 3 || e.getPlayer().getName().length() > 16) {
        Utils.kickPlayer(e.getPlayer(), Utils.getMessages("illegal_username_kick"));
        return;
      }
    }

    Auth.failures.put(e.getPlayer().getUniqueId(), 0);
    Auth.isLoggedIn.put(e.getPlayer().getUniqueId(), false);

    String uuid = (Auth.getInstance().getConf().getInt("player_identifier", 0) == 0) ? e.getPlayer().getName() : e.getPlayer().getUniqueId().toString();

    if (Auth.getInstance().getConf().getBoolean("session_enabled", false)) {
      Session session = Utils.getSession(uuid);
      if (session != null) {
        if (e.getPlayer().getAddress() != null && e.getPlayer().getAddress().getAddress() != null) {
          if (session.ip.equals(e.getPlayer().getAddress().getAddress().toString().replace("/", "")) && (session.date + Auth.getInstance().getConf().getInt("session_time", 30) * 60000L) > System.currentTimeMillis()) {
            Auth.isLoggedIn.put(e.getPlayer().getUniqueId(), true);
            e.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
            e.getPlayer().sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_successfully"));
          }
        }
      }
    }

    if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false)) {

      if (Auth.getInstance().getConf().getBoolean("teleport_player_last_location", true)) {
        Location loc = Utils.getLastPlayerLocation(uuid);
        if (loc == null) Utils.saveLastPlayerLocation(uuid, e.getPlayer().getLocation());
      }

      String world = auth.getConf().getString("spawn_world", null);
      if (world == null) {
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
      } else {
        World custom_world = Bukkit.getServer().getWorld(world);
        if (custom_world == null) {
          e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        } else {
          e.getPlayer().teleport(custom_world.getSpawnLocation());
        }
      }

      e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 1));

      if (!Utils.isPlayerRegistered(uuid)) {
        e.getPlayer().sendMessage(Utils.getMessages("prefix") + Utils.getMessages("register_syntax"));
        if (auth.getConf().getBoolean("titles_enabled", true))
          e.getPlayer().sendTitle(Utils.chat(auth.getConf().getString("register_title", "&aRegister")), Utils.chat(auth.getConf().getString("register_subtitle", "&a/register <password> <password>")), 50, auth.getConf().getInt("time_before_kick", 30) * 20, 50);
      } else {
        e.getPlayer().sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_syntax"));
        if (auth.getConf().getBoolean("titles_enabled", true))
          e.getPlayer().sendTitle(Utils.chat(auth.getConf().getString("login_title", "&aLogin")), Utils.chat(auth.getConf().getString("login_subtitle", "&a/login <password>")), 50, auth.getConf().getInt("time_before_kick", 30) * 20, 50);
      }

      Bukkit.getScheduler().runTaskLater(Auth.getInstance(), () -> {
        if (!Auth.isLoggedIn.getOrDefault(e.getPlayer().getUniqueId(), false) && e.getPlayer().isOnline()) {
          Utils.kickPlayer(e.getPlayer(), Utils.getMessages("prefix") + Utils.getMessages("login_time").replace("{time}", Utils.getConfig("time_before_kick")));
        }
      }, Auth.getInstance().getConf().getInt("time_before_kick", 30) * 20L);
    }
  }
}