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
package nb.essential.player;

import nb.economy.Eldar;
import nb.essential.files.JSONFile;
import nb.essential.main.IServer;
import nb.essential.permissions.GroupManager;
import nb.essential.permissions.PermissionGroup;
import nb.essential.utils.Utils;
import nb.script.commands.ScriptValues;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import zaesora.madeshiro.api.web.Authorization;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONObject;

import java.util.ArrayList;

import static nb.essential.main.NbEssential.getServer;
import static nb.essential.permissions.GroupManager.NB_GM_MINECRAFT_PERMISSION;

/**
 * A ProfileDescriptor contains all player profile's data stocked in a
 * {@link JSONObject} to simplify the save process using {@link JSONFile}.
 * The ProfileDescriptor also contains a {@link ProfileHandler} which permit to
 * modify easily player configuration and parameters. Moreover, the {@link ProfileHandler}
 * implementation is adapted according to if the player is online or not.
 *
 * @see PlayerProfile
 * @see ProfileHandler
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public final class ProfileDescriptor {

    private String name;
    private JSONObject data;
    private ProfileHandler handler;

    private ArrayList<String> permissions;
    private ArrayList<String> forbiddens;

    protected ProfileDescriptor(String name, String player, JSONObject data) throws IllegalArgumentException {
        if (data == null)
            throw new IllegalArgumentException("Invalid JSONValue <null>");

        this.name = name;
        this.data = data;
        setSpecialVars();

        this.handler = new OfflineProfileHandler(player);
    }

    protected ProfileDescriptor(String name, CraftNbPlayer player, JSONObject data) throws IllegalArgumentException {
        if (data == null)
            throw new IllegalArgumentException("Invalid JSONValue <null>");

        this.name = name;
        this.data = data;
        setSpecialVars();

        this.handler = new OnlineProfileHandler(player);
    }

    protected void setData(JSONObject data) {
        this.data = data;

        setSpecialVars();
    }

    private void setSpecialVars() {
        permissions = new ArrayList<>();
        forbiddens = new ArrayList<>();

        for (Object obj : data.<JSONArray>getObject("groupmanager", "permissions"))
            permissions.add(obj.toString());
        for (Object obj : data.<JSONArray>getObject("groupmanager", "forbiddens"))
            forbiddens.add(obj.toString());
    }

    public boolean isOnlineDescriptor() {
        return handler instanceof OnlineProfileHandler;
    }

    public ProfileHandler getHandler() {
        return handler;
    }

    public OnlineProfileHandler getOnlineHandler() {
        return (handler instanceof OnlineProfileHandler) ?
                (OnlineProfileHandler) handler : null;
    }

    public boolean save(JSONFile file) {
        handler.save();

        file.set(data, "profiles", name);
        return file.save();
    }

    /* --- JSON DATA METHODS --- */

    protected JSONObject getJSONData() {
        return data;
    }

    public IServer.SupportedLanguage getLang() {
        return IServer.SupportedLanguage.FR_fr;
    }

    public String getProfileName() {
        return name;
    }

    public String getNickname() {
        return data.getObject("nickname");
    }

    public String getComposedNickname() {
        return Utils.uncolorString(getNickname().replace(" ", "_"), '&');
    }

    private void setNickname(String nickname) {
        data.put("nickname", nickname);
    }

    public boolean isOperator() {
        return data.getObject("operator");
    }

    public String getPrefix() {
        return data.getObject("prefix");
    }

    private void setPrefix(String prefix) {
        data.put("prefix", prefix);
    }

    public double getMoney() {
        return data.getObject("money");
    }

    private void setMoney(double d) {
        data.put("money", d);
    }

    public Location getSaveLocation() {
        return GenerateLocation("save", "location");
    }

    public GameMode getSaveGamemode() {
        return GameMode.valueOf(data.<String>getObject("save", "gamemode").toUpperCase());
    }

    public boolean isFainted() {
        return data.getObject("state", "isFainted");
    }

    private void setFainted(boolean b) {
        data.<JSONObject>getObject("state").put("isFainted", b);
    }

    public boolean isJailed() {
        return data.getObject("state", "isJailed", "value");
    }

    public JSONObject getJailDescriptor() {
        return data.getObject("state", "isJailed", "jail");
    }

    public long getJailDuration() {
        return data.getObject("state", "isJailed", "duration");
    }

    private void setJailDescription(JSONObject obj) {
        data.<JSONObject>getObject("state", "isJailed").put("jail", obj);
    }

    private void setJailDuration(long duration) {
        data.<JSONObject>getObject("state", "isJailed").put("duration", duration);
    }

    public JSONObject getNbSL() {
        return data.getObject("state", "nbsl");
    }

    public String getJobType() {
        return data.getObject("job", "type");
    }

    public int getJobLevel() {
        return data.getObject("job", "level");
    }

    public JSONArray getJobData() {
        return data.getObject("job", "data");
    }

    private void setJobType(String name) {
        data.<JSONObject>getObject("job").put("type", name);
    }

    private void setJobLevel(int level) {
        data.<JSONObject>getObject("job").put("level", level);
    }

    public Location getLastLocation() {
        return GenerateLocation("location", "last");
    }

    private void setLastLocation(Location location) {
        data.<JSONObject>getObject("location").put("last", GenerateJLocation(location));
    }

    public Location getSpawnLocation() {
        return GenerateLocation("location", "spawn");
    }

    private void setSpawnLocation(Location location) {
        data.<JSONObject>getObject("location").put("spawn", GenerateJLocation(location));
    }

    // TODO Domains functions !

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public ArrayList<String> getForbiddens() {
        return forbiddens;
    }

    private void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;

        JSONArray array = new JSONArray();
        array.addAll(permissions);
        data.<JSONObject>getObject("groupmanager").put("permissions", array);
    }

    private void setForbiddens(ArrayList<String> forbiddens) {
        this.forbiddens = forbiddens;

        JSONArray array = new JSONArray();
        array.addAll(forbiddens);
        data.<JSONObject>getObject("groupmanager").put("forbiddens", array);
    }

    public void addPermission(String perm) {
        if (!permissions.contains(perm)) {
            permissions.add(perm);
            data.<JSONArray>getObject("groupmanager", "permissions").add(perm);
        }

        removeForbidden(perm);
    }

    public void addForbidden(String perm) {
        if (!forbiddens.contains(perm)) {
            forbiddens.add(perm);
            data.<JSONArray>getObject("groupmanager", "forbiddens").add(perm);
        }

        removePermission(perm);
    }

    public void removePermission(String perm) {
        permissions.remove(perm);
        data.<JSONArray>getObject("groupmanager", "permissions").remove(perm);
    }

    public void removeForbidden(String perm) {
        forbiddens.remove(perm);
        data.<JSONArray>getObject("groupmanager", "forbiddens").remove(perm);
    }

    private void setPermissionGroup(String name) {
        data.<JSONObject>getObject("groupmanager").put("group", name);
    }

    public String getPermissionGroup() {
        return data.getObject("groupmanager", "group");
    }

    /* --- USEFUL --- */

    private Location GenerateLocation(Object... path) {
        JSONObject location = data.getObject(path);
        if (location == null)
            return null;

        World world = getServer().getWorld(location.getObject("world"));
        double x = location.getObject("x"),
                y = location.getObject("y"),
                z = location.getObject("z");
        float yaw = location.getObject("yaw"),
                pitch = location.getObject("pitch");

        return world == null ? null : new Location(world,x,y,z,yaw,pitch);
    }

    private JSONObject GenerateJLocation(Location location) {
        JSONObject obj = new JSONObject();
        {
            obj.put("x", location.getX());
            obj.put("y", location.getY());
            obj.put("z", location.getZ());
            obj.put("pitch", location.getPitch());
            obj.put("yaw", location.getYaw());
            obj.put("world", location.getWorld().getName());
        }

        return obj;
    }

    public abstract class ProfileHandler {

        protected PermissionGroup group;

        public abstract String getPlayer();

        protected ProfileHandler() {
            if (ProfileDescriptor.this.getPermissionGroup() != null && !ProfileDescriptor.this.getPermissionGroup().isEmpty())
                group = GroupManager.Nb_GetGroup(ProfileDescriptor.this.getPermissionGroup());
        }

        /**
         * Save the profile handler data.
         * @implNote In case of {@link ProfileHandler}, this function does nothing.
         * To save properly data, use {@link ProfileDescriptor#save(JSONFile)}.
         */
        public void save() {
        }

        public String getPrefix() {
            String prefix = "";

            if (hasPermissionGroup())
                prefix = group.getPrefix();
            if (hasCustomPrefix())
                prefix = ProfileDescriptor.this.getPrefix();

            return prefix;
        }

        private boolean hasCustomPrefix() {
            return ProfileDescriptor.this.getPrefix() != null && !ProfileDescriptor.this.getPrefix().isEmpty();
        }

        public Eldar getMoney() {
            return new Eldar(ProfileDescriptor.this.getMoney());
        }

        public boolean setNickname(String nickname) {
            {
                String composedNickname = Utils.uncolorString(nickname.replace(' ', '_'), '&');
                if ( !getComposedNickname().equalsIgnoreCase(composedNickname) &&
                        PlayerSystem.Nb_GetBasicPlayer(composedNickname) != null)
                    return false;
            }

            PermissionGroup group = getPermissionGroup();
            setPermissionGroup(null);

            ProfileDescriptor.this.setNickname(nickname);

            setPermissionGroup(group);

            return true;
        }
        public boolean hasPermissionGroup() {
            return group != null;
        }

        public PermissionGroup getPermissionGroup() {
            return group;
        }

        public boolean setPermissionGroup(PermissionGroup group) {
            this.group = group;
            ProfileDescriptor.this.setPermissionGroup(group == null ? null:group.getName());

            return true;
        }

        public void setPermission(String permission, boolean value) {
            if (value)
                addPermission(permission);
            else
                addForbidden(permission);
        }

        public void unsetPermission(String permission) {
            removePermission(permission);
            removeForbidden(permission);
        }
    }

    public final class OnlineProfileHandler extends ProfileHandler {

        /**
         * JSONAPI / WebSite authorization parameter
         */
        public Authorization authorization;
        private ScriptValues values;

        private Eldar money;

        private final CraftNbPlayer player;
        private PlayerProfile profile() { return player.getProfile(); }

        @Override
        public String getPlayer() {
            return player.getName();
        }

        protected OnlineProfileHandler(final CraftNbPlayer player) {
            super();

            this . player = player;
            this . values = new ScriptValues(getNbSL());
            this . applyPermissions(false);
        }

        /**
         * Save the profile handler data.
         * @implNote In case of {@link OnlineProfileHandler}, this function only save
         * the player's state (current location, gamemode and inventory).
         * To save properly data, use {@link ProfileDescriptor#save(JSONFile)}.
         */
        @Override
        public void save() {
            saveState();
            values.putData(data);
        }

        public void saveState() {
            JSONObject state = new JSONObject();
            state.put("gamemode", player.getGameMode().name());
            state.put("inventory", player.getInventory().getContents());
            JSONObject location = new JSONObject();
            {
                location.put("x", player.getLocation().getX());
                location.put("y", player.getLocation().getY());
                location.put("z", player.getLocation().getZ());
                location.put("pitch", player.getLocation().getPitch());
                location.put("yaw", player.getLocation().getYaw());
                location.put("world", player.getLocation().getWorld().getName());
            }
            state.put("location", location);

            data.put("save", state);
        }

        public void applyPermissions(boolean reset) {
            if (reset) {
                for (String str : player.getProfile().getAttachment().getPermissions().keySet())
                    profile().getAttachment().unsetPermission(str);
            }

            if (hasPermissionGroup()) {
                for (String perm : group.getPermissions().keySet()) {
                    if (perm.equals("*") && group.getPermissions().get(perm))
                        setSuperPermission();
                    else
                        profile().getAttachment().setPermission(perm, group.getPermissions().get(perm));
                }
            }

            for (String str : permissions) {
                if (str.equals("*"))
                    setSuperPermission();
                else
                    profile().getAttachment().setPermission(str, true);
            }

            for (String str : forbiddens) {
                profile().getAttachment().setPermission(str, false);
            }
        }

        @Override
        public Eldar getMoney() {
            return money;
        }

        @Override
        public boolean setNickname(String nickname) {
            if (!super.setNickname(nickname))
                return false;

            player.setDisplayName(Utils.colorString(getNickname()));
            player.setPlayerListName(Utils.colorString(getNickname()));
            return true;
        }

        @Override
        public boolean setPermissionGroup(PermissionGroup group) {
            if (group == null) {
                if (this.group != null && !this.group.removeMember(player))
                    return false;
            } else {
                if (!group.addMember(player)) return false;
            }

            super.setPermissionGroup(group);
            applyPermissions(true);

            return true;
        }

        private void setSuperPermission() {
            for (String str : NB_GM_MINECRAFT_PERMISSION)
                profile().getAttachment().setPermission(str, true);
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
                for (Permission permission : plugin.getDescription().getPermissions())
                    profile().getAttachment().setPermission(permission, true);
        }

        @Override
        public void setPermission(String permission, boolean value) {
            super.setPermission(permission,value);
            applyPermissions(true);
        }
    }

    public final class OfflineProfileHandler extends ProfileHandler {
        private String player;

        protected OfflineProfileHandler(String player) {
            super();

            this.player = player;
        }

        public String getPlayer() {
            return player;
        }

        @Override
        public boolean setPermissionGroup(PermissionGroup group) {
            if (group == null) {
                if (this.group != null && !this.group.removeMember(getComposedNickname()))
                    return false;
            } else {
                if (!group.addMember(getComposedNickname()))
                    return false;
            }

            return super.setPermissionGroup(group);
        }
    }
}
