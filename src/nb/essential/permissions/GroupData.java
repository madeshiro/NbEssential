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
package nb.essential.permissions;

import nb.essential.files.JSONFile;
import nb.essential.loader.Loader;
import nb.essential.loader.PluginData;
import nb.essential.files.ResourceFile;
import zaesora.madeshiro.parser.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Class of NbEssential
 */
@Loader("permission")
public class GroupData implements PluginData {

    public final String getName() {
        return "GroupManager";
    }

    JSONFile groupFile;
    final HashMap<String, PermissionGroup> groups;

    public GroupData() {
        groups = new HashMap<>();
    }

    /**
     * Called to save this data of this database
     *
     * @return A boolean, true if the save action ended with success, false otherwise.
     */
    @Override
    public boolean save() {
        boolean status = true;
        for (PermissionGroup group : groups.values()) {
            if (group.save()) {
                loginfo("Group '" + group.getName() + "' hasn't been successfuly saved.");
            } else {
                loginfo("Group '" + group.getName() + "' hasn't been saved correctly...");
                status = false;
            }
        }
        return status;
    }

    /**
     * Called to load the data from file and store it in this database.
     * This function is often called by the {@link #reload()} function.
     *
     * @return A boolean, true if the load action ended with success, false otherwise.
     */
    public boolean load() {
        File file = new File("plugins/NbEssential/groups.json");
        if (!file.exists() &&
                !ResourceFile.copyFromResource("groups.json", "plugins/NbEssential/groups.json"))
            return false;
        this.groupFile = new JSONFile(file);
        if (!groupFile.load())
            return false;

        for (String key : groupFile.<JSONObject>get("groups").keySet()) {
            PermissionGroup group = new PermissionGroup(key);
            if (group.load()) {
                groups.put(key, group);
                loginfo("Load group <" + key + ">");
            } else logwarning("Unable to load group <" + key + ">");
        }

        loginfo(groups.size() + " groups has been loaded !");

        for (PermissionGroup group : groups.values())
            group.loadPermissions(groups.get(group.inheritence));

        return true;
    }

    /**
     * Called to reload data.
     *
     * @return A boolean, true if the reload action ended with success, false otherwise.
     */
    @Override
    public boolean reload() {
        if (!groupFile.reload())
            return false;

        boolean status = true;
        for (PermissionGroup group : groups.values()) {
            if (!group.reload()) {
                logwarning("Unable to reload group <" + group.getName() + ">");
                status = false;
            }
        }

        return status;
    }

    PermissionGroup group(String name) {
        return groups.get(name);
    }
}
