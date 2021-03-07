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

import nb.essential.event.PlayerFirstJoinEvent;
import nb.essential.files.JSONFile;
import nb.essential.files.ResourceFile;
import nb.essential.permissions.PermissionGroup;
import nb.essential.utils.Utils;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import zaesora.madeshiro.parser.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nb.essential.main.NbEssential.*;
import static nb.essential.permissions.GroupManager.Nb_GetGroup;

/**
 * <p>
 * A unique class instance to manage easily players particulary when they aren't
 * instanciated as {@link NbPlayer}. <br/>
 * The system is mainly used for the {@link PlayerJoinEvent} to create, instanciate and/or
 * verify player's dependencies.
 * </p>
 *
 * @see NbPlayer
 * @see nb.essential.commands.ProfileCmd ProfileCmd
 * @see nb.essential.listeners.PlayerListener#onPlayerJoin(PlayerJoinEvent) onPlayerJoin [event listener]
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.~2
 * @version NB 1.2
 */
// TODO RE-CREATE PLAYER SYSTEM FUNCTION WITH BETTER USE AND FULL DOCUMENTATION
public class PlayerSystem {

    public static final String rp_profile  = "rp-profile";
    private static final String directory  = "plugins/NbEssential/player/";
    private static final File playerFolder = new File(directory);
    private static final HashMap<String, OfflineNbPlayer> offlines = new HashMap<>();

    private static boolean systemLoaded = false;

    public static void Nb_LoadPlayerSystem() {
        if (systemLoaded)
            return;
        systemLoaded = true;

        for (ProfileDescriptor descriptor : Nb_GetAllProfileDescriptor()) {
            offlines.put(descriptor.getComposedNickname(), new OfflineNbPlayer(descriptor));
            if (isPluginDebug())
                getLogger().info("[PlayerSystem] Load <"+descriptor.getComposedNickname()+"> Offline profile");
        }
    }

    private static PlayerData getData() {
        return getDataLoader().getData("player");
    }

    private static JSONFile fileOf(Player player) {
        return fileOf(new File("plugins/NbEssential/player/" + player.getName() + ".json"));
    }

    private static JSONFile fileOf(File file) {
        JSONFile jfile = new JSONFile(file);

        return jfile.load() ? jfile : null;
    }

    private static JSONFile[] getJsons() {
        JSONFile[] files = new JSONFile[playerFolder.listFiles().length];

        int i = -1;
        for (File file : playerFolder.listFiles()) {
            files[i += 1] = new JSONFile(file);
            if (!files[i].load() && isPluginDebug())
                getLogger().warning("Unable to load <" + file.getName() + ">");
        }

        return files;
    }

    public static NbPlayer Nb_GetPlayer(String nickname) {
        for (PlayerProfile profile : getData())
            if (profile.getDescriptor().getComposedNickname().equalsIgnoreCase(nickname))
                return profile.getPlayer();
        return null;
    }

    public static boolean Nb_ConnectPlayer(CraftPlayer player) {
        if (!Nb_CreatePlayerFile(player))
            return false;
        JSONFile file = fileOf(player);
        PlayerProfile profile;
        boolean firstjoin = false;

        if (file.<JSONObject>get("profiles").size() == 0) {
            if (Nb_CreatePlayerProfile(player, rp_profile, player.getName(), false)) {
                profile = new PlayerProfile(player, rp_profile);
                firstjoin = true;
            } else {
                return false;
            }
        } else
            profile = new PlayerProfile(player, rp_profile);

        if (!profile.load()) return false;

        CraftNbPlayer craftplayer = new CraftNbPlayer(player, profile);
        if (firstjoin)
            getServer().getBukkitServer().getPluginManager().callEvent(new PlayerFirstJoinEvent(craftplayer));
        getData().addPlayer(craftplayer);
        offlines.remove(profile.getDescriptor().getComposedNickname());
        return true;
    }

    public static boolean Nb_DisconnectPlayer(CraftNbPlayer player) {
        player.saveData();
        getData().removePlayer(player);
        offlines.put(player.getComposedNickname(), new OfflineNbPlayer(player.getName(), player.getProfileDescriptor().getProfileName()));
        return true;
    }

    public static NbPlayer Nb_GetPlayer(Player player) {
        return getData().getPlayer(player);
    }

    public static NbPlayer Nb_GetPlayerByIp(String ip) {
        for (PlayerProfile profile : getData())
            if (profile.getPlayer().getAddress().getHostString().equals(ip))
                return profile.getPlayer();
        return null;
    }

    public static OfflineNbPlayer Nb_GetOfflinePlayer(String nickname) {
        return offlines.get(nickname);
    }

    public static BasicNbPlayer Nb_GetBasicPlayer(String nickname) {
        BasicNbPlayer player;
        return ((player = Nb_GetPlayer(nickname)) != null) ? player : offlines.get(nickname);
    }

    public static List<NbPlayer> Nb_GetPlayers() {
        ArrayList<NbPlayer> players = new ArrayList<>();
        for (PlayerProfile profile : getData())
            players.add(profile.getPlayer());
        return players;
    }

    public static List<String> Nb_GetPlayersList() {
        ArrayList<String> list = new ArrayList<>();
        for (PlayerProfile profile : getData())
            list.add(profile.getDescriptor().getComposedNickname());
        return list;
    }

    public static List<OfflineNbPlayer> Nb_GetOfflinePlayers() {
        return new ArrayList<>(offlines.values());
    }

    public static List<ProfileDescriptor> Nb_GetAllProfileDescriptor() {
        JSONFile[] files = getJsons();
        ArrayList<ProfileDescriptor> list = new ArrayList<>(files.length);

        for (JSONFile file : files) {
            for (String key : file.<JSONObject>get("profiles").keySet()) {
                ProfileDescriptor descriptor = new ProfileDescriptor(key, file.<String>get("name"), file.get("profiles", key));

                NbPlayer player;
                if ((player = Nb_GetPlayer(descriptor.getComposedNickname())) != null) {
                    descriptor = player.getProfileDescriptor();
                }

                list.add(descriptor);
            }
        }

        return list;
    }

    public static boolean Nb_IsProfileExists(Player player, String profile) {
        return fileOf(player).<JSONObject>get("profiles").containsKey(profile);
    }

    public static boolean Nb_IsInstantiate(Player player) {
        return getData().getPlayer(player) != null;
    }

    public static boolean Nb_IsNicknameUsed(String nick) {
        return Nb_GetBasicPlayer(nick) != null;
    }

    public static boolean Nb_CreatePlayerFile(Player player) {
        File file = new File(directory + player.getName() + ".json");
        if (file.exists() && fileOf(file) != null)
            return true;
        else if (file.exists())
            file.delete();
        else if (!file.exists()) {
            for (JSONFile file1 : getJsons()) {
                if (file1.load() && file1.get("uuid").equals(player.getUniqueId())) {
                    return file1.getFile().renameTo(new File(directory+player.getName()+".json"));
                }
            }
        }

        if (ResourceFile.copyFromResource("player.json", directory + player.getName() + ".json")) {
            JSONFile file1 = fileOf(player);
            file1.set(player.getName(), "name");
            file1.set(player.getUniqueId().toString(), "uuid");

            return file1.save();
        } else return false;
    }

    public static boolean Nb_CreatePlayerProfile(Player player, String profile, String nick, boolean admin) {
        if (!Nb_CreatePlayerFile(player))
            return false;

        JSONFile file = fileOf(player);
        if (file.<JSONObject>get("profiles").containsKey(profile) || Nb_IsNicknameUsed(nick))
            return false;
        file.set(getMainData().getConfigFile().<JSONObject>get("player", "defaultProfile"), "profiles", profile);
        file.set(nick, "profiles", profile, "nickname");
        file.set(admin, "profiles", profile, "operator");

        String groupName;
        if ((groupName = file.get("profiles", profile, "groupmanager", "group")) != null && !groupName.isEmpty()) {
            PermissionGroup group = Nb_GetGroup(groupName);
            if (group == null)
                file.set(null, "profiles", profile, "groupmanager", "group");
            else
                group.addMember(Utils.uncolorString(nick,'&'));
        }

        return file.save();
    }
}
