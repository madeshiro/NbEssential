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

package zaesora.madeshiro.protocollib.packet;

import zaesora.madeshiro.protocollib.IPacket;
import net.minecraft.server.v1_13_R1.Packet;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 29/11/2016
 */
public class Handshaking {

    private static final HashMap<Integer, Handshaking.ClientBound> CLIENT_BOUND_HASH;
    private static final HashMap<Integer, Handshaking.ServerBound> SERVER_BOUND_HASH;

    static {
        CLIENT_BOUND_HASH = new HashMap<>();
        for (Handshaking.ClientBound bound : Handshaking.ClientBound.values())
            CLIENT_BOUND_HASH.put(bound.getID(), bound);

        SERVER_BOUND_HASH = new HashMap<>();
        for (Handshaking.ServerBound bound : Handshaking.ServerBound.values())
            SERVER_BOUND_HASH.put(bound.getID(), bound);
    }

    public static Handshaking.ClientBound getClientPacket(int id) {
        return CLIENT_BOUND_HASH.get(id);
    }

    public static Handshaking.ServerBound getServerPacket(int id) {
        return SERVER_BOUND_HASH.get(id);
    }

    public static IPacket getPacket(int id, PacketType.Bound bound) {
        //noinspection Duplicates
        switch (bound) {
            case Client:
                return getClientPacket(id);
            case Server:
                return getServerPacket(id);
            default:
                return null;
        }
    }

    public enum ClientBound implements IPacket {
        ;

        ClientBound(int id, String name, Class<? extends Packet> cls) {
            this . id = id;
            this . name = name;
            this . aClass = cls;
        }

        private int id;
        private String name;
        private Class<? extends Packet> aClass;

        @Override
        public int getID() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public PacketType.Bound boundTo() {
            return PacketType.Bound.Client;
        }

        @Override
        public Class<? extends Packet> getPacketClass() {
            return aClass;
        }

        @Override
        public <T extends Packet<?>> T createPacket() throws Exception {
            return (T) getPacketClass().newInstance();
        }

        @Override
        public <T extends Packet<?>> T createPacket(Object... objects) throws Exception {
            for (Constructor constructor : getPacketClass().getConstructors())
                if (constructor.getParameterCount() == objects.length)
                    return (T) constructor.newInstance(objects);
            return null;
        }
    }

    public enum ServerBound implements IPacket {
        ;

        ServerBound(int id, String name, Class<? extends  Packet> cls) {
            this . id = id;
            this . name = name;
            this . aClass = cls;
        }

        private int id;
        private String name;
        private Class<? extends Packet> aClass;

        @Override
        public int getID() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public PacketType.Bound boundTo() {
            return PacketType.Bound.Server;
        }

        @Override
        public Class<? extends Packet> getPacketClass() {
            return aClass;
        }

        @Override
        public <T extends Packet<?>> T createPacket() throws Exception {
            return (T) getPacketClass().newInstance();
        }

        @Override
        public <T extends Packet<?>> T createPacket(Object... objects) throws Exception {
            for (Constructor constructor : getPacketClass().getConstructors())
                if (constructor.getParameterCount() == objects.length)
                    return (T) constructor.newInstance(objects);
            return null;
        }
    }
}
