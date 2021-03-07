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
import nb.essential.zone.Zone;
import nb.roleplay.jukebox.MusicPlayer;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 17/09/2016
 */
@SuppressWarnings("deprecation")
public interface NbPlayer extends Player, BasicNbPlayer {

    CraftPlayer getBukkitPlayer();

    String getNickname();

    String getPrefix();

    Eldar getMoney();

    JSONFile getFile();

    void sendTlMessage(String msg);

    void sendActionBarMsg(String msg);

    void resetTitle();

    void sendTitle(String title, String subtitle);

    void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

    void reloadInnerClass();

    void stopSound();

    void quit();

    void pardon();

    boolean disconnect();

    void kick(String msg);

    void ban(String reason);

    PermissionGroup getPermissionGroup();

    boolean hasPermissionGroup();

    Zone getZoneLocation();

    MusicPlayer getMusicPlayer();

    void setMusicPlayer(MusicPlayer musicPlayer);

    void setTabListHeaderFooter(String header, String footer);

    ProfileDescriptor getProfileDescriptor();
}
