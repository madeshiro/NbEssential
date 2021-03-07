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

package nb.roleplay.jukebox;

import nb.essential.loader.PluginData;

/**
 * Class of NbEssential
 */
public class AmbianceData implements PluginData {
    
    /**
     * Called to save this data of this database
     *
     * @return A boolean, true if the save action ended with success, false otherwise.
     */
    @Override
    public boolean save() {
        return false;
    }

    /**
     * Called to load the data from file and store it in this database.
     * This function is often called by the {@link #reload()} function.
     *
     * @return A boolean, true if the load action ended with success, false otherwise.
     */
    @Override
    public boolean load() {
        return false;
    }

    /**
     * Called to reload data.
     *
     * @return A boolean, true if the reload action ended with success, false otherwise.
     */
    @Override
    public boolean reload() {
        return false;
    }
}
