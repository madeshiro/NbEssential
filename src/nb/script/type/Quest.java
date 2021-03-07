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

import nb.essential.player.CraftNbPlayer;
import nb.script.ScriptSystem;
import nb.script.exception.ScriptException;
import nb.script.parser.ParseInfo;

import java.util.HashSet;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class Quest extends RootChunk {

    private CraftNbPlayer player;
    private HashSet<CraftNbPlayer> playerRunningQuest;

    public Quest(ParseInfo info) {
        super(info);

        playerRunningQuest = new HashSet<>();
    }

    @Override
    public void run() throws ScriptException {

        if (player == null)
            throw new ScriptException(this, "A player is needed to start a Quest CodeChunk");
        else
            defineLocalVariable("@s", player);

        super.run();
    }

    @Override
    public void RunScript(ScriptSystem nbsl) throws ScriptException {
        Quest quest = new Quest(this, nbsl.generateExecutionID());
        nbsl.addRunningScript(this);

        quest.run();
    }

    @Override
    public void addAtTag(MetaInstruction atTag) {
        if (atTag.isCompatibleWith(this)) {
            atTags.add(atTag);
        }
    }

    public void setPlayer(CraftNbPlayer player) {
        this.player = player;

        playerRunningQuest.add(player);
    }

    @Override
    public void Delete() {
        playerRunningQuest.remove(player);
        super.Delete();
    }

    protected Quest(Quest chunk, int executionID) {
        super(chunk, executionID);

        this.player = chunk.player;
        this.playerRunningQuest = chunk.playerRunningQuest;
    }

    public HashSet<CraftNbPlayer> getPlayersRunningQuest() {
        return playerRunningQuest;
    }
}
