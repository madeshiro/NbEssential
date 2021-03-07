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
package nb.essential.main;

import nb.essential.files.JSONFile;
import nb.essential.files.ResourceFile;
import nb.essential.loader.Loader;
import nb.essential.loader.PluginData;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import zaesora.madeshiro.parser.json.JSONArray;

import java.io.File;

import static nb.essential.loader.LoaderPriority.Lowest;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 25/08/2016
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
@Loader(value = "main", priority = Lowest)
public class MainData implements PluginData {

    private JSONFile file;

    /**
     * Constructs a new {@link MainData}.
     */
    public MainData() {
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean save() {
        return file.save();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean load() {
        File configFile = PluginData.checkrcfile("config.json");
        if (configFile == null) return false;
        file = new JSONFile(configFile);
        return file.load();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean reload() {
        return file.reload();
    }

    public JSONFile getConfigFile() {
        return file;
    }

    public String getServerName() {
        return file.get("server", "name");
    }

    public void setServerName(String name) {
        file.set(name, "server", "name");
    }

    public String getMOTD() {
        return file.get("server", "motd");
    }

    public void setMOTD(String motd) {
        file.set(motd, "server", "motd");
    }

    public String getChatFormat() {
        return file.get("server", "chat", "format");
    }

    public void setChatFormat(String format) {
        file.set(format, "server", "chat", "format");
    }

    public String getChatPermission() {
        return file.get("server", "chat", "permission");
    }

    public int getAPIPort() {
        return file.<Long>get("server", "external", "port").intValue();
    }

    public void setAPIPort(int port) {
        file.set(port, "server", "external", "port");
    }

    public String getAPIContentDirectory() {
        return file.get("server", "external", "content");
    }

    public boolean isMaintenanceEnabled() {
        return file.get("server", "maintenance", "enabled");
    }

    public void setMaintenanceEnabled(boolean value) {
        file.<JSONObject>get("server", "maintenance").put("enabled", value);
    }

    public String getMaintenanceKickMsg() {
        return file.get("server", "maintenance", "kickMsg");
    }

    public void setMaintenanceKickMsg(String msg) {
        file.<JSONObject>get("server", "maintenance").put("kickMsg",msg);
    }

    public boolean isMaintenancePlayerAuth(Player player) {
        JSONArray auths = file.get("server", "maintenance", "authorized");

        if (player.isOp() && auths.contains("@op"))
            return true;
        if (player.isWhitelisted() && auths.contains("@whitelist"))
            return true;

        return auths.contains(player.getName());
    }

    public void addMaintenanceAuth(String rule) {
        if (file.<JSONArray>get("server", "maintenance", "authorized").contains(rule))
            return;

        file.<JSONArray>get("server", "maintenance", "authorized").add(rule);
    }

    public void addMaintenancePlayerAuth(Player player) {
        addMaintenanceAuth(player.getName());
    }

    public void removeMaintenanceAuth(String rule) {
        file.<JSONArray>get("server", "maintenance", "authorized").remove(rule);
    }
}
