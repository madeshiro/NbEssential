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
package nb.script;

import nb.essential.scheduler.NbScheduler;
import nb.script.exception.ScriptException;
import nb.script.type.RootChunk;

import java.util.Collections;
import java.util.List;

import static nb.essential.main.NbEssential.getDataLoader;
import static nb.essential.main.NbEssential.getLogger;

/**
 * Class of NbEssential
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class ScriptManager {
    protected static final String script_directory = "/plugins/NbEssential/scripts/";
    private   static final EventManager event_manager = new EventManager();

    private static ScriptSystem getData() {
        return getDataLoader().getData("nbsl");
    }

    public static void Nb_SL_LogInfo(String text) {
        getLogger().info("[NbSL] " + text);
    }

    public static void Nb_SL_LogInfo(RootChunk root, String text) {
        Nb_SL_LogInfo("{"+root.getName()+"} " + text);
    }

    public static void Nb_SL_LogWarning(String text) {
        getLogger().warning("[NbSL] " + text);
    }

    public static void Nb_SL_LogWarning(RootChunk root, String text) {
        Nb_SL_LogWarning("{"+root.getName()+"} " + text);
    }

    public static void Nb_SL_LogSevere(String text) {
        getLogger().severe("[NbSL] " + text);
    }

    public static void Nb_SL_LogSevere(RootChunk root, String text) {
        Nb_SL_LogSevere("{"+root.getName()+"} " + text);
    }



    /**
     * Parses a script according to his file name. If the file doesn't exists or
     * if an error occured when parsing the script, the function will return null.
     *
     * @implNote The function will use the {@link #script_directory Script Directory} as root directory.
     * @param fileName The Script file name
     * @return An array of CodeChunk object if the parse succeed, Otherwise, <code>An empty</code>.
     */
    public static List<RootChunk> Nb_SL_ParseScript(String fileName) {
        return Nb_SL_ParseScript(fileName, script_directory);
    }

    public static List<RootChunk> Nb_SL_ParseScript(String fileName, String directory) {
        getData().loginfo("Load '" + fileName + "' in directory '" + directory + "' (0 RootChunk loaded)");
        return Collections.emptyList();
    }

    /**
     * Gets the NbSL Event Manager.
     * @return the NbSL {@link EventManager}.
     */
    public static EventManager Nb_SL_GetEventManager() {
        return event_manager;
    }

    public static void Nb_SL_RunScript(RootChunk script) throws ScriptException {
        script.RunScript(getDataLoader().getData("nbsl"));
    }

    /* --- */

    /**
     * Gets the NbSL Scheduler
     * @return the NbSL {@link ScriptScheduler}.
     */
    public static ScriptScheduler Nb_SL_GetScheduler() {
        return ScriptScheduler.instance;
    }

    /**
     * Class of NbEssential
     *
     * @author MađeShirő ƵÆsora
     * @since NB 1.2 (pre-release 1)
     * @version NB 1.2
     */
    public static class ScriptScheduler extends NbScheduler {

        private static final ScriptScheduler instance = new ScriptScheduler();

        protected ScriptScheduler() {
            super();
        }
    }
}
