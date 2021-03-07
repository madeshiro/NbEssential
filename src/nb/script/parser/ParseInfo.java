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
import nb.script.type.RootChunk;

/**
 * Class of NbEssential in package nb.script.parser
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class ParseInfo {

    private int line;
    private String fileName;

    private RootChunk root;
    private CodeChunk parent;
    private ParseContext context;

    public int getLine() {
        return line;
    }

    public String getFileName() {
        return fileName;
    }

    public RootChunk getRoot() {
        return root;
    }

    public CodeChunk getParent() {
        return parent;
    }

    public ParseContext getContext() {
        return context;
    }
}
