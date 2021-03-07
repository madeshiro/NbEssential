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
package nb.script.commands;

import zaesora.madeshiro.parser.json.JSONObject;

/**
 * Class of NbEssential in package nb.script.commands
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2
 */
public class ScriptValues {
    private JSONObject nbsldata;
    public ScriptValues(JSONObject data) {
        nbsldata = data;
    }

    public void putData(JSONObject profileDescriptor) {
        profileDescriptor.<JSONObject>getObject("state").put("nbsl",nbsldata);
    }

    public Integer getValue(String identifier) {
        return nbsldata.getObject("values", identifier);
    }

    public void setValue(String identifier, int value) {
        nbsldata.<JSONObject>getObject("values").put(identifier, value);
    }
}
