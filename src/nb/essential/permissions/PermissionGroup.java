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

import nb.essential.player.BasicNbPlayer;
import nb.essential.player.NbPlayer;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import static nb.essential.permissions.GroupManager.*;
import static nb.essential.main.NbEssential.getServer;

/**
 * Class of NbEssential
 */
public class PermissionGroup  {

    /**
     * The group's name
     */
    String name;
    /**
     * Prefix to display in the chat and in the Player's list
     */
    String prefix;
    /**
     * A list of all prohibited permissions
     */
    ArrayList<String> forbiddens;
    /**
     * A list of all allowed permissions
     */
    ArrayList<String> permissions;

    /**
     *
     */
    HashMap<String, Boolean> medleyPerms;
    /**
     * Group from which this one inherit.
     */
    String inheritence;
    /**
     * A list of all members of this group.
     */
    HashSet<String> members;

    PermissionGroup(String name) {
        this . name = name;
        this . medleyPerms = new HashMap<>();
    }

    public boolean save() {
        JSONObject data = new JSONObject();
        JSONArray members = new JSONArray();
        if (this.members.size() > 0)
            members.addAll(this.members);

        data.put("prefix", prefix);
        data.put("members", members);
        data.put("inherit", inheritence);

        JSONArray permissions = new JSONArray(), forbiddens = new JSONArray();
        permissions.addAll(this.permissions);
        forbiddens.addAll(this.forbiddens);

        data.put("permissions", permissions);
        data.put("forbiddens", forbiddens);

        Nb_GM_GetFileHandler().<JSONObject>get("groups").put(name, data);
        return Nb_GM_GetFileHandler().save();
    }

    public boolean load() {
        JSONObject data = Nb_GM_GetFileHandler().get("groups", name);
        if (data == null)
            return false;

        members = new HashSet<>();
        permissions = new ArrayList<>();
        forbiddens = new ArrayList<>();

        prefix = data.getObject("prefix");

        for (Object member : Nb_GM_GetFileHandler().<JSONArray>get("groups",name,"members"))
            members.add((String) member);

        inheritence = data.getObject("groups", name, "inherit");

        for (Object permission : Nb_GM_GetFileHandler().<JSONArray>get("groups", name, "permissions"))
            permissions.add((String) permission);
        for (Object forbidden : Nb_GM_GetFileHandler().<JSONArray>get("groups", name, "forbiddens"))
            forbiddens.add((String) forbidden);

        return true;
    }

    public boolean reload() {
        if (Nb_GM_GetFileHandler().reload() && load()) {
            loadPermissions(getData().group(inheritence));
            return true;
        } else
            return false;
    }

    void loadPermissions(PermissionGroup inheritence) {
        medleyPerms.clear();

        if (inheritence != null) {
            for (String str : inheritence.permissions)
                medleyPerms.put(str, true);
            for (String str : inheritence.forbiddens)
                medleyPerms.put(str, false);
        }

        for (String str : permissions)
            medleyPerms.put(str, true);
        for (String str : forbiddens)
            medleyPerms.put(str, false);

        for (BasicNbPlayer player : Nb_GetPlayersFrom(this, true)) {
            player.getProfileDescriptor().getOnlineHandler().applyPermissions(true);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean hasPrefix() {
        return prefix != null && !prefix.isEmpty();
    }

    public void setPrefix(String prefix) {
        this . prefix = prefix;
    }

    public boolean setInheritence(String group) {
        PermissionGroup gr = getData().group(group);
        if (gr == null)
            return false;
        setInheritence(gr);
        return true;
    }

    public void setInheritence(PermissionGroup group) {
        this . inheritence = group.getName();
        loadPermissions(group);
    }

    public String getName() {
        return name;
    }

    public boolean changeName(String name) {
        Nb_GM_GetFileHandler().set(null, "groups", this.name);
        this . name = name;
        return save();
    }

    @Override
    public boolean equals(Object group) {
        if (group instanceof PermissionGroup)
            return getName().equals(((PermissionGroup) group).getName());
        else
            return super.equals(group);
    }

    public HashMap<String, Boolean> getPermissions() {
        return medleyPerms;
    }

    public void setPermission(String permission, boolean value) {
        (value ? permissions : forbiddens).add(permission);
        (!value ? permissions : forbiddens).remove(permission);
        loadPermissions(Nb_GetGroup(inheritence));
    }

    @Override
    public String toString() {
        return this . name;
    }

    public String getInheritence() {
        return inheritence;
    }

    public boolean addMember(String player) {
        NbPlayer nbPlayer = getServer().getNbPlayerByNickname(player); // make the difference
                                                                       // between profiles
        if (nbPlayer != null)
            return addMember(nbPlayer);

        for (PermissionGroup group : getData().groups.values()) {
            if (group.hasMember(player)) {
                group.removeMember(player);
                break;
            }
        }

        return members.add(player);
    }

    public boolean addMember(NbPlayer player) {
        if (player.hasPermissionGroup() && player.getPermissionGroup().equals(this))
            return false;
        if (player.hasPermissionGroup())
            player.getPermissionGroup().removeMember(player);

        return members.add(player.getComposedNickname());
    }

    public boolean removeMember(String player) {
        NbPlayer nbPlayer = getServer().getNbPlayerByNickname(player);
        if (nbPlayer != null)
            return removeMember(nbPlayer);

        return aRemMember(player);
    }

    public boolean removeMember(NbPlayer player) {
        return !(!player.hasPermissionGroup() || !player.getPermissionGroup().equals(this)) &&
                aRemMember(player.getComposedNickname());
    }

    public boolean hasMember(String player) {
        return members.contains(player);
    }

    public boolean hasMember(NbPlayer player) {
        return hasMember(player.getName());
    }

    public HashSet<String> getMembers() {
        return members;
    }

    public boolean aRemMember(String nickname) {
        ArrayList<String> list = new ArrayList<>(members);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(nickname)) {
                list.remove(i);
                members = new HashSet<>(list);
                return true;
            }
        }

        return false;
    }
}
