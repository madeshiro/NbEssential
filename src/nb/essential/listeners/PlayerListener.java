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
package nb.essential.listeners;

import nb.essential.event.PlayerChangeZoneEvent;
import nb.essential.event.PlayerChooseProfileEvent;
import nb.essential.interfaces.InventoryInterface;
import nb.essential.loader.EventListener;
import nb.essential.main.NbEssential;
import nb.essential.player.CraftNbPlayer;
import nb.essential.player.NbPlayer;
import nb.essential.stick.Chairs;
import nb.essential.utils.Utils;
import nb.essential.zone.Rules;
import nb.essential.zone.Zone;
import nb.essential.zone.ZoneType;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.spigotmc.event.entity.EntityDismountEvent;

import static nb.essential.blockmeta.MetaManager.Nb_BlockHasMeta;
import static nb.essential.blockmeta.MetaManager.Nb_BlockGetMeta;
import static nb.essential.main.NbEssential.*;
import static nb.essential.player.PlayerSystem.*;
import static nb.essential.stick.LockDoor.*;
import static nb.essential.zone.ZoneManager.Nb_ZN_CreateZone;
import static nb.essential.zone.ZoneManager.Nb_ZN_GetZone;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 04/10/2016
 */
@EventListener
public final class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (getMainData().isMaintenanceEnabled() && !getMainData().isMaintenancePlayerAuth(event.getPlayer())) {
            event.getPlayer().kickPlayer(getMainData().getMaintenanceKickMsg());
        } else {
            if (!Nb_ConnectPlayer((CraftPlayer) event.getPlayer())) {
                event.getPlayer().kickPlayer(tl("s:join_profile_error"));
            }
        }

        // cancel the join message
        event.setJoinMessage("");
    }
/*
    public void onPlayerFirstJoin(final PlayerFirstJoinEvent event) {
         CraftNbPlayer player;
        if (!Nb_CreatePlayerProfile(event.getPlayer(), "normal", false)) {
            event.getPlayer().kickPlayer(tl("s:join_create_profile_error", event.getPlayer()));
        } else if ((player = (CraftNbPlayer) Nb_Instantiate((CraftPlayer) event.getPlayer(), "normal")) == null)
            event.getPlayer().kickPlayer(tl("s:_join_profile_error", event.getPlayer())
                    .replace("%error%", "getPlayerSystem().instanciate(player, profile) -> false"));
        else {
            player.getFile().setSave("player.info.connection.first", getDayString());
            getServer().broadcastMsgExcept(tl("s:first_join_connect", event.getPlayer()),
                    Collections.singletonList(event.getPlayer()));
            event.getPlayer().sendMessage(tl("s:first_join_comer", event.getPlayer()));
        }
    }

    private void a(final PlayerJoinEvent event) {
        // Test if player has already been in the server
        // -> testing the existence of /player/%player%.json file
        /*
        PlayerExistenceStatus status = Nb_CheckPlayerExistence(event.getPlayer());
        if (status == PlayerExistenceStatus.NO_FILE) {
            onPlayerFirstJoin(new PlayerFirstJoinEvent(event.getPlayer()));
        } else if (status == PlayerExistenceStatus.NO_PROFILE) {
            if (!Nb_CreatePlayerProfile(event.getPlayer(), "normal", false)) {
                event.getPlayer().kickPlayer(tl("s:join_create_profile_error", event.getPlayer()));
            } else if (Nb_Instantiate((CraftPlayer) event.getPlayer(), "normal") == null)
                event.getPlayer().kickPlayer(tl("s:_join_profile_error", event.getPlayer())
                        .replace("%error%", "getPlayerSystem().instanciate(player, profile) -> false"));
        } else if (Nb_GetPlayerProfiles(event.getPlayer()).size() > 1) {
            Nb_AskRepeatedlyProfile((CraftPlayer) event.getPlayer());
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else if (Nb_Instantiate((CraftPlayer) event.getPlayer(), "normal") == null)
            event.getPlayer().kickPlayer(tl("s:_join_profile_error", event.getPlayer())
                    .replace("%error%", "getPlayerSystem().instanciate(player, profile) -> false"));
    }*/

    private void a(final PlayerQuitEvent event) {
        if (Nb_IsInstantiate(event.getPlayer())) {
            Nb_GetPlayer(event.getPlayer()).quit();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangeGamemode(PlayerGameModeChangeEvent event) {
        if (!Nb_IsInstantiate(event.getPlayer()) &&
                event.getNewGameMode() != GameMode.SPECTATOR) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c" +
                    tl("s:gamemode_no_profile"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChooseProfile(PlayerChooseProfileEvent event) {
        if (!event.isCancelled()) {
            event.getPlayer().sendMessage(tl("s:join_profile", event.getPlayer()));
            event.getPlayer().sendActionBarMsg("§8§l|> §r§7Log in with profile '§9" + event.getSelectedProfile() + "§7' §8§l<|");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (Nb_IsInstantiate(event.getPlayer())) {
            Nb_DisconnectPlayer((CraftNbPlayer) Nb_GetPlayer(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        event.setFormat(a(getMainData().getChatFormat(),event.getPlayer()));

        if (event.getPlayer().hasPermission("nbessential.color"))
            event.setMessage(Utils.colorString(event.getMessage()));
    }

    @EventHandler
    public void onMountChairs(PlayerInteractEvent e) {
        if (e.isCancelled() || e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        CraftNbPlayer player = (CraftNbPlayer) Nb_GetPlayer(e.getPlayer());
        if (player == null)
            return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if (Nb_BlockHasMeta(block, "chairs")
                    && Nb_BlockGetMeta(block, "chairs").asBoolean()
                    && player.getVariables().getChairs() == null) {
                player.getVariables().setChairs(new Chairs(block, e.getPlayer()));
                player.getVariables().getChairs().sit();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDismountChairs(EntityDismountEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            CraftNbPlayer player = (CraftNbPlayer) Nb_GetPlayer((Player) event.getEntity());

            if (player.getVariables().getChairs() != null) {
                player.getVariables().getChairs().standup();
                player.getVariables().setChairs(null);
            }
        }
    }

    private String a(String format, Player player) {
        NbPlayer nb = Nb_GetPlayer(player);
        if (nb != null) {
            return Utils.colorString(format.replace("%prefix%", nb.getPrefix()));
        } else
            return Utils.colorString(format.replace("%prefix% ", ""));
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        CraftNbPlayer player = (CraftNbPlayer) getServer().getNbPlayer(event.getPlayer());

        if (player == null)
            return;

        if ((event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) && (
            !player.getZoneLocation().getRules().rule(Rules.Rule.PlayerEnderpearl, player) ||
            !Nb_ZN_GetZone(event.getTo()).getRules().rule(Rules.Rule.PlayerEnderpearl, player))) ||
            !checkZone(player))
            event.setCancelled(true);
        else if (validate(event.getCause()))
            player.getVariables().setLastLocation(event.getFrom());
    }

    boolean validate(PlayerTeleportEvent.TeleportCause cause) {
        return cause == PlayerTeleportEvent.TeleportCause.COMMAND
            || cause == PlayerTeleportEvent.TeleportCause.PLUGIN
            || cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL;
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        CraftNbPlayer player = (CraftNbPlayer) NbEssential.getServer().getNbPlayer((Player) event.getWhoClicked());
        if (player == null)
            return;

        if (player.getVariables().hasOpenedInterface() &&
                player.getVariables().getOpenedInterface() instanceof InventoryInterface) {
            ((InventoryInterface) player.getVariables().getOpenedInterface()).doAction(event.getSlot(), event.getCurrentItem());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        CraftNbPlayer player = (CraftNbPlayer) NbEssential.getServer().getNbPlayer((Player) event.getPlayer());
        if (player == null)
            return;

        if (player.getVariables().hasOpenedInterface() &&
                player.getVariables().getOpenedInterface() instanceof InventoryInterface)
            player.getVariables().setOpenedInterface(null);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        CraftNbPlayer player = (CraftNbPlayer) getServer().getNbPlayer(event.getPlayer());
        if (player == null)
            return;

        if (!checkZone(player)) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkZone(CraftNbPlayer player) {
        Zone zn = Nb_ZN_GetZone(player.getLocation());

        if (zn == null && Nb_ZN_GetZone(player.getWorld()) == null)
            Nb_ZN_CreateZone(player.getWorld().getName(), ZoneType.Realm);
        else if (player.getZoneLocation() == null)
            zn.attend(player);
        else if (!player.getZoneLocation().equals(zn)) {
            if (!(zn.getRules().rule(Rules.Rule.PlayerEnter, player) && player.getZoneLocation().getRules().rule(Rules.Rule.PlayerExit, player))) {
                player.sendActionBarMsg("§eYou can't change zone !");
                return false;
            }

            PlayerChangeZoneEvent event = new PlayerChangeZoneEvent(player, player.getZoneLocation(), zn);
            getServer().getBukkitServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return false;

            zn.attend(player);
        }

        return true;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        CraftNbPlayer player = (CraftNbPlayer) getServer().getNbPlayer(event.getPlayer());

        if (player == null)
            event.setCancelled(true);
        else if (event.getRightClicked() instanceof Player && !player.getZoneLocation().getRules().rule(Rules.Rule.PVP))
            event.setCancelled(true);
        else if (!player.getZoneLocation().getRules().rule(Rules.Rule.PVE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerOpenDoor(PlayerInteractEvent event) {
        CraftNbPlayer player = (CraftNbPlayer) getServer().getNbPlayer(event.getPlayer());
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null ||
                !Utils.isLockdoorSupported(event.getClickedBlock())) {
            event.setCancelled(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.DRAGON_EGG || event.isCancelled());
            return;
        }

        if (Nb_BlockHasMeta(event.getClickedBlock(), "lockdoor")) {

            // If door is locked
            if (Nb_LD_IsDoorLock(event.getClickedBlock())) {
                // Check the key
                if (Nb_LD_IsKeyFor(event.getClickedBlock(), event.getPlayer().getInventory().getItemInMainHand())) {
                    // Unlock the door and opened it
                    event.setCancelled(o(event.getClickedBlock(), false));
                } else {
                    player.sendActionBarMsg("§7" + NbEssential.tl("s:door_lock"));
                    event.setCancelled(true);
                }
            // Else if the door is unlocked
            } else {
                // Check the key
                if (Nb_LD_IsKeyFor(event.getClickedBlock(), event.getPlayer().getInventory().getItemInMainHand())) {
                    // Lock the door and closed it
                    event.setCancelled(o(event.getClickedBlock(), true));
                }
            }
        }
    }

    private boolean o(Block block, boolean lock) {
        if (block.getBlockData() instanceof Openable) {
            Openable door = (Openable) block.getBlockData();
            Nb_LD_DefineLockDoor(block, lock);
            if (block.getType().name().contains("IRON")) {
                door.setOpen(!lock);
                block.setBlockData(door, true);
                return true;
            } else {
                return lock && !door.isOpen();
            }
        } else {
            Nb_LD_DefineLockDoor(block, lock);
            return lock;
        }
    }
}
