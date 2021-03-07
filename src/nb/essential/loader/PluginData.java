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

package nb.essential.loader;

import nb.essential.files.ResourceFile;

import java.io.File;

import static nb.essential.main.NbEssential.getLogger;
import static nb.essential.main.NbEssential.isPluginDebug;

/**
 * <p>
 * Represents a "database" for the components associated with the plugin or one of
 * these subplugins. This interface possesses the essential which allows the {@link DataLoader}
 * to load, reload and save data according to how this interface is overload.
 * </p>
 * <p>
 * For this class is recognized and loaded, it requires that the class has the {@link Loader}
 * annotation having mentionned the database's name associated with. It's highly
 * recommended to choose well the base's name because it will be used to recover
 * the database later. Moreover, classes inheriting from {@code PluginData} should
 * not ask parameters in their constructor otherwise, the class loading will be
 * ignored.
 * </p>
 * <p>
 * During loading, the database can be recovered directly by using the {@link DataLoader#getData(String)}
 * function with settings the database's name. To verify if the base has been
 * loaded correctly, you can also use {@link DataLoader#exists(String)}.
 * </p>
 *
 * @see Loader
 * @see DataLoader
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
public interface PluginData {

    /**
     * Called to save this data of this database
     * @return A boolean, true if the save action ended with success, false otherwise.
     */
    boolean save();

    /**
     * Called to load the data from file and store it in this database.
     * This function is often called by the {@link #reload()} function.
     * @return A boolean, true if the load action ended with success, false otherwise.
     */
    boolean load();

    /**
     * Called to reload data.
     * @return A boolean, true if the reload action ended with success, false otherwise.
     */
    boolean reload();

    static File checkrcfile(String filename) {
        File rcfile = new File("plugins/NbEssential/"+filename);
        if (!rcfile.exists() &&
                !ResourceFile.copyFromResource(filename, "plugins/NbEssential/"+filename)) {
            return null;
        }
        return rcfile;
    }

    default String getName() {
        if (getClass().isAnnotationPresent(Loader.class)) {
            return getClass().getAnnotation(Loader.class).value();
        } else
            return "PluginData";
    }

    default void loginfo(String msg) {
        if (isPluginDebug())
            getLogger().info("["+getName()+"] " + msg);
    }

    default void logwarning(String msg) {
        if (isPluginDebug())
            getLogger().warning("["+getName()+"] " + msg);
    }

    default void logsevere(String msg) {
        if (isPluginDebug())
            getLogger().severe("["+getName()+"] " + msg);
    }
}
