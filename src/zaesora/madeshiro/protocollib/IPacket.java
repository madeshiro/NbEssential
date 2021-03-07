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

package zaesora.madeshiro.protocollib;

import net.minecraft.server.v1_13_R1.Packet;
import zaesora.madeshiro.protocollib.packet.PacketType;
import zaesora.madeshiro.protocollib.reflect.PacketConstructor;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 29/11/2016
 */
public interface IPacket {

    int getID();
    String getName();
    PacketType.Bound boundTo();
    Class<? extends Packet> getPacketClass();

    < T extends Packet<?> > T createPacket() throws Exception;
    < T extends Packet<?> > T createPacket(Object... objects) throws Exception;

    default PacketConstructor build(Object... objects) throws Exception {
        return new APacketConstructor(this, objects);
    }
}

final class APacketConstructor extends PacketConstructor {
    APacketConstructor(IPacket packet) throws Exception {
        super(packet);
    }

    APacketConstructor(IPacket packet, Object... params) throws Exception {
        super(packet, params);
    }
}
