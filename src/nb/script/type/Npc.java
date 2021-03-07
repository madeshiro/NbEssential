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

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class Npc extends RootChunk {

    // TODO : add NbslNPC[] var and handler
    public Npc(ParseInfo info) {
        super(info);
    }

    protected Npc(RootChunk chunk, int executionID) {
        super(chunk, executionID);
    }

    @Override
    public void RunScript(ScriptSystem nbsl) throws ScriptException {
        Npc npc = new Npc(this, nbsl.generateExecutionID());
        nbsl.addRunningScript(npc);

        npc.run();
    }

    @Override
    public void addAtTag(MetaInstruction atTag) {
        if (atTag.isCompatibleWith(this)) {
            atTags.add(atTag);
        }
    }
}
