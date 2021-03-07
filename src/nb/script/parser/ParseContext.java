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

package nb.script.parser;

import nb.script.type.CodeChunk;

/**
 * Class of NbEssential in package nb.script.parser
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class ParseContext {

    private int index;

    private String name;
    private Class<? extends CodeChunk> type;

    protected ParseContext() {
        index = 0;
        name = "";
        type = null;
    }

    public void setType(Class<? extends CodeChunk> type) {
        this.type = type;
    }

    public Class<? extends CodeChunk> getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public int increaseIndex() {
        return index++;
    }

    public int decreaseIndex() {
        return index--;
    }
}
