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
package nb.essential.zone;

import nb.essential.files.JSONFile;
import nb.essential.zone.zones.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

import static nb.essential.main.NbEssential.getDataLoader;

/**
 * Class of NbEssential
 */
public class ZoneManager {

    static ZoneData getData() {
        return getDataLoader().getData("zone");
    }

    public static JSONFile Nb_ZN_GetFileHandler() {
        return getData().zoneFile;
    }

    public static boolean Nb_ZN_Save() {
        return getData().save();
    }

    public static boolean Nb_ZN_Reload() {
        return getData().reload();
    }

    public static Zone Nb_ZN_GetZone(Location location) {
        //FIXME
        if (location.getBlock().getMetadata("zone").size() > 0)
            return Nb_ZN_GetZone(location.getBlock().getMetadata("zone").get(0).asString());
        else
            return Nb_ZN_GetZone(location.getWorld());
    }

    public static Zone Nb_ZN_GetZone(String fulldescription) {
        String[] splits = fulldescription.split(".");
        if (splits.length < 2)
            return null;
        ZoneType type = ZoneType.valueOf(splits[0]);

        return Nb_ZN_GetZone(splits[1], type);
    }

    public static Realm Nb_ZN_GetZone(World world) {
        return getData().realms.get(world.getName());
    }

    public static <E extends Zone> E Nb_ZN_GetZone(String name, ZoneType type) {
        switch (type) {
            case Realm:
                return (E) getData().realms.get(name);
            case Area:
                return (E) getData().areas.get(name);
            case House:
                return (E) getData().houses.get(name);
            case Market:
                return (E) getData().markets.get(name);
            case Dungeon:
                return null;
            default: return null;
        }
    }

    public static boolean Nb_ZN_IsZoneExists(String name, ZoneType type) {
        switch (type) {
            case Area:
                return getData().areas.containsKey(name);
            case Realm:
                return getData().realms.containsKey(name);
            case House:
                return getData().houses.containsKey(name);
            case Market:
                return getData().markets.containsKey(name);
            case Dungeon:
                return true;
            default:
                return false;
        }
    }

    public static boolean Nb_ZN_RemoveZone(String name, ZoneType type) {
        return Nb_ZN_IsZoneExists(name, type) && Nb_ZN_GetZone(name).remove();
    }

    public static Zone Nb_ZN_CreateZone(String name, ZoneType zoneType) {
        Zone zone = null;
        if (Nb_ZN_IsZoneExists(name, zoneType))
            return null;

        switch (zoneType) {
            case Realm:
                zone = Realm.create(name);
                if (Bukkit.getWorld(name) != null && !getData().realms.containsKey(name)) {
                    getData().realms.put(name, (Realm) zone);
                } else
                    return null;
                break;
            case Area:
                zone = Area.create(name);
                getData().areas.put(name, (Area) zone);
                break;
            case House:
                zone = House.create(name);
                getData().houses.put(name, (House) zone);
                break;
            case Market:
                zone = Market.create(name);
                getData().markets.put(name, (Market) zone);
                break;
            case Dungeon:
                // TODO ADD DUNGEON
                zone = Dungeon.create(name);
                break;
        }

        return zone;
    }

    public static ArrayList<Area> Nb_GetAreaList() {
        return new ArrayList<>(getData().areas.values());
    }

    public static ArrayList<House> Nb_GetHouseList() {
        return new ArrayList<>(getData().houses.values());
    }

    public static ArrayList<Market> Nb_GetMarketList() {
        return new ArrayList<>(getData().markets.values());
    }
}
