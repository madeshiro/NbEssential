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
import nb.essential.files.ResourceFile;
import nb.essential.loader.Loader;
import nb.essential.loader.LoaderPriority;
import nb.essential.loader.PluginData;
import nb.essential.player.CraftNbPlayer;
import nb.essential.player.NbPlayer;
import nb.essential.player.PlayerSystem;
import nb.essential.zone.zones.*;
import zaesora.madeshiro.parser.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import static nb.essential.zone.ZoneManager.Nb_ZN_GetZone;

/**
 * Class of NbEssential
 */
@Loader(value = "zone", priority = LoaderPriority.High)
public class ZoneData implements PluginData {

    public final String getName() {
        return "ZoneManager";
    }

    JSONFile zoneFile;

    HashMap<String, Area> areas = new HashMap<>();
    HashMap<String, House> houses = new HashMap<>();
    HashMap<String, Market> markets = new HashMap<>();
    HashMap<String, Realm> realms = new HashMap<>();

    private boolean saveZones(HashMap<String, ? extends Zone> zones) {
        boolean status = true;
        for (Zone z : zones.values()) {
            if (z.save()) {
                loginfo("Save {" + z.toString() + "#" + z.getType().toString() + "}");
            } else {
                logsevere("Unable to save {" + z.toString() + "#" + z.getType().toString() + "}");
                status = false;
            }
        }
        return status;
    }

    private boolean reloadZones(HashMap<String, ? extends Zone> zones) {
        boolean status = true;
        for (Zone z : zones.values()) {
            if (!z.reload()) {
                status = false;
                logwarning("Unable to reload zone {" + z.toString() + "#" + z.getType().toString() + "}");
            } else
                loginfo("Reload zone {" + z.toString() + "#" + z.getType().toString() + "}");
        }

        return status;
    }

    private boolean loadZones(ZoneType type) {
        assert type != null;
        boolean status = true;

        for (Object objkey : zoneFile.<JSONObject>get(type.toString()+"s").keySet()) {
            Zone z = type.instanciate(objkey.toString());
            if (z != null && z.load()) {
                switch (type) {
                    case Realm:
                        realms.put(objkey.toString(), (Realm) z);
                        break;
                    case Area:
                        areas.put(objkey.toString(), (Area) z);
                        break;
                    case House:
                        houses.put(objkey.toString(), (House) z);
                        break;
                    case Market:
                        markets.put(objkey.toString(), (Market) z);
                        break;
                    case Dungeon:
                        break;
                }
                loginfo("Load zone: {"+z.toString() + "#" + z.getType().toString() + "}");
            } else {
                logwarning("Unable to load zone: {" + objkey.toString() + "#" + type.toString() + "}");
                status = false;
            }
        }

        return status;
    }

    /**
     * Called to save this data of this database
     *
     * @return A boolean, true if the save action ended with success, false otherwise.
     */
    @Override
    public boolean save() {
        boolean status = saveZones(areas);

        status = saveZones(houses) && status;
        status = saveZones(markets) && status;
        status = saveZones(realms) && status;

        return status;
    }

    /**
     * Called to load the data from file and store it in this database.
     * This function is often called by the {@link #reload()} function.
     *
     * @return A boolean, true if the load action ended with success, false otherwise.
     */
    @Override
    public boolean load() {
        File file = new File("plugins/NbEssential/zones.json");
        if (!file.exists() &&
                !ResourceFile.copyFromResource("zones.json", "plugins/NbEssential/zones.json"))
            return false;

        if ((zoneFile = new JSONFile(file)).load()) {
            boolean status = true;
            for (ZoneType type : ZoneType.values()) {
                status = loadZones(type) && status;
            }

            return status;
        } else return false;
    }

    /**
     * Called to reload data.
     *
     * @return A boolean, true if the reload action ended with success, false otherwise.
     */
    @Override
    public boolean reload() {
        if (!zoneFile.reload())
            return false;

        boolean status = reloadZones(realms);
        status = reloadZones(areas) && status;
        status = reloadZones(houses) && status;
        status = reloadZones(markets) && status;


        if (status) {
            for (NbPlayer player : PlayerSystem.Nb_GetPlayers())
                Nb_ZN_GetZone(player.getLocation()).attend((CraftNbPlayer) player);
            return true;
        } else
            return false;
    }

    private Area operator_new_zone(String area, String type) {
        ZoneType zoneType = null;
        for (ZoneType zt : ZoneType.values()) {
            if (zt.toString().equalsIgnoreCase(type)) {
                zoneType = zt;
                break;
            }
        }
        switch (zoneType) {
            case Area:
                return new Area(area);
            case House:
                return new House(area);
            case Market:
                return new Market(area);
            case Dungeon:
                return new Dungeon(area);
            default:
                return null;
        }
    }
}
