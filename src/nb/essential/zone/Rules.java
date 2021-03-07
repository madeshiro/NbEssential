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
package nb.essential.zone;

import nb.essential.player.NbPlayer;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONObject;

/**
 * Class of NbEssential
 */
// TODO YAML/JSON
public class Rules {

    private JSONObject jsonData;

    Rules(Zone zone) {
        jsonData = zone.jsonData.getObject("rules");
    }

    public enum Rule {
        PlayerInterract(0, "player", "interract", "auth"),
        PlayerBreakBlock(0, "player", "breakblock", "auth"),
        PlayerEnter(0, "player", "enter", "auth"),
        PlayerExit(0, "player", "exit", "auth"),
        PlayerEnderpearl(0, "player", "enderpearl", "auth"),

        Jobzone(1, "jobzone"),
        PVP(1, "pvp"),
        PVE(1, "pve"),

        TNT_BlockDetonation(2, "tnt", "block-detonation"),
        TNT_BlockDamage(2, "tnt", "block-damage"),
        TNT_PlayerDamage(2, "tnt", "player-damage"),

        MobsSpawn(3, "mobs", "spawn"),
        MobsGrief(3, "mobs", "grief"),

        NatureFirePropagation(4, "nature", "fire-propagation"),
        NaturePlantsGrow(4, "nature", "plants-grow"),
        NatureAlwaysRaining(4, "nature", "always-raining"),
        NatureAlwaysThundering(4, "nature", "always-thundering")
        ;

        private int id;
        private Object[] jsonPath;
        Rule(int id, Object... path) {
            this.id = id;
            this.jsonPath = path;
        }
    }

    public boolean rule(Rule rule) {
        if (rule.id == 0) throw new IllegalArgumentException("This rule need to specify the concerned player !");

        return jsonData.getObject(rule.jsonPath);
    }

    public boolean rule(Rule rule, NbPlayer player) {
        if (rule.id > 0)
            return rule(rule);
        else if (player != null && (player.getProfileDescriptor().isOperator() || player.isOp()))
            return true;
        else {
            Object[] exceptPath = {rule.jsonPath[0], rule.jsonPath[1], "except"};

            return jsonData.<Boolean>getObject(rule.jsonPath) ^
                    ( jsonData.<JSONArray>getObject(exceptPath).contains(player.getComposedNickname()) ||
                            (player.hasPermissionGroup() && jsonData.<JSONArray>getObject(exceptPath).contains("#"+player.getPermissionGroup().getName()))
                    );
        }
    }

    public void setRule(Rule rule, boolean value) {
        int i;
        JSONObject obj = jsonData;
        for (i = 0; i < rule.jsonPath.length-1; i++)
            //noinspection SuspiciousMethodCalls
            obj = (JSONObject) obj.get(rule.jsonPath[i]);
        obj.put(rule.jsonPath[i].toString(), value);
    }

    public void addPlayerException(Rule rule, String value) {
        if (rule.id > 0) return;

        Object[] jsonPath = {rule.jsonPath[0], rule.jsonPath[1], "except"};

        if (!jsonData.<JSONArray>getObject(jsonPath).contains(value))
            jsonData.<JSONArray>getObject(jsonPath, value).add(value);
    }

    public void removePlayerException(Rule rule, String value) {
        if (rule.id > 0) return;

        Object[] jsonPath = {rule.jsonPath[0], rule.jsonPath[1], "except"};
        jsonData.<JSONArray>getObject(jsonPath, value).remove(value);
    }
}
