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

import nb.essential.zone.ZoneType;

import static nb.essential.zone.ZoneManager.Nb_ZN_IsZoneExists;

/**
 * Class of NbEssential
 */
public class Market extends House {

    public Market(String name) {
        super(name);
    }

    public static Market create(String name) {
        if (Nb_ZN_IsZoneExists(name, ZoneType.Market))
            return null;

        Market market = new Market(name);
        market.to_default();
        return market.save() ? market : null;
    }

    @Override
    public ZoneType getType() {
        return ZoneType.Market;
    }
}
