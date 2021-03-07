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
import nb.essential.player.NbPlayer;
import nb.essential.player.PlayerSystem;
import nb.essential.ressourcepack.Music;
import nb.roleplay.jukebox.Programmation;
import nb.roleplay.jukebox.player.IngameMusicPlayer;
import nb.roleplay.jukebox.player.WebsiteMusicPlayer;
import zaesora.madeshiro.api.GenericPacket;
import zaesora.madeshiro.api.web.GenericWebPacket;
import zaesora.madeshiro.api.web.WebPacket;
import zaesora.madeshiro.parser.json.JSONObject;

/**
 * Class of NbEssential
 */
public class RequestMusic extends GenericWebPacket {

    private CraftNbPlayer player;

    public RequestMusic() {
        super();
    }

    private boolean checkIp(String ip) {
        for (NbPlayer player : PlayerSystem.Nb_GetPlayers()) {
            if (player.getAddress().getHostName().equals(ip)) {
                this.player = (CraftNbPlayer) player;
                return true;
            }
        }

        return false;
    }

    private boolean checkPlayer(String nickname) {
        for (NbPlayer player : PlayerSystem.Nb_GetPlayers()) {
            if (player.getComposedNickname().equals(nickname)) {
                this.player = (CraftNbPlayer) player;
                return true;
            }
        }

        return false;
    }

    private void checkMusicPlayer() {
        if (player.getMusicPlayer() instanceof IngameMusicPlayer)
        {
            Programmation music = player.getMusicPlayer().getProgrammation();
            player.getMusicPlayer().cancelProgrammation();
            player.getMusicPlayer().stopSound();


            player.setMusicPlayer(new WebsiteMusicPlayer(player));
            music.setParent(player.getMusicPlayer());
            player.getMusicPlayer().setProgrammation(music);
        }
    }

    @Override
    public void _treatRequest() {
        if (checkPlayer((String) object.get("player")) || checkIp((String) object.get("ip"))) {
            if (player.getProfileDescriptor().getOnlineHandler().authorization.update() &&
                    player.getProfileDescriptor().getOnlineHandler().authorization.getCode().equals(object.get("playerkey"))) {

                checkMusicPlayer();

                Music music = ((WebsiteMusicPlayer) player.getMusicPlayer()).GetInstruction();
                JSONObject object = new JSONObject();
                object.put("action", music == null ? "up-to-date" : (music == Music.List.stopMusic ? "stop" : "change"));

                if (music != null && music != Music.List.stopMusic) {
                    object.put("path", music.getPath());
                    JSONObject about = new JSONObject();
                    {
                        about.put("title", music.getDescription().getName());
                        about.put("author", music.getDescription().getAuthor());
                        about.put("album", music.getDescription().getAlbum());
                    }

                    object.put("about", about);
                }

                out.setOutContent(GenericPacket.writeJSON(object));
                out.setOutHttpStatus("200 OK");
            } else
                out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.PlayerKey).out();
        } else
            out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.InvalidPlayer).out();
    }
}
