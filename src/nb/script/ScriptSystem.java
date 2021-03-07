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

import nb.essential.loader.PluginData;
import nb.script.exception.ScriptException;
import nb.script.type.Quest;
import nb.script.type.RootChunk;
import nb.script.type.Script;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static nb.essential.main.NbEssential.isPluginDebug;
import static nb.script.ScriptManager.Nb_SL_ParseScript;
import static nb.script.ScriptManager.script_directory;

/**
 * Class of NbEssential in package nb.script
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
//@Loader(value = "nbsl", priority = LoaderPriority.Highest)
public class ScriptSystem implements PluginData {

    private int nextId = -1;
    private ArrayList<RootChunk> runningScripts;

    private HashSet<Quest> quests;
    private HashSet<Script> scripts;

    /**
     * Called to save this data of this database
     *
     * @return A boolean, true if the save action ended with success, false otherwise.
     */
    @Override
    public boolean save() {
        return true;
    }

    /**
     * Called to load the data from file and store it in this database.
     * This function is often called by the {@link #reload()} function.
     *
     * @return A boolean, true if the load action ended with success, false otherwise.
     */
    @Override
    public boolean load() {
        runningScripts = new ArrayList<>();
        quests = new HashSet<>();
        scripts = new HashSet<>();

        File directory = new File(script_directory);
        if (!directory.exists())
            directory.mkdir();

        for (File file : directory.listFiles()) {
            List<RootChunk> list = Nb_SL_ParseScript(file.getName(), script_directory);
            for (RootChunk chunk : list) {
                if (chunk instanceof Script) {
                    scripts.add((Script) chunk);
                    loginfo("Register script '" + chunk.getName() + "'");
                    if (((Script) chunk).doRunOnLoad()) {
                        try {
                            loginfo("Starting script '" + chunk.getName() + "' (cause=run-on-load)");
                            chunk.RunScript(this);
                        } catch (ScriptException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (chunk instanceof Quest) {
                    quests.add((Quest) chunk);
                    loginfo("Register quest '" + chunk.getName() + "'");
                } // TODO ADD NPC MANAGEMENT
            }
        }

        return true;
    }

    /**
     * Called to reload data.
     *
     * @return A boolean, true if the reload action ended with success, false otherwise.
     */
    @Override
    public boolean reload() {
        for (RootChunk chunk : runningScripts) {
            try {
                chunk.interrupt();
                loginfo("Interrupt '" + chunk.getName() + "' (cause=reload-data)");
            } catch (ScriptException e) {
                if (isPluginDebug()) e.printStackTrace();
                logsevere("An error occured when trying to interrupt '" + chunk.getName() + "' (cause=reload-data)");
            }
        }

        runningScripts.clear();
        quests.clear();
        scripts.clear();

        return load();
    }

    public int generateExecutionID() {
        return nextId++;
    }

    public void addRunningScript(RootChunk script) {
        runningScripts.add(script);
    }

    public void addScript(Script script) {
        scripts.add(script);
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    public HashSet<Script> getScripts() {
        return scripts;
    }

    public HashSet<Quest> getQuests() {
        return quests;
    }

    @Override
    public String getName() {
        return "NbSL";
    }
}
