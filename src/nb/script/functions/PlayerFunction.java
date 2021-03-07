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
package nb.script.functions;

import nb.essential.player.NbPlayer;
import nb.script.type.objects.NbslPlayer;

import java.util.Collection;

/**
 * Class of NbEssential in package nb.script.functions
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
interface PlayerFunction {

    static void SendMessage(Collection<NbslPlayer> players, String text) {
        for (NbslPlayer player : players)
            player.sendMessage(text);
    }

    static void SendRawMessage(Collection<NbslPlayer> players, String text) {
        for (NbslPlayer player : players)
            player.sendRawMessage(text);
    }
}
