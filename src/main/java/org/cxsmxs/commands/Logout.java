package org.cxsmxs.commands;

import org.cxsmxs.Auth;
import org.cxsmxs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Logout implements ICommand {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;
    String uuid = (Auth.getInstance().getConf().getInt("player_identifier", 0) == 0) ? player.getName() : player.getUniqueId().toString();

    if (!Auth.isLoggedIn.getOrDefault(player.getUniqueId(), false)) {
      player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("logout_login_first"));
      return true;
    }

    Auth.isLoggedIn.put(player.getUniqueId(), false);
    Utils.removeSession(uuid);

    if (Auth.getInstance().getConf().getBoolean("teleport_player_last_location", true)) {
      Utils.saveLastPlayerLocation(uuid, player.getLocation());
      player.teleport(player.getWorld().getSpawnLocation());
    }

    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 1));

    if (!Utils.isPlayerRegistered(uuid)) {
      player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("register_syntax"));
      if (Auth.getInstance().getConf().getBoolean("titles_enabled", true))
        player.sendTitle(Utils.chat(Auth.getInstance().getConf().getString("register_title", "&aRegister")), Utils.chat(Auth.getInstance().getConf().getString("register_subtitle", "&a/register <password> <password>")), 50, Auth.getInstance().getConf().getInt("time_before_kick", 30) * 20, 50);
    } else {
      player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_syntax"));
      if (Auth.getInstance().getConf().getBoolean("titles_enabled", true))
        player.sendTitle(Utils.chat(Auth.getInstance().getConf().getString("login_title", "&aLogin")), Utils.chat(Auth.getInstance().getConf().getString("login_subtitle", "&a/login <password>")), 50, Auth.getInstance().getConf().getInt("time_before_kick", 30) * 20, 50);
    }

    Bukkit.getScheduler().runTaskLater(Auth.getInstance(), () -> {
      if (!Auth.isLoggedIn.getOrDefault(player.getUniqueId(), false) && player.isOnline()) {
        Utils.kickPlayer(player, Utils.getMessages("prefix") + Utils.getMessages("login_time").replace("{time}", Utils.getConfig("time_before_kick")));
      }
    }, Auth.getInstance().getConf().getInt("time_before_kick", 30) * 20L);

    return true;
  }
}