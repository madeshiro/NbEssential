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

/**
 * Class of NbEssential
 */
public abstract class MusicPlayer {

    protected Music currentMusic;
    protected long playTimeMillis;

    protected NbPlayer assignedPlayer;
    private Programmation playerProgrammation;

    public MusicPlayer(NbPlayer player) {
        this . assignedPlayer = player;
    }

    public abstract void playMusic(Music music);
    public abstract void restartMusic();
    public abstract void stopSound();

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public long getElapsedPlayTimeMillis() {
        return System.currentTimeMillis() - playTimeMillis;
    }

    public long getElapsedPlayTimeSeconds() {
        return getElapsedPlayTimeMillis() / 1000;
    }

    public long getElapsedPlayTimeMinutes() {
        return getElapsedPlayTimeSeconds() / 60;
    }

    public boolean isMusicPlayed() {
        return currentMusic != null
                && getElapsedPlayTimeMillis() < (currentMusic.getDuration()*1000);
    }

    public void setProgrammation(Programmation programmation) {
        if (hasProgrammation())
            this.playerProgrammation.cancel();

        this . playerProgrammation = programmation;
        this . playerProgrammation . start();
    }

    public boolean hasProgrammation() {
        return playerProgrammation != null && playerProgrammation.isRunning();
    }

    public void cancelProgrammation() {
        playerProgrammation.cancel();
    }

    public Programmation getProgrammation() {
        return playerProgrammation;
    }

    protected NbPlayer getAssignedPlayer() {
        return assignedPlayer;
    }
}
