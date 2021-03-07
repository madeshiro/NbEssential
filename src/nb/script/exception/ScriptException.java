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
package nb.script.exception;

import nb.script.type.RootChunk;

/**
 * Class of NbEssential in package nb.script.exception
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class ScriptException extends Exception {
    private final RootChunk root;

    public ScriptException(RootChunk root) {
        super();

        this . root = root;
    }

    public ScriptException(RootChunk root, String msg) {
        super(msg);

        this . root = root;
    }

    public ScriptException(RootChunk root, String msg, Throwable cause) {
        super(msg, cause);

        this . root = root;
    }

    public ScriptException(RootChunk root, Throwable cause) {
        super(cause);

        this . root = root;
    }

    public ScriptException(RootChunk root, String message,
                           Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        this . root = root;
    }

    public RootChunk getroot() {
        return root;
    }
}
