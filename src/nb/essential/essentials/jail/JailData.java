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

package nb.essential.essentials.jail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import nb.essential.files.JSONFile;
import nb.essential.files.ResourceFile;
import nb.essential.loader.Loader;
import nb.essential.loader.PluginData;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 25/09/2016
 */
@Loader("jails")
public class JailData implements PluginData {

    private JSONFile file;
    private ArrayList<Jail> jails;

    @Override
    public boolean save() {
        for (Jail jail : jails)
            jail.save(file);
        return true;
    }

    @Override
    public boolean load() {
        jails = new ArrayList<>();

        File jailFile = new File("plugins/NbEssential/jails.json");
        if (!jailFile.exists() &&
            !ResourceFile.copyFromResource("jails.json", "plugins/NbEssential/jails.json"))
            return false;

        file = new JSONFile(jailFile);

        return file.load();
    }

    @Override
    public boolean reload() {
        return load();
    }

    public Jail get(String name) {
        for (Jail jail : jails)
            if (jail.getName().equals(name))
                return jail;
        return null;
    }

    public List<Jail> getByTags(HashMap<String, String> tags) {

        return Collections.emptyList();
    }

    /**
     * Adds a jail in the jail's database.
     * @param jail The jail to put in the {@code ArrayList}.
     */
    void add(Jail jail) {
        jails.add(jail);
    }
    /**
     * Removes a jail from the jail's database.
     * @param jail The jail to remove.
     * @return A boolean, true if the jail was removed from the list, otherwise, false.
     * @implNote Return false means that the jail wasn't in the list.
     */
    boolean remove(Jail jail) {
        return jails.remove(jail);
    }
}
