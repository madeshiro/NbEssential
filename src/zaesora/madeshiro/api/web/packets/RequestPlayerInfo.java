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

import nb.essential.player.*;
import nb.essential.utils.Utils;
import zaesora.madeshiro.api.GenericPacket;
import zaesora.madeshiro.api.web.GenericWebPacket;
import zaesora.madeshiro.api.web.WebPacket;
import zaesora.madeshiro.parser.json.JSONObject;

import static nb.essential.player.PlayerSystem.Nb_GetOfflinePlayer;

/**
 * Class of NbEssential
 */
public class RequestPlayerInfo extends GenericWebPacket {

    public RequestPlayerInfo() {
        super();
    }

    public WebPacket createPacket(BasicNbPlayer player) {
        if (player instanceof OfflineNbPlayer)
            return createOffPacket((OfflineNbPlayer) player);
        else
            return createOnPacket((CraftNbPlayer) player);
    }

    private WebPacket createOffPacket(OfflineNbPlayer player) {
        WebPacket packet = new WebPacket();
        JSONObject object = new JSONObject();
        {
            object.put("name", player.getName());
            object.put("uuid", player.getUniqueId().toString());
            object.put("nickname", Utils.uncolorString(Utils.uncolorString(player.getNickname()), '&'));
            object.put("group", player.getPermissionGroup().getName());
            object.put("money", player.getMoney().getAmount());
            object.put("job", new JSONObject());
            object.put("connect", false);
        }

        packet.setOutContent(GenericPacket.writeJSON(object));
        if (packet.getHttpOutContent().contains("Internal Error"))
            packet.setOutHttpStatus("500 Internal Error");
        else
            packet.setOutHttpStatus("200 OK");
        return packet;
    }

    private WebPacket createOnPacket(CraftNbPlayer player) {
        WebPacket packet = new WebPacket();
        JSONObject object = new JSONObject();
        {
            object.put("name", player.getName());
            object.put("uuid", player.getUniqueId().toString());
            object.put("nickname", Utils.uncolorString(Utils.uncolorString(player.getNickname()), '&'));
            object.put("group", player.getPermissionGroup().getName());
            object.put("money", player.getMoney().getAmount());
            object.put("job", new JSONObject());
            object.put("connect", true);
            object.put("ip", player.getAddress().getHostString());
            object.put("location", player.getZoneLocation().getDisplayName());
        }

        packet.setOutContent(GenericPacket.writeJSON(object));
        if (packet.getHttpOutContent().contains("Internal Error"))
            packet.setOutHttpStatus("500 Internal Error");
        else
            packet.setOutHttpStatus("200 OK");
        return packet;
    }

    @Override
    public void _treatRequest() {
        String nickname = (String) this.object.get("player");
        BasicNbPlayer player = PlayerSystem.Nb_GetPlayer(nickname);
        player = player == null ? Nb_GetOfflinePlayer(nickname) : player;

        out = player == null
            ? (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.InvalidPlayer).out()
                : createPacket(player);
    }
}
