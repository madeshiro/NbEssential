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
package nb.essential.zone.zones;

import nb.essential.player.CraftNbPlayer;
import nb.essential.player.NbPlayer;
import nb.essential.ressourcepack.Music;
import nb.essential.zone.Zone;
import nb.essential.zone.ZoneType;
import nb.roleplay.jukebox.MusicPlayer;
import nb.roleplay.jukebox.Programmation;

import static nb.essential.main.NbEssential.*;
import static nb.essential.zone.ZoneManager.Nb_ZN_IsZoneExists;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class of NbEssential
 */
public class Realm extends Zone {

    public Realm(String world) {
        super(world);
    }

    public static Realm create(String name) {
        if (Nb_ZN_IsZoneExists(name, ZoneType.Realm))
            return null;

        Realm realm = new Realm(name);
        realm.to_default();
        return realm.save() ? realm : null;
    }

    @Override
    public ZoneType getType() {
        return ZoneType.Realm;
    }

    @Override
    protected boolean delimit() {
        return true;
    }

    @Override
    protected boolean undelimit() {
        return true;
    }

    @Override
    protected boolean remove() {
        return false;
    }

    @Override
    public int getSignificiance() {
        return -1;
    }

    public class MusicProgrammation extends Programmation {

        private int taskId = -1;

        protected MusicProgrammation(MusicPlayer parent) {
            super(parent);
        }

        @Override
        protected void _cancel() {
            if (taskId < 0) return;
            // if (isWebPlayer()) { }
            // else
            getScheduler().scheduleCancelTask(taskId);
            parent.stopSound();
        }

        @Override
        protected void _start() {
            if (Realm.this.getMusics().size() == 0)
                cancel();
            else
                taskId = getScheduler().scheduleSyncDelayedTask(taskId1 -> play(), 80);
        }

        private void play() {
            final Music selection = select(Realm.this, getAssignedPlayer());
            parent.playMusic(selection);

            taskId = getScheduler().scheduleSyncDelayedTask(taskId1 -> play(), 20*(selection.getDuration()+3));
        }
    }

    private static Music select(Realm realm, NbPlayer player) {
        ArrayList<Music.List> fromBiome = new ArrayList<>(), musics = new ArrayList<>();
        for (String m : realm.getMusics()) {
            Music music;
            if ((music = Music.getMusic(m)) != null)
                musics.add((Music.List) music);
        }

        for (Music.List m : musics)
            if (m.isAdaptedTo(player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ())))
                fromBiome.add(m);

        return fromBiome.isEmpty() ? musics.get(randomInt(musics.size())) : fromBiome.get(randomInt(fromBiome.size()));
    }

    private static int randomInt(int max) {
        return ThreadLocalRandom.current().nextInt(0,max);
    }

    @Override
    protected MusicProgrammation getMusicProgrammation(final CraftNbPlayer player) {
        return new MusicProgrammation(player.getMusicPlayer());
    }
}
