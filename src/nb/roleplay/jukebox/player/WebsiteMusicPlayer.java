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

package nb.roleplay.jukebox.player;

import nb.essential.player.NbPlayer;
import nb.essential.ressourcepack.Music;
import nb.roleplay.jukebox.MusicPlayer;

/**
 * Class of NbEssential
 */
public class WebsiteMusicPlayer extends MusicPlayer {

    private Music to_play;

    public WebsiteMusicPlayer(NbPlayer player) {
        super(player);
    }


    @Override
    public void playMusic(Music music) {
        to_play = music;
    }

    @Override
    public void restartMusic() {
        to_play = currentMusic;
    }

    @Override
    public void stopSound() {
        to_play = Music.List.stopMusic;
    }

    public Music GetInstruction() {
        this.currentMusic = to_play;
        this.playTimeMillis = System.currentTimeMillis();

        to_play = null;

        return currentMusic;
    }
}
