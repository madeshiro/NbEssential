/*
 * This file is a part of the NbEssential plugin, licensed under the GPL v3 License (GPL)
 * Copyright © 2015-2018 MađeShirő ƵÆsora <https://github.com/madeshiro>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nb.essential.listeners;

import nb.essential.loader.EventListener;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.StringTokenizer;

import static nb.essential.main.NbEssential.tl;
import static nb.essential.main.NbEssential.getServer;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 27/12/2016
 */
@EventListener
public class PreprocessCommandListener implements Listener {

    private String label;
    private String[] args;

    private boolean tokenize(String msg) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(msg);
            int length = tokenizer.countTokens();

            args = new String[tokenizer.countTokens() - 1];
            label = tokenizer.nextToken();

            int i = 0;
            while (tokenizer.hasMoreTokens()) {
                args[i] = tokenizer.nextToken();
                ++i;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private Player _targetTP;
    private Location _locationTP;
    private Entity _destinationTP;

    @EventHandler
    public void onCommandTeleport(PlayerCommandPreprocessEvent event) {
        boolean tpStatus;
        tokenize(event.getMessage());
        if (!label.startsWith("/tp") || args.length == 0)
            return;

        _targetTP = event.getPlayer();

        if (args.length == 1) a(args[0]);
        if (args.length == 2) a(args[0], args[1]);
        if (args.length >= 3) a(args[0], args[1], args[2], args.length > 3 ? args[3] : null,
                                                           args.length > 4 ? args[4] : null,
                                                           _targetTP.getWorld());
        if (args.length >= 4) a(args[0], args[1], args[2], args[3],
                                                           args.length > 4 ? args[4] : null,
                                                           args.length > 5 ? args[5] : null);

        if (_locationTP == null && _destinationTP != null)
            tpStatus = _targetTP.teleport(_destinationTP, PlayerTeleportEvent.TeleportCause.COMMAND);
        else if (_locationTP != null && _destinationTP == null)
           tpStatus =  _targetTP.teleport(_locationTP, PlayerTeleportEvent.TeleportCause.COMMAND);
        else
            return;

        if (tpStatus)
            event.getPlayer().sendMessage("§e" + tl("s:c:teleport:tp_success"));
        else
            event.getPlayer().sendMessage("§c" + tl("s:c:teleport:tp_fail"));

        event.setCancelled(true);
    }

    void a(String var1) {
        _destinationTP = Bukkit.getPlayer(var1);
    }

    /**
     *
     * @param var1 aka Target Player
     * @param var2 aka Destination Player
     */
    void a(String var1, String var2) {
        _targetTP = Bukkit.getPlayer(var1);
        _destinationTP = Bukkit.getPlayer(var2);
    }

    void a(String var1, String var2, String var3, String var4, String var5, World world) {
        try {
            double x = b(var1, _targetTP.getLocation().getX());
            double y = b(var2, _targetTP.getLocation().getY());
            double z = b(var3, _targetTP.getLocation().getZ());
            float yaw  =  b(var4, _targetTP.getLocation().getYaw());
            float pitch = b(var5, _targetTP.getLocation().getPitch());

            _locationTP = new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException ignored) {
        }
    }

    void a(String var1, String var2, String var3, String var4, String var5, String var6) {
        try {
            Player player = Bukkit.getPlayer(var1);
            double x = b(var2, player.getLocation().getX());
            double y = b(var3, player.getLocation().getY());
            double z = b(var4, player.getLocation().getZ());
            float yaw = b(var5, player.getLocation().getYaw());
            float pitch = b(var6, player.getLocation().getPitch());

            if (player != null) {
                _targetTP = player;
                _locationTP = new Location(_targetTP.getWorld(), x, y, z, yaw, pitch);
            }
        } catch (NumberFormatException | NullPointerException ignored) {
        }
    }

    double b(String var1, double origin) throws NumberFormatException {
        if (var1.startsWith("~")) {
            if (var1.length() == 1)
                return origin;

            char var2 = var1.charAt(1);
            if (var2 == '-' || var2 == '+')
                var1 = var1.substring(2);
            else
                var1 = var1.substring(1);

            double var3 = Double.valueOf(var1);
            switch (var2) {
                case '-':
                    return origin - var3;
                default:
                    return origin + var3;
            }
        } else
            return Double.valueOf(var1);
    }

    float b(String var1, float origin) throws NumberFormatException {
        if (var1 == null)
            return origin;
        else if (var1.startsWith("~")) {
            if (var1.length() == 1)
                return origin;

            char var2 = var1.charAt(1);
            if (var2 == '-' || var2 == '+')
                var1 = var1.substring(2);
            else
                var1 = var1.substring(1);

            float var3 = Float.valueOf(var1);
            switch (var2) {
                case '-':
                    return origin - var3;
                default:
                    return origin + var3;
            }
        } else
            return Float.valueOf(var1);
    }

    Entity[] s(String selector, Player sender) {
        switch (selector) {
            case "@a":
                Player[] players = new Player[Bukkit.getOnlinePlayers().size()];
                return Bukkit.getOnlinePlayers().toArray(players);
            case "@p":
            case "@r":
                int rand = (int) (Math.random() * (getServer().getOnlinePlayers().size() - 1));
                return new Entity[] {getServer().getPlayer(getServer().getMCServer().getPlayers()[rand])};
            case "@e":
                List<Entity> nearbyEntities = sender.getNearbyEntities(50,50,50);
                Entity[] entities = new Entity[nearbyEntities.size()];
                return nearbyEntities.toArray(entities);
            default:
                return new Entity[0];
        }
    }
}
