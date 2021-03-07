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
import nb.essential.player.BasicNbPlayer;
import nb.essential.player.NbPlayer;
import nb.essential.player.ProfileDescriptor;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import static nb.essential.main.NbEssential.*;
import static nb.essential.player.PlayerSystem.*;

/**
 * Class of NbEssential
 */
public class GroupManager {

    public static final String[] NB_GM_MINECRAFT_PERMISSION = new String [] {
            "minecraft.command.achievement",
            "minecraft.command.ban",
            "minecraft.command.ban-ip",
            "minecraft.command.banlist",
            "minecraft.command.clear",
            "minecraft.command.debug",
            "minecraft.command.defaultgamemode",
            "minecraft.command.deop",
            "minecraft.command.difficulty",
            "minecraft.command.effect",
            "minecraft.command.enchant",
            "minecraft.command.gamemode",
            "minecraft.command.gamerule",
            "minecraft.command.give",
            "minecraft.command.help",
            "minecraft.command.kick",
            "minecraft.command.kill",
            "minecraft.command.list",
            "minecraft.command.me",
            "minecraft.command.op",
            "minecraft.command.pardon",
            "minecraft.command.pardon-ip",
            "minecraft.command.playsound",
            "minecraft.command.save-all",
            "minecraft.command.save-on",
            "minecraft.command.save-off",
            "minecraft.command.say",
            "minecraft.command.scoreboard",
            "minecraft.command.seed",
            "minecraft.command.setblock",
            "minecraft.command.fill",
            "minecraft.command.setidletimeout",
            "minecraft.command.setworldspawn",
            "minecraft.command.spawnpoint",
            "minecraft.command.spreadplayers",
            "minecraft.command.stop",
            "minecraft.command.summon",
            "minecraft.command.tell",
            "minecraft.command.tellraw",
            "minecraft.command.testfor",
            "minecraft.command.testforblock",
            "minecraft.command.time",
            "minecraft.command.toggledownfall",
            "minecraft.command.tp",
            "minecraft.command.weather",
            "minecraft.command.whitelist",
            "minecraft.command.xp"
    };

    public static boolean Nb_GM_Save()
    {
        return getData().save();
    }

    public static boolean Nb_GM_Reload()
    {
        return getData().reload();
    }

    public static PermissionGroup Nb_GetGroup(String name) {
        return getData().group(name);
    }

    public static boolean Nb_IsGroupExist(String name) {
        return getData().groups.containsKey(name);
    }

    static GroupData getData() {
        return getDataLoader().getData("permission");
    }

    public static JSONFile Nb_GM_GetFileHandler() {
        return getData().groupFile;
    }

    public static List<BasicNbPlayer> Nb_GetPlayersFrom(PermissionGroup group, boolean online) {
        ArrayList<BasicNbPlayer> players = new ArrayList<>();

        if (online) {
            for (NbPlayer player : Nb_GetPlayers())
                if (player.hasPermissionGroup() && player.getPermissionGroup().equals(group))
                    players.add(player);
        } else {
            for (ProfileDescriptor descriptor : Nb_GetAllProfileDescriptor()) {
                if (descriptor.getHandler().hasPermissionGroup() && descriptor.getHandler().getPermissionGroup().equals(group)) {
                    players.add(descriptor.isOnlineDescriptor() ?
                            Nb_GetPlayer(descriptor.getComposedNickname()) :
                            Nb_GetOfflinePlayer(descriptor.getComposedNickname())
                    );
                }
            }
        }

        return players;
    }

    public static PermissionGroup Nb_CreateGroup(String name, String prefix) {
        if (Nb_IsGroupExist(name))
            return null; // group already exists

        PermissionGroup group = new PermissionGroup(name);
        group.forbiddens = new ArrayList<>();
        group.permissions = new ArrayList<>();
        group.inheritence = "";
        group.prefix = prefix;
        group.members = new HashSet<>();

        if (group.save()) {
            getData().groups.put(name, group);
            return group;
        } else
            return null;
    }

    public static ArrayList<String> Nb_GetGroupList() {
        return new ArrayList<>(getData().groups.keySet());
    }
}
