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

package nb.essential.player;

import nb.economy.Eldar;
import nb.essential.files.JSONFile;
import nb.essential.permissions.PermissionGroup;

/**
 * Class of NbEssential
 */
public interface BasicNbPlayer {
    String getNickname();

    String getComposedNickname();

    String getPrefix();

    Eldar getMoney();

    JSONFile getFile();

    void reloadInnerClass();

    boolean setPermissionGroup(PermissionGroup group);

    PermissionGroup getPermissionGroup();

    boolean hasPermissionGroup();

    boolean isOnline();

    void unsetPermission(String permission);

    void setPermission(String permission, boolean value);

    String getProfileName();

    ProfileDescriptor getProfileDescriptor();

    default String getFirstConnection() {
        return getFile().get("info", "connection", "first");
    }

    default String getLastConnection() {
        return getFile().get("info", "connection", "last");
    }
}
