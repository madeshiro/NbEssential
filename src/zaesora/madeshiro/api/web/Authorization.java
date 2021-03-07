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

import nb.essential.player.CraftNbPlayer;
import nb.essential.player.ProfileDescriptor;
import zaesora.madeshiro.parser.json.JSONObject;

import java.util.UUID;

/**
 * Class of NbEssential
 */
public class Authorization {
    private final String code;
    private final String player;
    private long time;

    private static final long timeout = 500_000; // 500 secs

    Authorization(String code, CraftNbPlayer player) {

        this . code = code;
        this . player = player.getComposedNickname();
        this  . time = System.currentTimeMillis();

        ((ProfileDescriptor.OnlineProfileHandler)player.getProfileDescriptor().getHandler()).authorization = this;
    }

    public boolean isValid() {
        return System.currentTimeMillis() - time <= timeout;
    }

    public long getTime() {
        return time;
    }

    public long getRemaining() {
        return timeout - (System.currentTimeMillis() - time);
    }

    public boolean update() {
        boolean rtn;

        if (rtn = isValid())
            time = System.currentTimeMillis();

        return rtn;
    }

    public final String getCode() {
        return code;
    }

    public static Authorization generate(CraftNbPlayer player) {
        return new Authorization(UUID.randomUUID().toString(), player);
    }

    JSONObject getPayload() {
        JSONObject object = new JSONObject();
        object.addJValue("player", player);
        object.addJValue("code", code);

        return object;
    }
}
