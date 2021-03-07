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

import nb.essential.main.NbEssential;
import nb.essential.player.CraftNbPlayer;
import nb.essential.player.NbPlayer;
import nb.essential.ressourcepack.Music;
import nb.essential.zone.Zone;
import nb.essential.zone.ZoneType;
import nb.roleplay.jukebox.MusicPlayer;
import nb.roleplay.jukebox.Programmation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import static nb.essential.main.NbEssential.getScheduler;
import static nb.essential.zone.ZoneManager.*;

/**
 * Class of NbEssential
 */
public class Area extends Zone {

    protected int significiance;
    private HashSet<Coordinates> coordinates;

    public Area(String name) {
        super(name);

        this . coordinates = new HashSet<>();
    }

    @Override
    public boolean load() {
        if (super.load()) {
            for (Object obj : jsonData.<JSONArray>getObject("coordinates")) {
                if (obj instanceof JSONObject)
                    coordinates.add(new Coordinates((JSONObject) obj));
            }

            return delimit();
        } else return false;
    }

    @Override
    public ZoneType getType() {
        return ZoneType.Area;
    }

    @Override
    protected boolean delimit() {
        for (Coordinates c : this.coordinates) {
            for (int x = c.xMin; x <= c.xMax; x++) {
                for (int y = c.yMin; y <= c.yMax; y++) {
                    for (int z = c.zMin; z <= c.zMax; z++) {
                        Block b = c.world.getBlockAt(x, y, z);
                        if (!b.hasMetadata("zone") ||
                                Nb_ZN_GetZone(b.getMetadata("zone").get(0).asString()).getSignificiance() < significiance) {
                            b.setMetadata("zone", new FixedMetadataValue(NbEssential.getPlugin(), toString()));
                        }
                    }
                }
            }
        }


        return true;
    }

    public void addCoordinates(Coordinates... coordinates) {
        for (Coordinates c : coordinates) {
            if (this.coordinates.add(c))
                delimit(c);
        }
    }

    protected final void delimit(Coordinates c) {
        for (int x = c.xMin; x <= c.xMax; x++) {
            for (int y = c.yMin; y <= c.yMin; y++) {
                for (int z = c.zMin; z <= c.zMax; z++) {
                    Block b = c.world.getBlockAt(x, y, z);
                    if (!b.hasMetadata("zone") ||
                            Nb_ZN_GetZone(b.getMetadata("zone").get(0).asString()).getSignificiance() < significiance)
                        b.setMetadata("zone", new FixedMetadataValue(NbEssential.getPlugin(), toString()));
                }
            }
        }
    }

    @Override
    protected boolean undelimit() {
        try {
            for (Coordinates c : this.coordinates) {
                for (int x = c.xMin; x <= c.xMax; x++) {
                    for (int y = c.yMin; y <= c.yMax; y++) {
                        for (int z = c.zMin; z <= c.zMax; z++) {
                            Block b = c.world.getBlockAt(x, y, z);
                            if (b.hasMetadata("zone") && b.getMetadata("zone").get(0).asString().equals(toString()))
                                b.removeMetadata("zone", NbEssential.getPlugin());
                        }
                    }
                }
            }
        } catch(Exception ignored) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean remove() {
        return Nb_ZN_GetFileHandler().save() && undelimit();
    }

    @Override
    public int getSignificiance() {
        return significiance;
    }

    @Override
    protected Programmation getMusicProgrammation(CraftNbPlayer player) {
        return new MusicProgrammation(player.getMusicPlayer());
    }

    public static Area create(String name) {
        if (Nb_ZN_IsZoneExists(name, ZoneType.Area))
            return null;

        Area area = new Area(name);
        area.to_default();
        return area.save() ? area : null;
    }

    public static class Coordinates {
        private int xMin, xMax, yMin, yMax, zMin, zMax;
        private World world;

        public Coordinates(Location loc1, Location loc2) {
            if (!loc1.getWorld().equals(loc2.getWorld()))
                throw new IllegalArgumentException();

            this . xMin = Math.min(loc1.getBlockX(), loc2.getBlockX());
            this . xMax = Math.max(loc1.getBlockX(), loc2.getBlockX());
            this . yMin = Math.min(loc1.getBlockY(), loc2.getBlockY());
            this . yMax = Math.max(loc1.getBlockY(), loc2.getBlockY());
            this . zMin = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            this . zMax = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
            this . world = loc1.getWorld();
        }

        public Coordinates(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, World world) {
            this . xMin = xMin;
            this . xMax = xMax;
            this . yMin = yMin;
            this . yMax = yMax;
            this . zMin = zMin;
            this . zMax = zMax;
            this . world = world;
        }

        public Coordinates(JSONObject coord) {
            xMin = coord.<Long>getObject("xmin").intValue();
            xMax = coord.<Long>getObject("xmax").intValue();
            yMin = coord.<Long>getObject("ymin").intValue();
            yMax = coord.<Long>getObject("ymax").intValue();
            zMax = coord.<Long>getObject("zmin").intValue();
            zMin = coord.<Long>getObject("zmax").intValue();

            world = Bukkit.getWorld(coord.<String>getObject("world"));
            if (world == null)
                throw new IllegalArgumentException("Invalid world in coordinates specified !");
        }

        public int getXMax() {
            return xMax;
        }

        public int getXMin() {
            return xMin;
        }

        public int getYMin() {
            return yMin;
        }

        public int getYMax() {
            return yMax;
        }

        public int getZMin() {
            return zMin;
        }

        public int getZMax() {
            return zMax;
        }

        @Override
        public String toString() {
            return xMin + ":" + yMin + ":" + zMin + ":" + xMax + ":" + yMax + ":" + zMax + ":" + world.getName();
        }

        @Override
        public int hashCode() {
            int hash = 1;

            hash = hash * 13 + xMin;
            hash = hash * 13 + xMax;
            hash = hash * 13 + yMin;
            hash = hash * 13 + yMax;
            hash = hash * 13 + zMin;
            hash = hash * 13 + zMax;

            return hash;
        }

        public JSONObject getData() {
            JSONObject object = new JSONObject();
            object.put("xmin", xMin);
            object.put("xmax", xMax);
            object.put("ymin", yMin);
            object.put("ymax", yMax);
            object.put("zmin", zMin);
            object.put("zmax", zMax);
            object.put("world", world.getName());

            return object;
        }
    }

    public class MusicProgrammation extends Programmation {

        private int taskId = -1;
        protected MusicProgrammation(MusicPlayer parent) {
            super(parent);
        }

        @Override
        protected void _cancel() {
            parent.stopSound();
            if (taskId < 0) return;

            getScheduler().scheduleCancelTask(taskId);
        }

        @Override
        protected void _start() {
            if (Area.this.getMusics().size() == 0)
                cancel();
            else
                taskId = getScheduler().scheduleSyncDelayedTask(taskId1 -> play(), 80);
        }

        private void play() {
            final Music selection = select(Area.this, getAssignedPlayer());
            parent.playMusic(selection);

            taskId = getScheduler().scheduleSyncDelayedTask(taskId1 -> play(), 20*(selection.getDuration()+4));
        }
    }

    private static Music select(Area realm, NbPlayer player) {
        ArrayList<Music.List> musics = new ArrayList<>();
        for (String m : realm.getMusics()) {
            Music music;
            if ((music = Music.getMusic(m)) != null)
                musics.add((Music.List) music);
        }

        assert musics.size() > 0;
        return musics.get(ThreadLocalRandom.current().nextInt(0, musics.size()));
    }
}
