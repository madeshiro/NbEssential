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

import nb.script.type.objects.NbslNPC;

import static nb.script.ScriptManager.Nb_SL_LogWarning;

public interface NpcFunction {

    static void SendMessage(NbslNPC npc, String message, int intensity) {
        switch (intensity) {
            case 0: // whisper

                break;
            case 1: // speak normally

                break;
            case 2: // cry

                break;
            default:
                Nb_SL_LogWarning("Invalid intensity sended (".concat(Integer.toString(intensity)).concat(") in NpcFunction#SendMessage !"));
                break;
        }
    }
}
