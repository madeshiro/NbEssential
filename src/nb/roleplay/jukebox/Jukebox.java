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

import nb.essential.player.NbPlayer;
import nb.essential.ressourcepack.Music;
import nb.essential.ressourcepack.SFX;

import static nb.essential.main.NbEssential.getDataLoader;

/**
 * Class of NbEssential
 */
public class Jukebox {

    private AmbianceData getAmbianceData() {
        return getDataLoader().getData("ambiance_points");
    }

    public static Music getMusic(String path) {
        return Music.List.getMusic(path);
    }

    public static SFX getSFX(String path) {
        return SFX.List.getSFX(path);
    }

    public static void Nb_JB_PlayMusic(Music music, NbPlayer player) {
        player.getMusicPlayer().playMusic(music);
    }

    public static void Nb_JB_PlayMusic(String music, NbPlayer player) {
        Music m = Music.getMusic(music);
        if (m != null)
            Nb_JB_PlayMusic(m, player);
    }
}
