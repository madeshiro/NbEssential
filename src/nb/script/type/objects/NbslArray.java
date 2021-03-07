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
package nb.script.type.objects;

import java.util.ArrayList;
import static nb.script.type.objects.NbslObject.OperatorFunction.*;

public class NbslArray<E> extends NbslObject {

    private ArrayList<E> list = new ArrayList<>();

    public void add(E element) {
        list.add(element);
    }

    public void remove(E element) {
        list.remove(element);
    }

    public int size() {
        return list.size();
    }

    @Override
    public Object onOperator(OperatorFunction function, Object obj) {
        switch (function) {
            case PlusEquals:
                list.add((E) obj);
                return this;
            case MinusEquals:
                //noinspection SuspiciousMethodCalls
                list.remove(obj);
                return this;
            case Assign:
                assert obj instanceof NbslArray : "Assignation shold be done with same object type";
                list = new ArrayList<>(((NbslArray) obj).list);
                return this;
            case Equals:
                assert obj instanceof NbslArray : "Comparaison should be done with same object type";
                return list.equals(((NbslArray) obj).list);
            case Unequals:
                assert obj instanceof NbslArray : "Comparaison should be done with same object type";
                return !list.equals(((NbslArray) obj).list);
            default: return null;
        }

    }

    @Override
    public OperatorFunction[] getSupportedOperators() {
        return new OperatorFunction[] {PlusEquals, MinusEquals, Equals, Unequals, Assign};
    }
}
