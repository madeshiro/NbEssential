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
package zaesora.madeshiro.protocollib.dispatch;

import org.bukkit.entity.Player;
import nb.essential.main.NbEssential;
import nb.essential.player.CraftNbPlayer;
import net.minecraft.server.v1_13_R1.Packet;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import zaesora.madeshiro.protocollib.event.DispatchPacketEvent;
import zaesora.madeshiro.protocollib.reflect.PacketConstructor;

import java.util.List;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Bukkit.getScheduler;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 03/12/2016
 */
public class DispatchTool {

    @SuppressWarnings("UnusedReturnValue")
    public static boolean send(final ShipmentContent content) {
        DispatchPacketEvent event = new DispatchPacketEvent(content);
        getServer().getPluginManager().callEvent(event);

        if (event.isCancelled())
            return false;
        sendSilently(content);
        return true;
    }

    public static void delayedDispatch(final ShipmentContent content, long delay) {
        getScheduler().scheduleSyncDelayedTask(NbEssential.getPlugin(), () -> send(content), delay);
    }

    public static void sendSilently(final ShipmentContent content) {
        for (Player player : content.getTargets()) {
            for (Packet<?> packet : content.getPackets())
                if (player instanceof CraftPlayer)
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                else if (player instanceof CraftNbPlayer)
                    ((CraftNbPlayer) player).getPlayerConnection().sendPacket(packet);
        }
    }

    public static ShipmentContent createShipmentContent(PacketConstructor pc, Player... targets) {
        ShipmentContent content = new ShipmentContent(pc.getGenPacket());
        content.setTargets(targets);
        return content;
    }

    public static ShipmentContent createShipmentContent(PacketConstructor pc, List<Player> players) {
        ShipmentContent content = new ShipmentContent(pc.getGenPacket());
        content.setTargets(players);
        return content;
    }
}
