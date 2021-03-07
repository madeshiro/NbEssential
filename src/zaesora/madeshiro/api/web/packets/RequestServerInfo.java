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

package zaesora.madeshiro.api.web.packets;

import nb.essential.main.NbEssential;
import org.bukkit.Bukkit;
import zaesora.madeshiro.api.GenericPacket;
import zaesora.madeshiro.api.web.GenericWebPacket;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONObject;

/**
 * Class of NbEssential
 */
public class RequestServerInfo extends GenericWebPacket {

    public RequestServerInfo() {
        super();
    }

    @Override
    public void _treatRequest() {
        JSONObject object = new JSONObject();
        object.put("name", NbEssential.getServer().getName());
        object.put("version", NbEssential.getServer().getMCServer().getVersion());
        object.put("connected", Bukkit.getOnlinePlayers().size());
        object.put("slots", Bukkit.getMaxPlayers());
        object.put("operators", new JSONArray());

        out.setOutContent(GenericPacket.writeJSON(object));
    }
}
