package org.cxsmxs.utils;

import org.cxsmxs.Auth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {
    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getConfig(String config) {
        return chat(Auth.getInstance().getConf().getString(config));
    }

    public static String getMessages(String config) {
        return chat(Auth.getInstance().getMess().getString(config));
    }

    public static void kickPlayer(Player player, String reason) {
        player.kickPlayer(chat(reason));
    }

    public static Hash getHash(String uuid) {
        if (Auth.hikari != null) {
            String query = "SELECT algo, hash, salt FROM Auth_players WHERE uuid = ?;";
            try {
                Connection conn = Auth.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String algo = rs.getString("algo");
                    String hash = rs.getString("hash");
                    String salt = rs.getString("salt");
                    rs.close();
                    ps.close();
                    conn.close();
                    return new Hash(algo, hash, salt, true);
                }
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            String algo = Auth.getInstance().getPass().getString(uuid + ".algo");
            String hash = Auth.getInstance().getPass().getString(uuid + ".hash");
            String salt = Auth.getInstance().getPass().getString(uuid + ".salt");
            return (algo != null && hash != null && salt != null) ? new Hash(algo, hash, salt, true) : null;
        }
        return null;
    }

    public static void savePassword(String uuid, String password, String ip, String date) {
        Hash hash = new Hash(getConfig("encoder"), password);
        if (Auth.hikari != null) {
            String query = "INSERT INTO Auth_players VALUES(?,?,?,?,?,?);";
            try {
                Connection conn = Auth.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, uuid);
                ps.setString(2, hash.algo);
                ps.setString(3, hash.hash);
                ps.setString(4, hash.salt);
                ps.setString(5, ip);
                ps.setString(6, date);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Auth.getInstance().getPass().set(uuid + ".algo", hash.algo);
            Auth.getInstance().getPass().set(uuid + ".hash", hash.hash);
            Auth.getInstance().getPass().set(uuid + ".salt", hash.salt);
            Auth.getInstance().savePass();
        }
    }

    public static void changePassword(String uuid, String password) {
        Hash hash = new Hash(getConfig("encoder"), password);
        if (Auth.hikari != null) {
            String query = "UPDATE Auth_players SET algo = ?, hash = ?, salt = ? WHERE uuid = ?;";
            try {
                Connection conn = Auth.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, hash.algo);
                ps.setString(2, hash.hash);
                ps.setString(3, hash.salt);
                ps.setString(4, uuid);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Auth.getInstance().getPass().set(uuid + ".algo", hash.algo);
            Auth.getInstance().getPass().set(uuid + ".hash", hash.hash);
            Auth.getInstance().getPass().set(uuid + ".salt", hash.salt);
            Auth.getInstance().savePass();
        }
    }

    public static boolean isPlayerRegistered(String uuid) {
        if (Auth.hikari != null) {
            String query = "SELECT COUNT(*) AS amount FROM Auth_players WHERE uuid = ?;";
            try {
                Connection conn = Auth.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("amount") == 0) {
                        rs.close();
                        ps.close();
                        conn.close();
                        return false;
                    }
                }
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            return Auth.getInstance().getPass().contains(uuid + ".hash");
        }
        return true;
    }

    public static Session setSession(String uuid, String ip) {
        if (Auth.hikari != null) {
            String query = "UPDATE Auth_players SET ip = ?, date = ? WHERE uuid = ?;";
            try {
                Connection conn = Auth.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, ip);
                ps.setString(2, String.valueOf(System.currentTimeMillis()));
                ps.setString(3, uuid);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            return Auth.session.put(uuid, new Session(ip, System.currentTimeMillis()));
        }
        return null;
    }

    public static void removeSession(String uuid) {
        if (Auth.hikari != null) {
            String query = "UPDATE Auth_players SET date = ? WHERE uuid = ?;";
            try {
                Connection conn = Auth.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, "0");
                ps.setString(2, uuid);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Auth.session.remove(uuid);
        }
    }

    public static Session getSession(String uuid) {
        if (Auth.hikari != null) {
            String query = "SELECT ip, date FROM Auth_players WHERE uuid = ?;";
            try {
                Connection conn = Auth.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String ip = rs.getString("ip");
                    long date = rs.getLong("date");
                    rs.close();
                    ps.close();
                    conn.close();
                    return new Session(ip, date);
                }
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            return Auth.session.getOrDefault(uuid, null);
        }
        return null;
    }

    public static Location getLastPlayerLocation(String uuid) {
        String sWorld = Auth.getInstance().getPass().getString(uuid + ".loc.world");
        double x = Auth.getInstance().getPass().getDouble(uuid + ".loc.x");
        double y = Auth.getInstance().getPass().getDouble(uuid + ".loc.y");
        double z = Auth.getInstance().getPass().getDouble(uuid + ".loc.z");
        float yaw = (float) Auth.getInstance().getPass().getDouble(uuid + ".loc.yaw");
        float pitch = (float) Auth.getInstance().getPass().getDouble(uuid + ".loc.pitch");
        if (sWorld == null) return null;
        World world = Bukkit.getServer().getWorld(sWorld);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static void saveLastPlayerLocation(String uuid, Location loc) {
        Auth.getInstance().getPass().set(uuid + ".loc.world", loc.getWorld().getName());
        Auth.getInstance().getPass().set(uuid + ".loc.x", loc.getX());
        Auth.getInstance().getPass().set(uuid + ".loc.y", loc.getY());
        Auth.getInstance().getPass().set(uuid + ".loc.z", loc.getZ());
        Auth.getInstance().getPass().set(uuid + ".loc.yaw", loc.getYaw());
        Auth.getInstance().getPass().set(uuid + ".loc.pitch", loc.getPitch());
        Auth.getInstance().savePass();
    }

}