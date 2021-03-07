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

package zaesora.madeshiro.api.web;

import zaesora.madeshiro.api.GenericPacket;
import zaesora.madeshiro.api.web.packets.*;

/**
 * Class of NbEssential
 */
public enum RequestType {
    invalid("", InvalidRequest.class),

    music("music", RequestMusic.class),
    session("session", RequestSession.class),
    player_info("player", RequestPlayerInfo.class),
    server_info("server", RequestServerInfo.class);

    private String uri;
    private Class<? extends GenericPacket> cls;
    RequestType(String uri, Class<? extends GenericPacket> cls) {
        this . uri = '/' + uri;
        this . cls = cls;
    }

    public GenericPacket generate() {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static RequestType getRequestByURI(String uriWithSlash) {
        for (RequestType request : RequestType.values())
            if (request.uri.equals(uriWithSlash))
                return request;
        return invalid;
    }
}
