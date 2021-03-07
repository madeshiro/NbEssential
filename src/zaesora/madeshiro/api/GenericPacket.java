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

package zaesora.madeshiro.api;

import zaesora.madeshiro.parser.json.JSONObject;
import zaesora.madeshiro.parser.json.JSONValue;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Class of NbEssential
 */
public interface GenericPacket {

    void setInputPacket(Packet packet);

    Packet in();
    Packet out();

    boolean treatRequest();

    static boolean CheckWebsiteID(JSONObject object) {
        return JSONAPI.Nb_JAPI_VerifyWebsiteID((String) object.get("webkey"));
    }

    static String writeJSON(JSONValue content) {
        StringWriter writer = new StringWriter();
        try {
            content.write(writer);
        } catch (IOException e) {
            return "{\"ErrorRequest\":\"Internal Error\", \"ErrorType\": -1}";
        }

        return writer.toString();
    }
}
