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

import nb.script.ScriptSystem;
import nb.script.exception.ScriptException;
import nb.script.parser.ParseInfo;
import nb.script.type.interfaces.Copyable;

import java.util.HashMap;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public abstract class RootChunk extends CodeChunk {

    private int executionID;
    private HashMap<String, Object> globalVariables;

    public RootChunk(ParseInfo info) {
        super(info);

        this.root = this;
        this.executionID = -1;
    }

    protected RootChunk(RootChunk chunk, int executionID) {
        super(chunk, null);

        this.root = this;
        this.executionID = executionID;
        this.globalVariables = chunk.globalVariables;
    }

    @Override
    public int hashCode() {
        return executionID;
    }

    @Override
    public HashMap<String, Object> getGlobalVariables() {
        return this.globalVariables;
    }

    @Override
    public void run() throws ScriptException {

        if (executionID < 0)
            throw new ScriptException(this, "Invalid execution ID ! Please, use Nb_SL_RunScript(Script) to run a valid Script instance.");

        super.run();

        if (!isWaiting()) {
            Delete();
        }
    }

    @Override
    public Copyable copy(CodeChunk parent) {
        throw new UnsupportedOperationException("A root chunk need to be copied using the constructor !");
    }

    @Override
    public final void setWaiting(boolean value) {
        this.wait = value;
    }

    @Override
    public final boolean isRoot() {
        return true;
    }

    @Override
    public void Delete() {
        super.Delete();

        // remove script from running
        // in case of Quest Code Chunk,
        // check if the amount of player running the current quest == 0
        // if true, unregister event

        // if players.size() == 0
        // super.Delete()
        // else {...}
    }

    public abstract void RunScript(ScriptSystem nbsl) throws ScriptException;

    /**
     * Defines which CodeChunk is allowed to be declared in this chunk.
     *
     * @return An array of allowed {@link CodeChunk} classes.
     * @see #isAllow(CodeChunk)
     */
    @Override
    protected Class<? extends CodeChunk>[] allowCodeChunks() {
        return new Class[] {
                Conditions.class,
                Event.class,
                Loop.class,
                Step.class
        };
    }
}
