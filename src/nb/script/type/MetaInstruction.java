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
package nb.script.type;

import nb.script.type.interfaces.AtTag;

import java.util.HashMap;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public enum MetaInstruction implements AtTag {
    RunOnLoad(Script.class, Quest.class),
    Async(Loop.class, Event.class),
    Sync(Loop.class, Step.class),
    DisabledEvent(Event.class)
    ;

    private HashMap<String, Object> innerValues;
    private Class<? extends CodeChunk>[] supportedChunks;

    @SafeVarargs
    MetaInstruction(Class<? extends CodeChunk>... supportedChunks) {
        this.innerValues = new HashMap<>();
        this.supportedChunks = supportedChunks;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public HashMap<String, Object> getInnerValues() {
        return innerValues;
    }

    public Class<? extends CodeChunk>[] getSupportedChunks() {
        return supportedChunks;
    }

    public <T extends CodeChunk> boolean isCompatibleWith(T chunk) {
        for (Class<? extends CodeChunk> support : getSupportedChunks())
            if (support.isInstance(chunk))
                return true;
        return false;
    }
}
