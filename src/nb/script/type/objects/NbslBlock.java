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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import static nb.script.type.objects.NbslObject.OperatorFunction.Assign;
import static nb.script.type.objects.NbslObject.OperatorFunction.Equals;
import static nb.script.type.objects.NbslObject.OperatorFunction.Unequals;

public class NbslBlock extends NbslObject {

    private Block block;

    public NbslBlock(Block block) {
        this . block = block;
    }

    public NbslBlock(Location location) {
        this.block = location.getBlock();
    }

    public Block getBlock() {
        return block;
    }

    public Material getType() {
        return block.getType();
    }

    public void setType(Material material) {
        block.setType(material);
    }

    public void setType(Material material, boolean applyPhysics) {
        block.setType(material, applyPhysics);
    }

    @Override
    public Object onOperator(OperatorFunction function, Object obj) {
        assert obj instanceof NbslBlock;
        switch (function) {
            case Assign:
                block = ((NbslBlock) obj).block;
                return this;
            case Equals:
                return block.equals(((NbslBlock) obj).block);
            case Unequals:
                return !block.equals(((NbslBlock) obj).block);
            default:
                return null;
        }
    }

    @Override
    public OperatorFunction[] getSupportedOperators() {
        return new OperatorFunction[] {Assign, Equals, Unequals};
    }
}
