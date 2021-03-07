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

import nb.essential.player.CraftNbPlayer;
import nb.essential.player.NbPlayer;
import nb.essential.ressourcepack.Music;
import nb.roleplay.jukebox.MusicPlayer;
import org.bukkit.SoundCategory;

/**
 * Class of NbEssential
 */
public final class IngameMusicPlayer extends MusicPlayer {

    private int schedulerTaskId;
    private static final SoundCategory channel = SoundCategory.RECORDS;

    public IngameMusicPlayer(NbPlayer player) {
        super(player);
    }

    @Override
    public void playMusic(Music music) {
        if (!assignedPlayer.getFile().<Boolean>get("info", "ingame-music"))
            return;

        if (isMusicPlayed())
            stopSound();

        this . currentMusic = music;
        this . playTimeMillis = System.currentTimeMillis();
        assignedPlayer.sendActionBarMsg("§cNow playing: §7" + music.getDescription().getName());
        assignedPlayer.playSound(assignedPlayer.getLocation(), currentMusic.getPath(), channel, 1000.0f, 1.0f);
    }

    @Override
    public void restartMusic() {
        if (isMusicPlayed()) {
            Music _currentMusic = currentMusic;

            stopSound();
            playMusic(_currentMusic);
        }
    }

    @Override
    public void stopSound() {
        if (isMusicPlayed()) {
            assignedPlayer.stopSound(currentMusic.getPath(), channel);
            currentMusic = null;
        }
    }
}
