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

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Packet;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 18/01/2017
 */
public final class ResearchSet {

    private HashMap<Class<?>, ArrayList<Field>> fieldMap;

    ResearchSet(Class<? extends Packet> packetClass) {
        fieldMap = new HashMap<>();
            sort(packetClass);
    }

    private void sort(Class<? extends Packet> aClass) {
        for (Field field : aClass.getDeclaredFields()) {
            if (fieldMap.containsKey(field.getType()))
                fieldMap.get(field.getType()).add(field);
            else
                fieldMap.put(field.getType(), new ArrayList<>(Collections.singletonList(field)));
        }
    }

    Field[] getFields(Class<?> type) {
        ArrayList<Field> fields = fieldMap.get(type);
        if (fields == null || fields.isEmpty())
            return new Field[0];

        Field[] var1 = new Field[fields.size()];
        return fields.toArray(var1);
    }

    Field[] getFields(SupportedType sType) {
        return getFields(sType.getType());
    }

    public enum SupportedType {
        BlocksPosition(BlockPosition.class),
        Integers(Integer.class),
        Doubles(Double.class),
        Strings(String.class),
        Floats(Float.class),
        Bytes(Byte.class);

        private final Class aClass;

        SupportedType(Class aClass) {
            this . aClass = aClass;
        }

        Class<?> getType() {
            return aClass;
        }
    }
}
