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
import zaesora.madeshiro.api.MojangAPI;
import zaesora.madeshiro.parser.json.JSONObject;

import java.util.UUID;

/**
 * Class of NbEssential
 */
public final class OfflineNbPlayer implements BasicNbPlayer {

    private UUID player_uuid;
    private String player_name;

    private JSONFile player_file;
    private ProfileDescriptor descriptor;

    OfflineNbPlayer(String player, String profile) throws IllegalArgumentException {
        player_name = player;
        player_uuid = MojangAPI.MjAPI_GetPlayerUniqueId(player);
        player_file = new JSONFile("plugins/NbEssential/player/" + player + ".json");

        if (player_file.load()) {
            JSONObject profiles = player_file.get("profiles");
            descriptor = new ProfileDescriptor(profile, player, profiles.getObject(profile));
        } else throw new IllegalArgumentException("Unable to load correctly " + player + ".json file !");

        if (descriptor == null)
            throw new IllegalArgumentException("Unable to load correctly " + player + "'s profile !");
    }

    OfflineNbPlayer(ProfileDescriptor descriptor) {
        player_name = descriptor.getHandler().getPlayer();
        player_uuid = MojangAPI.MjAPI_GetPlayerUniqueId(player_name);
        player_file = new JSONFile("plugins/NbEssential/player/" + player_name + ".json");

        if (player_file.load()) {
            this.descriptor = descriptor;
        } else throw new IllegalArgumentException("Unable to load correctly " + player_name + ".json file !");
    }

    public UUID getUniqueId() {
        return player_uuid;
    }

    public String getName() {
        return player_name;
    }

    @Override
    public String getNickname() {
        return descriptor.getNickname();
    }

    @Override
    public String getComposedNickname() {
        return descriptor.getComposedNickname();
    }

    @Override
    public String getPrefix() {
        return descriptor.getPrefix();
    }

    @Override
    public Eldar getMoney() {
        return new Eldar(descriptor.getMoney());
    }

    @Override
    public JSONFile getFile() {
        return player_file;
    }

    @Override
    @Deprecated
    public void reloadInnerClass() {
    }

    public boolean saveData() {
        player_file.set(descriptor.getJSONData(), "profiles", descriptor.getProfileName());
        return player_file.save();
    }

    @Override
    public boolean setPermissionGroup(PermissionGroup group) {
        return getProfileDescriptor().getHandler().setPermissionGroup(group);
    }

    @Override
    public PermissionGroup getPermissionGroup() {
        return getProfileDescriptor().getHandler().getPermissionGroup();
    }

    @Override
    public boolean hasPermissionGroup() {
        return getProfileDescriptor().getHandler().hasPermissionGroup();
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void unsetPermission(String permission) {
        getProfileDescriptor().getHandler().unsetPermission(permission);
    }

    @Override
    public void setPermission(String permission, boolean value) {
        getProfileDescriptor().getHandler().setPermission(permission, value);
    }

    @Override
    public String getProfileName() {
        return getProfileDescriptor().getProfileName();
    }

    @Override
    public ProfileDescriptor getProfileDescriptor() {
        return descriptor;
    }
}
