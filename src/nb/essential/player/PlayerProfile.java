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

import static nb.essential.main.NbEssential.*;
import static nb.essential.player.PlayerSystem.*;

import nb.essential.commands.CommandVariables;
import nb.essential.files.JSONFile;
import nb.essential.main.IServer;
import nb.essential.utils.Utils;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import zaesora.madeshiro.parser.json.JSONObject;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 31/08/2016
 */
final class PlayerProfile {

    private JSONFile file;
    private ProfileDescriptor descriptor;

    private final String name;

    private final CraftNbPlayer player;
    protected CommandVariables variables;
    private PermissionAttachment attachment;

    /**
     * Creates a new PlayerProfile based on the {@link CraftPlayer player} and the profile name.
     * @param player The current player.
     * @param name The profile name.
     */
    public PlayerProfile(CraftPlayer player, String name) {
        this.name = name;
        this.player = new CraftNbPlayer(player, this);

        this.file = new JSONFile("plugins/NbEssential/player/" + player.getName() + ".json");
        this.attachment = this.player.getBukkitPlayer().addAttachment(getPlugin());
        this.variables = new CommandVariables();
    }

    /**
     * Saves the player's profile.
     * @return A boolean, TRUE on success, false otherwise.
     */
    public boolean save() {
        return descriptor.save(file);
    }

    /**
     * Loads the player's profile.
     * @return A boolean, TRUE on success, false otherwise.
     */
    public boolean load() {
        if (file.load()) {
            JSONObject profiles = file.get("profiles");
            if (!profiles.containsKey(name))
                return false;

            descriptor = new ProfileDescriptor(name, player, profiles.getObject(name));
            player.setDisplayName(Utils.colorString(getDescriptor().getNickname()));
            player.setPlayerListName(Utils.colorString(getDescriptor().getNickname()));
            return true;
        }

        return false;
    }

    /**
     * Reloads the player's profile.
     * @return A boolean, TRUE on success, false otherwise.
     */
    public boolean reload() {
        return file.reload() && load();
    }

    /**
     * Disconnect the player from his profile without kicking him in case of a multiple profiles selection available.
     * @return A boolean, False if the player has been kicked, else true.
     */
    public boolean disconnect () {
        return Nb_DisconnectPlayer(player);
    }

    /**
     * Make the player disconnect from his profile and leave the server.
     */
    public void quit() {
        Nb_DisconnectPlayer(player);
    }

    /**
     * Gets the plugin permission's attachment.
     * @return the player permission attachment set by the {@link nb.essential.main.NbEssential plugin}.
     */
    PermissionAttachment getAttachment() {
        return attachment;
    }

    CraftPlayer getBukkitPlayer() {
        return player.getCraftPlayer();
    }

    /**
     * Gets the NbEssential player's representation.
     * @return A {@link CraftNbPlayer}.
     */
    CraftNbPlayer getPlayer() {
        return player;
    }

    /**
     * Gets the player {@link JSONFile JSON} file.
     * @return The player's file.
     */
    public JSONFile getFile() {
        return file;
    }

    /**
     * Gets the profile name.
     * @return the profile name;
     */
    public String getName() {
        return name;
    }

    /**
     * Gets if the current player's profile is an administrator profile.
     * @return A boolean, True if the player is connected as an admin.
     */
    public boolean isAdminProfile() {
        return descriptor.isOperator();
    }

    /**
     * Gets the player profile descriptor which contains an amount of methods to
     * simplify the data input/output with the {@link JSONFile player's file}.
     * @return A complete {@link ProfileDescriptor} based on the {@link JSONFile player's file}.
     */
    public ProfileDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Gets player speaking language
     * @return per default, FR_fr
     */
    public IServer.SupportedLanguage getLang() {
        return IServer.SupportedLanguage.FR_fr;
    }
}
