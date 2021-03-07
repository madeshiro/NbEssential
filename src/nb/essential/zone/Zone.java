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

import nb.essential.player.CraftNbPlayer;
import nb.essential.ressourcepack.Music;
import nb.essential.utils.Utils;
import nb.roleplay.jukebox.Programmation;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONObject;

import java.util.ArrayList;

import static nb.essential.zone.ZoneManager.Nb_ZN_GetFileHandler;

/**
 * Class of NbEssential
 */
public abstract class Zone {

    protected Rules rules;
    protected final String name;
    protected JSONObject jsonData;

    public abstract ZoneType getType();
    protected abstract boolean delimit();
    protected abstract boolean undelimit();

    protected boolean remove() {
        Nb_ZN_GetFileHandler().<JSONObject>get(getType().toString() + "s").remove(name);
        return Nb_ZN_GetFileHandler().save() && undelimit();
    }

    public boolean save() {
        Nb_ZN_GetFileHandler().set(jsonData, getType().toString()+"s", getName());
        return Nb_ZN_GetFileHandler().save();
    }

    public boolean load() {
        jsonData = Nb_ZN_GetFileHandler().get(getType().toString()+"s", getName());
        rules = new Rules(this);

        return jsonData != null;
    }

    public boolean reload() {
        return Nb_ZN_GetFileHandler().reload() && undelimit() && load();
    }

    protected Zone(String name) {
        this.name = name;
    }

    /* --- zone property --- */

    public String getName() {
        return name;
    }

    public void setDisplayName(String name) { jsonData.put("displayName", name); }

    public String getDisplayName() { return jsonData.getObject("displayName"); }

    public void addMusic(String music) {
        jsonData.<JSONArray>getObject("musics").add(music);
    }
    public void addMusic(Music music) {
        jsonData.<JSONArray>getObject("music").add(music.getPath());
    }
    public void removeMusic(String music) {
        jsonData.<JSONArray>getObject("musics").remove(music);
    }
    public void removeMusic(Music music) {
        jsonData.<JSONArray>getObject("musics").remove(music.getPath());
    }

    public ArrayList<String> getMusics() {
        ArrayList<String> musics = new ArrayList<>();
        for (Object obj : jsonData.<JSONArray>getObject("musics"))
            musics.add(obj.toString());
        return musics;
    }

    public String getPlayerListHeader() {
        return jsonData.getObject("display", "playerList", "header");
    }

    public String getPlayerListFooter() {
        return jsonData.getObject("display", "playerList", "footer");
    }

    public void setPlayerListHeader(String header) {
        jsonData.<JSONObject>getObject("display", "playerList").put("header", header);
    }

    public void setPlayerListFooter(String footer) {
        jsonData.<JSONObject>getObject("display", "playerList").put("footer", footer);
    }

    public void setTitle(String title) {
        jsonData.<JSONObject>getObject("display").put("title", title);
    }

    public void setSubtitle(String subtitle) {
        jsonData.<JSONObject>getObject("display").put("subtitle", subtitle);
    }

    public void setActionbar(String msg) {
        jsonData.<JSONObject>getObject("display").put("actionbar", msg);
    }

    public String getTitle() {
        return jsonData.getObject("display", "title");
    }

    public String getSubtitle() {
        return jsonData.getObject("display", "subtitle");
    }

    public String getActionbar() {
        return jsonData.getObject("display", "actionbar");
    }

    public abstract int getSignificiance();

    public String toString() {
        return getType().name() + "." + getName();
    }

    public <T extends Rules> T getRules() {
        return (T) rules;
    }

    public boolean equals(Zone zone) {
        return zone != null && zone.getType().equals(getType()) && getName().equals(zone.getName());
    }

    protected void to_default() {
        jsonData = new JSONObject(Nb_ZN_GetFileHandler().<JSONObject>get("defaults", getType().toString()));
        rules =  new Rules(this);
    }

    protected abstract Programmation getMusicProgrammation(final CraftNbPlayer player);

    public void attend(final CraftNbPlayer player) {
        player.setTabListHeaderFooter(getPlayerListHeader(), getPlayerListFooter());

        if (player.getZoneLocation() != null && !player.getZoneLocation().equals(this) && (
                player.getZoneLocation().getSignificiance() < getSignificiance() ||
                getSignificiance() == -1)) {

            if (getTitle() != null || getSubtitle() != null)
                player.sendTitle(getTitle(),
                        getSubtitle(),
                        20, 60, 20);
            if (getActionbar() != null)
                player.sendActionBarMsg(Utils.colorString(getActionbar()));
        }

        player.getVariables().setZoneLocation(this);
        player.getMusicPlayer().setProgrammation(getMusicProgrammation(player));
    }
}

