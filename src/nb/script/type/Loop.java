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

import nb.script.parser.ParseInfo;
import nb.script.type.interfaces.Breakable;
import nb.script.exception.ScriptException;
import nb.script.type.interfaces.Copyable;

import static nb.script.ScriptManager.Nb_SL_GetScheduler;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class Loop extends CodeChunk implements Breakable {

    private int taskId = -1;
    private long repeat = 0;

    public Loop(ParseInfo info) {
        super(info);
    }

    @Override
    public void run() throws ScriptException {

        if (repeat <= 0)
            throw new ScriptException(getRoot(), "Invalid repeat value");

        if (hasAtTag(MetaInstruction.Sync))
            parent.setWaiting(true);

        taskId = Nb_SL_GetScheduler().scheduleSyncRepeatingTask(taskId -> {
            try {
                super.run();
            } catch (ScriptException e) {
                Nb_SL_GetScheduler().scheduleCancelTask(taskId);
                e.printStackTrace();
            }
        }, 0, repeat);
    }

    /**
     * Defines which CodeChunk is allowed to be declared in this chunk.
     *
     * @return An array of allowed {@link CodeChunk} classes.
     * @see #isAllow(CodeChunk)
     */
    @Override
    protected Class<? extends CodeChunk>[] allowCodeChunks() {
        return new Class[] {
                Step.class,
                Loop.class,
                Conditions.class
        };
    }

    @Override
    public void addAtTag(MetaInstruction atTag) {
        //noinspection Duplicates
        if (atTag.isCompatibleWith(this)) {
            atTags.add(atTag);

            switch (atTag) {
                case Sync:
                    atTags.remove(MetaInstruction.Async);
                    break;
                case Async:
                    atTags.remove(MetaInstruction.Sync);
                    break;
            }
        }
    }

    protected Loop(Loop chunk, CodeChunk parent) {
        super(chunk, parent);

        this.repeat = chunk.repeat;
    }

    public void setRepeat(long tick) {
        this.repeat = tick;
    }

    @Override
    public void Break() {
        Delete();
    }

    @Override
    public void Return() {
        super.Return();
        Nb_SL_GetScheduler().scheduleCancelTask(taskId);

        if (hasAtTag(MetaInstruction.Sync)) {
            try {
                parent.setWaiting(false);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Delete() {
        eventLoaded = false;

        Return();
    }

    @Override
    public Copyable copy(CodeChunk parent) {
        return new Loop(this, parent);
    }
}
