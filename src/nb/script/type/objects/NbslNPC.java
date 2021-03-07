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

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

import static nb.script.type.objects.NbslObject.OperatorFunction.Assign;
import static nb.script.type.objects.NbslObject.OperatorFunction.Equals;
import static nb.script.type.objects.NbslObject.OperatorFunction.Unequals;

public class NbslNPC extends NbslObject {
    
    private NPC npc;
    
    public NbslNPC(NPC npc, int tag) {
        this.npc = npc;

        if (npc.hasTrait(NbslNPCTrait.class)) {
            npc.getTrait(NbslNPCTrait.class).tag = tag;
        } else {
            npc.addTrait(new NbslNPCTrait().a(tag));
        }
    }

    protected NbslNPC(int tag) {
        for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
            if (npc.hasTrait(NbslNPCTrait.class)) {
                if (npc.getTrait(NbslNPCTrait.class).tag == tag)
                    this.npc = npc;
            }
        }
    }

    public static NbslNPC GetNbslNPC(int tag) {
        NbslNPC npc = new NbslNPC(tag);
        return npc.npc != null ? npc : null;
    }

    public NPC getNpc() {
        return npc;
    }

    @Override
    public Object onOperator(OperatorFunction function, Object obj) {
        assert obj instanceof NbslNPC;
        switch (function) {
            case Assign:
                npc = ((NbslNPC) obj).npc;
                return this;
            case Equals:
                return npc.equals(((NbslNPC) obj).npc);
            case Unequals:
                return !npc.equals(((NbslNPC) obj).npc);
            default:
                return null;
        }
    }

    @Override
    public OperatorFunction[] getSupportedOperators() {
        return new OperatorFunction[] {Assign, Equals, Unequals};
    }

    @TraitName("nbsl-npctrait")
    public class NbslNPCTrait extends Trait {

        @Persist("a-identifier")
        private int tag;

        public NbslNPCTrait() {
            super("nbsl-npctrait");
        }

        private NbslNPCTrait a(int tag) {
            this.tag = tag;
            return this;
        }

        @Override
        public void load(DataKey data) {
        }

        @Override
        public void save(DataKey data) {
        }

        @Override
        public void onAttach() {
        }

        @Override
        public void run() {
        }
    }
}
