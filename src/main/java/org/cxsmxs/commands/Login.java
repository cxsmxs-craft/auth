package org.cxsmxs.commands;

import org.cxsmxs.Auth;
import org.cxsmxs.utils.Hash;
import org.cxsmxs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Login implements ICommand {

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;
    String uuid = (Auth.getInstance().getConf().getInt("player_identifier", 0) == 0) ? player.getName() : player.getUniqueId().toString();
    Hash hash = Utils.getHash(uuid);

    if (hash == null) {
      player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_register"));
      return true;
    }

    if (Auth.isLoggedIn.getOrDefault(player.getUniqueId(), false)) {
      player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_already"));
      return true;
    }

    if (args.length != 1) {
      player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_syntax"));
      return true;
    }

    Hash hash2 = new Hash(hash.algo, args[0], hash.salt, false);
    if (!hash2.hash.equals(hash.hash)) {
      int attempts = Auth.failures.merge(player.getUniqueId(), 1, Integer::sum);
      if (attempts >= Auth.getInstance().getConf().getInt("attempts")) {
        Auth.failures.put(player.getUniqueId(), 0);
        player.kickPlayer(Utils.getMessages("prefix") + Utils.getMessages("login_too_many_attempts"));
        return true;
      }
      player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_incorrect"));
      return true;
    }

    Auth.isLoggedIn.put(player.getUniqueId(), true);

    if (Auth.getInstance().getConf().getBoolean("teleport_player_last_location", true)) {
      Location loc = Utils.getLastPlayerLocation(uuid);
      if (loc != null) player.teleport(loc);
    }

    player.removePotionEffect(PotionEffectType.BLINDNESS);
    player.resetTitle();
    player.sendMessage(Utils.getMessages("prefix") + Utils.getMessages("login_successfully"));

    if (Auth.getInstance().getConf().getBoolean("session_enabled", false)) {
      if (player.getAddress() != null && player.getAddress().getAddress() != null)
        Utils.setSession(uuid, player.getAddress().getAddress().toString().replace("/", ""));
    }
    return true;
  }

}