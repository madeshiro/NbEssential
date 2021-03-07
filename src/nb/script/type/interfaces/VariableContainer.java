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

import java.util.HashMap;

/**
 * Class of NbEssential in package nb.script.type.interfaces
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
public interface VariableContainer {

    HashMap<String, Object> getGlobalVariables();
    HashMap<String, Object> getLocalisedVariables();

    default Object getVariable(String name) {
        return getVariable(name, false);
    }

    default Object getVariable(String name, boolean isGlobal) {
        Object value;
        if (!isGlobal && (value = getLocalVariable(name)) != null)
            return value;
        else
            return getGlobalVariable(name);
    }

    /**
     * Gets local variable according to his name. In case of undefined var in
     * the current CodeChunk, the function will look at parent CodeChunk.
     *
     * @param name The variable name
     * @return The local variable value if found, Null otherwise.
     */
    Object getLocalVariable(String name);

    /**
     * Gets global variable.
     * @param name The global variable name.
     * @return The global variable value if existing, Null otherwise.
     */
    default Object getGlobalVariable(String name) {
        return getGlobalVariables().get(name);
    }

    /**
     * Sets a variable value if this one has been defined. <br/>
     * The function will first searched for local var if <code>isGlobal = false</code>
     * and, in case of non-existing local var, the function will look at global variables.
     *
     * @see #defineLocalVariable(String, Object)
     * @see #defineGlobalVariables(String, Object)
     *
     * @param name The variable full-name (including special char like '@' or '$')
     * @param value The variable value
     * @param isGlobal A boolean, if true, tell the function to directly look at global var. Otherwise,
     *                 the function will look at both var containers.
     * @return True if the variable exists, false otherwise.
     */
    boolean setVariable(String name, Object value, boolean isGlobal);

    /* --- */

    void   defineLocalVariable(String name, Object value);
    void   defineGlobalVariables(String name, Object value);

    /* --- */

    default void deleteVariable(String name) {
        deleteVariable(name, false);
    }
    void   deleteVariable(String name, boolean isGlobal);

    /* --- */
    boolean isLocalVariableExists(String name);
    default boolean isGlobalVariableExists(String name) {
        return getGlobalVariables().containsKey(name);
    }
}
