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
import nb.script.events.NbslRunScriptEvent;
import nb.script.exception.ScriptException;
import nb.script.parser.ParseInfo;

import static nb.essential.main.NbEssential.getServer;
import static nb.script.ScriptManager.Nb_SL_LogInfo;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
public class Script extends RootChunk {

    public Script(ParseInfo info) {
        super(info);
    }

    @Override
    public void addAtTag(MetaInstruction atTag) {
        if (atTag.isCompatibleWith(this)) {
            atTags.add(atTag);
        }
    }

    protected Script(RootChunk chunk, int executionID) {
        super(chunk, executionID);
    }

    @Override
    public void RunScript(ScriptSystem nbsl) throws ScriptException {
        NbslRunScriptEvent event = new NbslRunScriptEvent(this);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        // Create a copy of this script to create an unique instance
        // with unlinked variables !
        Script script = new Script(this, nbsl.generateExecutionID());
        nbsl.addRunningScript(script);

        Nb_SL_LogInfo("Run script '" + getName() + "' !");

        // Run the script
        script.run();
    }

    public boolean doRunOnLoad() {
        return hasAtTag(MetaInstruction.RunOnLoad);
    }
}
