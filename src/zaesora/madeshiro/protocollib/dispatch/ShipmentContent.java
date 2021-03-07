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

import net.minecraft.server.v1_13_R1.Packet;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static nb.essential.main.NbEssential.getServer;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 03/12/2016
 */
public class ShipmentContent {

    private HashSet<Player> players;
    private ArrayList<Packet<?>> packets;

    public ShipmentContent() {
        packets = new ArrayList<>();
        players = new HashSet<>();
    }

    public ShipmentContent(Packet<?>... packet) {
        this.packets = new ArrayList<>(Arrays.asList(packet));
        this.players = new HashSet<>(getServer().getOnlinePlayers());
    }

    public void addPacket(Packet<?> packet) {
        packets.add(packet);
    }

    public void addPacket(int index, Packet<?> packet) {
        packets.add(index, packet);
    }

    public boolean removePacket(Packet<?> packet) {
        return packets.remove(packet);
    }

    public void setTargets(Player... players) {
        this . players = new HashSet<>(Arrays.asList(players));
    }

    public void setTargets(List<Player> players) {
        this . players = new HashSet<>(players);
    }

    public HashSet<Player> getTargets() {
        return players;
    }

    public ArrayList<Packet<?>> getPackets() {
        return packets;
    }
}
