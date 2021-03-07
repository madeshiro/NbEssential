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

import nb.essential.player.CraftNbPlayer;
import nb.essential.player.PlayerSystem;
import zaesora.madeshiro.api.GenericPacket;
import zaesora.madeshiro.api.web.Authorization;
import zaesora.madeshiro.api.web.GenericWebPacket;
import zaesora.madeshiro.api.web.WebPacket;
import zaesora.madeshiro.parser.json.JSONObject;

/**
 * Class of NbEssential
 */
public class RequestSession extends GenericWebPacket {

    public RequestSession() {
        super();
    }

    @Override
    public void _treatRequest() {
        CraftNbPlayer player = (CraftNbPlayer) PlayerSystem.Nb_GetPlayerByIp((String) object.get("player"));

        if (player == null) {
            out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.InvalidPlayer).out();
        } else {
            JSONObject response = new JSONObject();
            response.put("name", player.getComposedNickname());

            switch (((String) object.get("action")).toLowerCase()) {
                case "create":
                    player.getProfileDescriptor().getOnlineHandler().authorization = Authorization.generate(player);
                    response.put("uid", player.getProfileDescriptor().getOnlineHandler().authorization.getCode());
                    break;
                case "validate":
                    Authorization auth = player.getProfileDescriptor().getOnlineHandler().authorization;
                    if (auth.getCode().equals(object.get("key"))) {
                        response.put("timeout", auth.getRemaining());
                        if (auth.update()) {
                            response.put("status", "ok");
                        } else
                            response.put("status", "expired");
                    } else
                        response.put("status", "invalid");
                    break;
                default:
                    out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.BadRequest).out();
                    return;
            }

            out.setOutContent(GenericPacket.writeJSON(response));
        }
    }
}
