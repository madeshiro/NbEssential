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

package zaesora.madeshiro.protocollib.packet;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import net.minecraft.server.v1_13_R1.*;
import zaesora.madeshiro.protocollib.IPacket;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 29/11/2016
 */
public class Play {

    private static final HashMap<Integer, ClientBound> CLIENT_BOUND_HASH;
    private static final HashMap<Integer, ServerBound> SERVER_BOUND_HASH;

    static {
        CLIENT_BOUND_HASH = new HashMap<>();
        for (ClientBound bound : ClientBound.values())
            CLIENT_BOUND_HASH.put(bound.getID(), bound);

        SERVER_BOUND_HASH = new HashMap<>();
        for (ServerBound bound : ServerBound.values())
            SERVER_BOUND_HASH.put(bound.getID(), bound);
    }

    public static ClientBound getClientPacket(int id) {
        return CLIENT_BOUND_HASH.get(id);
    }

    public static ServerBound getServerPacket(int id) {
        return SERVER_BOUND_HASH.get(id);
    }

    public static IPacket getPacket(int id, PacketType.Bound bound) {
        //noinspection Duplicates
        switch (bound) {
            case Client:
                return getClientPacket(id);
            case Server:
                return getServerPacket(id);
            default:
                return null;
        }
    }

    public enum ClientBound implements IPacket {

        SPAWN_ENTITY(0x00, "Spawn Entity", PacketPlayOutSpawnEntity.class),
        SPAWN_EXPERIENCE_ORB(0x01, "Spawn Experience Orb", PacketPlayOutSpawnEntityExperienceOrb.class),
        SPAWN_GLOBAL_ENTITY(0x02, "Spawn Entity Weather", PacketPlayOutSpawnEntityWeather.class),
        SPAWN_MOB(0x03, "Spawn Entity Living", PacketPlayOutSpawnEntityLiving.class),
        SPAWN_PAINTING(0x04, "Spawn Painting", PacketPlayOutSpawnEntityPainting.class),
        SPAWN_PLAYER(0x05, "Spawn Player", PacketPlayOutNamedEntitySpawn.class),
        ANIMATION(0x06, "Animation", PacketPlayOutAnimation.class),
        STATISTICS(0x07, "Statistics", PacketPlayOutStatistic.class),
        BLOCK_BREAK_ANIMATION(0x08, "Block Break Animation", PacketPlayOutBlockBreakAnimation.class),
        UPDATE_BLOCK_ENTITY(0x09, "Update Block Entity", PacketPlayOutTileEntityData.class),
        BLOCK_ACTION(0x0A, "Block Action", PacketPlayOutBlockAction.class),
        BLOCK_CHANGE(0x0B, "Block Change", PacketPlayOutBlockChange.class),
        BOSS_BAR(0x0C, "Boss Bar", PacketPlayOutBoss.class),
        SERVER_DIFFICULTY(0x0d, "Server Difficulty", PacketPlayOutServerDifficulty.class),
        TAB_COMPLETE(0x0e, "Tab Complete", PacketPlayOutTabComplete.class),
        CHAT_MESSAGE(0x0f, "Chat Message", PacketPlayOutChat.class),
        MULTI_BLOCK_CHANGE(0x10, "Multi Block Change", PacketPlayOutMultiBlockChange.class),
        CONFIRM_TRANSACTION(0x11, "Confirm Transaction", PacketPlayOutTransaction.class),
        CLOSE_WINDOW(0x12, "Close Window", PacketPlayOutCloseWindow.class),
        OPEN_WINDOW(0x13, "Open Window", PacketPlayOutOpenWindow.class),
        WINDOW_ITEMS(0x14, "Window Items", PacketPlayOutWindowItems.class),
        WINDOW_PROPERTY(0x15, "Window Property", PacketPlayOutWindowData.class),
        SET_SLOT(0x16, "Set Slot (Window)", PacketPlayOutSetSlot.class),
        SET_COOLDOWN(0x17, "Set Cooldown", PacketPlayOutSetCooldown.class),
        PLUGIN_MESSAGE(0x18, "Plugin Message", PacketPlayOutCustomPayload.class),
        NAMED_SOUND_EFFECT(0x19, "Named Sound Effect", PacketPlayOutNamedSoundEffect.class),
        DISCONNECT(0x1a, "Disconnect", PacketPlayOutKickDisconnect.class),
        ENTITY_STATUS(0x1b, "Entity Status", PacketPlayOutEntityStatus.class),
        EXPLOSION(0x1c, "Explosion", PacketPlayOutExplosion.class),
        UNLOAD_CHUNK(0x1d, "Unload Chunk", PacketPlayOutUnloadChunk.class),
        CHANGE_GAME_STATE(0x1e, "Change Game State", PacketPlayOutGameStateChange.class),
        KEEP_ALIVE(0x1f, "Keep Alive", PacketPlayOutKeepAlive.class),
        CHUNK_DATA(0x20, "Chunk Data", PacketPlayOutMapChunk.class),
        EFFECT(0x21, "Effect", PacketPlayOutWorldEvent.class),
        PARTICLE(0x22, "Particle", PacketPlayOutWorldParticles.class),
        JOIN_GAME(0x23, "Join Game", PacketPlayOutLogin.class),
        MAP(0x24, "Map", PacketPlayOutMap.class),
        ENTITY_RELATIVE_MOVE(0x25, "Entity Relative Move", PacketPlayOutEntity.PacketPlayOutRelEntityMove.class),
        ENTITY_LOOK_RELATIVE_MOVE(0x26, "Entity Look And Relative Move", PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook.class),
        ENTITY_LOOK(0x27, "Entity Look", PacketPlayOutEntity.PacketPlayOutEntityLook.class),
        ENTITY(0x28, "Entity", PacketPlayOutEntity.class),
        VEHICLE_MOVE(0x29, "Vehicle Move", PacketPlayOutVehicleMove.class),
        OPEN_SIGN_EDITOR(0x2a, "Open Sign Editor", PacketPlayOutOpenSignEditor.class),
        PLAYER_ABILITIES(0x2b, "Player Abilities", PacketPlayOutAbilities.class),
        COMBAT_EVENT(0x2c, "Combat Event", PacketPlayOutCombatEvent.class),
        PLAYER_LIST_ITEM(0x2d, "Player List Item", PacketPlayOutPlayerInfo.class),
        PLAYER_POSITION_AND_LOOK(0x2e, "Player Position and Look", PacketPlayOutPosition.class),
        USE_BED(0x2f, "Use Bed", PacketPlayOutBed.class),
        DESTROY_ENTITIES(0x30, "Destroy Entities", PacketPlayOutEntityDestroy.class),
        REMOVE_ENTITY_EFFECT(0x31, "Remove Entity Effect", PacketPlayOutRemoveEntityEffect.class),
        RESOURCE_PACK_SEND(0x32, "Resource Pack Send", PacketPlayOutResourcePackSend.class),
        RESPAWN(0x33, "Respawn", PacketPlayOutRespawn.class),
        ENTITY_HEAD_LOOK(0x34, "Entity Head Look", PacketPlayOutEntityHeadRotation.class),
        WORLD_BORDER(0x35, "World Border", PacketPlayOutWorldBorder.class),
        CAMERA(0x36, "Camera", PacketPlayOutCamera.class),
        HELD_ITEM_CHANGE(0x37, "Held Item Change", PacketPlayOutHeldItemSlot.class),
        DISPLAY_SCOREBOARD(0x38, "Display Scoreboard", PacketPlayOutScoreboardDisplayObjective.class),
        ENTITY_METADATA(0x39, "Entity Metadata", PacketPlayOutEntityMetadata.class),
        ATTACH_ENTITY(0x3a, "Attach Entity", PacketPlayOutAttachEntity.class),
        ENTITY_VELOCITY(0x3b, "Entity Velocity", PacketPlayOutEntityVelocity.class),
        ENTITY_EQUIPMENT(0x3c, "Entity Equipment", PacketPlayOutEntityEquipment.class),
        SET_EXPERIENCE(0x3d, "Set Experience", PacketPlayOutExperience.class),
        UPDATE_HEALTH(0x3e, "Update Health", PacketPlayOutUpdateHealth.class),
        SCOREBOARD_OBJECTIVE(0x3f, "Scoreboard Objective", PacketPlayOutScoreboardObjective.class),
        SET_PASSENGERS(0x40, "Set Passengers", PacketPlayOutMount.class),
        TEAMS(0x41, "Teams", PacketPlayOutScoreboardTeam.class),
        UPDATE_SCORE(0x42, "Update Score", PacketPlayOutScoreboardScore.class),
        SPAWN_POSITION(0x43, "Spawn Position", PacketPlayOutSpawnPosition.class),
        TIME_UPDATE(0x44, "Time Update", PacketPlayOutUpdateTime.class),
        TITLE(0x45, "Title", PacketPlayOutTitle.class),
        SOUND_EFFECT(0x46, "Sound Effect", PacketPlayOutCustomSoundEffect.class),
        PLAYER_LIST_HEADER_FOOTER(0x47, "Player List Header and Footer", PacketPlayOutPlayerListHeaderFooter.class),
        COLLECT_ITEM(0x48, "Collect Item", PacketPlayOutCollect.class),
        ENTITY_TELEPORT(0x49, "Entity Teleport", PacketPlayOutEntityTeleport.class),
        ENTITY_PROPERTIES(0x4a, "Entity Properties", PacketPlayOutUpdateAttributes.class),
        ENTITY_EFFECT(0x4b, "Entity Effect", PacketPlayOutEntityEffect.class)
        ; //

        ClientBound(int id, String name, Class<? extends  Packet<? extends PacketListenerPlayOut> > cls) {
            this . id = id;
            this . name = name;
            this . aClass = cls;
        }

        private int id;
        private String name;
        private Class<? extends Packet> aClass;

        @Override
        public int getID() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<? extends Packet> getPacketClass() {
            return aClass;
        }

        @Override
        public PacketType.Bound boundTo() {
            return PacketType.Bound.Client;
        }

        @Override
        public <T extends Packet<?>> T createPacket() throws Exception {
            return (T) getPacketClass().newInstance();
        }

        @Override
        public <T extends Packet<?>> T createPacket(Object... objects) throws Exception {
            Exception eF = null;
            for (Constructor constructor : getPacketClass().getConstructors()) {
                if (constructor.getParameterCount() == objects.length) {
                    try {
                        return (T) constructor.newInstance(objects);
                    } catch(Exception e) {
                        eF = e;
                    }
                }
            }

            if (eF == null)
                return null;
            else
                throw eF;
        }
    }

    public enum ServerBound implements IPacket {

        TELEPORT_CONFIRM(0x00, "Teleport Confirm", PacketPlayInTeleportAccept.class),
        TAB_COMPLETE(0x01, "Tab Complete", PacketPlayInTabComplete.class),
        CHAT_MESSAGE(0x02, "Chat Message", PacketPlayInChat.class),
        CLIENT_STATUS(0x03, "Client Status", PacketPlayInKeepAlive.class),
        CLIENT_SETTINGS(0x04, "Client Settings", PacketPlayInSettings.class),
        CONFIRM_TRANSACTION(0x05, "Confirm Transaction", PacketPlayInTransaction.class),
        ENCHANT_ITEM(0x06, "Enchant Item", PacketPlayInEnchantItem.class),
        CLICK_WINDOW(0x07, "Click Window", PacketPlayInWindowClick.class),
        CLOSE_WINDOW(0x08, "Close Window", PacketPlayInCloseWindow.class),
        PLUGIN_MESSAGE(0x09, "Plugin Message", PacketPlayInCustomPayload.class)
        ;

        ServerBound(int id, String name, Class<? extends  Packet<? extends PacketListenerPlayIn> > cls) {
            this . id = id;
            this . name = name;
            this . aClass = cls;
        }

        private int id;
        private String name;
        private Class<? extends Packet> aClass;

        @Override
        public int getID() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<? extends Packet> getPacketClass() {
            return aClass;
        }

        @Override
        public PacketType.Bound boundTo() {
            return PacketType.Bound.Server;
        }

        @Override
        public <T extends Packet<?>> T createPacket() throws Exception {
            return (T) getPacketClass().newInstance();
        }

        @Override
        public <T extends Packet<?>> T createPacket(Object... objects) throws Exception {
            for (Constructor constructor : getPacketClass().getConstructors())
                if (constructor.getParameterCount() == objects.length)
                    return (T) constructor.newInstance(objects);
            return null;
        }
    }
}
