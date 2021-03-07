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

package nb.script.type.interfaces;

import nb.script.exception.ScriptException;
import nb.script.type.CodeChunk;

/**
 * Class of NbEssential in package nb.script.type.interfaces
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public interface Instruction {

    /**
     * Gets the declaration line of this instruction
     * @return the declaration file line.
     */
    int getLine();

    /**
     * Gets the declaration file name.
     * @return the declaration file name.
     */
    String getFileName();

    /**
     * Gets the declaration script name.
     * @return the declaration script name.
     */
    String getScriptName();

    /**
     * Runs the statement by specifying the parent containing the statement.
     *
     * @param parent The statement parent
     * @throws ScriptException If an exception occured while running the instruction.
     */
    void run(CodeChunk parent) throws ScriptException;
}
