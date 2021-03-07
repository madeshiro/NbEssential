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

import nb.essential.player.CraftNbPlayer;
import nb.roleplay.jukebox.player.WebsiteMusicPlayer;

/**
 * Class of NbEssential
 */
public abstract class Programmation  {

    private boolean running;
    protected MusicPlayer parent;
    protected Programmation(MusicPlayer parent) {
        this . parent = parent;
    }

    protected final CraftNbPlayer getAssignedPlayer() {
        return (CraftNbPlayer) parent.assignedPlayer;
    }

    public final boolean isWebPlayer() {
        return parent instanceof WebsiteMusicPlayer;
    }

    public final void cancel() {
        if (running) {
            running = false;
            _cancel();
        }
    }
    public final void start() {
        if (!running) {
            running = true;
            _start();
        } else
            restart();
    }

    public void restart() {
        if (running)
            cancel();
        start();
    }

    public void setParent(MusicPlayer parent) {
        if (parent != null)
            this.parent = parent;
    }

    public boolean isRunning() {
        return running;
    }

    protected abstract void _cancel();
    protected abstract void _start();
}
