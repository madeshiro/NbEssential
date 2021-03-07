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

import zaesora.madeshiro.protocollib.packet.*;
import zaesora.madeshiro.protocollib.reflect.PacketConstructor;

import java.io.IOException;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 09/10/2016
 */
public class ProtocolLib {

    private LibLogger logger;
    private static final ProtocolLib instance = new ProtocolLib();

    public ProtocolLib() {
        try {
            logger = new LibLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LibLogger getLogger() {
        return logger;
    }

    public static IPacket getPacket(int id, PacketType type, PacketType.Bound bound) {
        assert type != null;
        assert bound != null;

        switch (type) {
            case Play:
                return Play.getPacket(id, bound);
            case Login:
                return Login.getPacket(id, bound);
            case Status:
                return Status.getPacket(id, bound);
            case HandShaking:
                return Handshaking.getPacket(id, bound);
            default:
                return null;
        }
    }

    public static PacketConstructor buildPacket(int id, PacketType type, PacketType.Bound bound,
                                                Object... params) throws Exception {
        return getPacket(id, type, bound).build(params);
    }

    public static PacketConstructor buildPacket(IPacket packet, Object... params) {
        try {
            return packet.build(params);
        } catch (Exception e) {
            instance.getLogger().warning(e.getMessage());
            return null;
        }
    }

    public static ProtocolLib getInstance() {
        return instance;
    }
}
