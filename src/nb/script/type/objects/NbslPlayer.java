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

package nb.script.type.objects;

import nb.essential.player.CraftNbPlayer;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;

import static nb.script.type.objects.NbslObject.OperatorFunction.Assign;
import static nb.script.type.objects.NbslObject.OperatorFunction.Equals;
import static nb.script.type.objects.NbslObject.OperatorFunction.Unequals;

public class NbslPlayer extends NbslObject {

    private CraftNbPlayer nbPlayer;

    public NbslPlayer(final CraftNbPlayer player) {
        this.nbPlayer = player;
    }

    public CraftNbPlayer getNbPlayer() {
        return nbPlayer;
    }

    public CraftPlayer getBukkitPlayer() {
        return nbPlayer.getBukkitPlayer();
    }

    public void sendMessage(String msg) {
        nbPlayer.sendMessage(msg);
    }

    public void sendRawMessage(String raw) {
        nbPlayer.sendRawMessage(raw);
    }

    @Override
    public Object onOperator(OperatorFunction function, Object obj) {
        assert obj instanceof NbslPlayer;
        switch (function) {
            case Assign:
                nbPlayer = ((NbslPlayer) obj).nbPlayer;
                return this;
            case Equals:
                return nbPlayer.equals(((NbslPlayer) obj).nbPlayer);
            case Unequals:
                return !nbPlayer.equals(((NbslPlayer) obj).nbPlayer);
            default:
                return null;
        }
    }

    @Override
    public OperatorFunction[] getSupportedOperators() {
        return new OperatorFunction[] {Assign, Equals, Unequals};
    }
}
