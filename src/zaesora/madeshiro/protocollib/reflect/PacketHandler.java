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

package zaesora.madeshiro.protocollib.reflect;

import java.lang.reflect.Field;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 17/01/2017
 */
public class PacketHandler {

    private PacketConstructor constructor;

    private final Modifier mBytes;
    private final Modifier mFloats;
    private final Modifier mDoubles;
    private final Modifier mStrings;
    private final Modifier mIntegers;
    private final Modifier mBlocksPosition;

    public PacketHandler(PacketConstructor c) {
        this . constructor = c;
        this . mBytes = new Modifier(ResearchSet.SupportedType.Bytes);
        this . mFloats = new Modifier(ResearchSet.SupportedType.Floats);
        this . mDoubles = new Modifier(ResearchSet.SupportedType.Doubles);
        this . mStrings = new Modifier(ResearchSet.SupportedType.Strings);
        this . mIntegers =  new Modifier(ResearchSet.SupportedType.Integers);
        this . mBlocksPosition = new Modifier(ResearchSet.SupportedType.BlocksPosition);
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> boolean write(int id, Class<?> cls, T value) {
        return getModifierOf(cls).write(id, value);
    }

    public boolean write(String fieldName, Object value) {
        try {
            return write(constructor.iPacket.getClass().getDeclaredField(fieldName), value);
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private boolean write(Field field, Object value) {
        try {
            boolean accessible = field.isAccessible();
            if (!accessible)
                field.setAccessible(true);

            field.set(constructor.genPacket, value);

            if (!accessible)
                field.setAccessible(false);
        } catch (NullPointerException | IllegalArgumentException | IllegalAccessException e) {
            return false;
        }

        return true;
    }

    public Modifier getDoubles() {
        return mDoubles;
    }

    public Modifier getStrings() {
        return mStrings;
    }

    public Modifier getFloats() {
        return mFloats;
    }

    public Modifier getBytes() {
        return mBytes;
    }

    public Modifier getIntegers() {
        return mIntegers;
    }

    public Modifier getBlocksPosition() {
        return mBlocksPosition;
    }

    public Modifier getModifierOf(Class<?> fieldType) {
        return new Modifier(fieldType);
    }

    public class Modifier {
        private Field[] fields;

        Modifier(Class<?> type) {
            fields = constructor.fields.getFields(type);
        }

        Modifier(ResearchSet.SupportedType sType) {
            fields = constructor.fields.getFields(sType);
        }

        public boolean write(int id, Object value) {
            return (fields.length > id && id >= 0) &&
                    PacketHandler.this.write(fields[id], value);
        }
    }
}
