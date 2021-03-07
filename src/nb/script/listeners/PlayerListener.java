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
package nb.script.listeners;

import nb.essential.loader.EventListener;
import nb.script.EventManager;
import nb.script.exception.ScriptException;
import nb.script.functions.Function;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Collections;

import static nb.essential.player.PlayerSystem.Nb_GetPlayer;
import static nb.script.EventManager.SupportedEvent.*;
import static nb.script.ScriptManager.Nb_SL_GetEventManager;

/**
 * Class of NbEssential in package nb.script.listeners
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
@EventListener
public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        Function.Args player = new Function.Args("$player", Collections.singletonList(Nb_GetPlayer(event.getPlayer()))),
                      text = new Function.Args("$text", event.getMessage()),
                      selector = new Function.Args("@s", player.value);

        try {
            Nb_SL_GetEventManager()
                    .getHandler()
                    .doEvent(OnPlayerChatEvent, player, text, selector);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Function.Args player = new Function.Args("$player", Collections.singletonList(Nb_GetPlayer(event.getPlayer()))),
                selector = new Function.Args("@s", player.value);

        try {
            Nb_SL_GetEventManager()
                    .getHandler()
                    .doEvent(OnPlayerMoveEvent, player, selector);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerRightClickAtEntityEvent(PlayerInteractAtEntityEvent event) {
        Function.Args player = new Function.Args("$player", Collections.singletonList(Nb_GetPlayer(event.getPlayer()))),
                entity = new Function.Args("$entity", event.getRightClicked()),
                selector = new Function.Args("@s", player.value),
                SELECTORS = new Function.Args("Selector", entity.value);

        try {
            Nb_SL_GetEventManager()
                    .getHandler()
                    .doEvent(OnPlayerRightClickAtEntityEvent, player, entity, selector, SELECTORS);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerBreakBlockEvent(BlockBreakEvent event) {
        Function.Args player = new Function.Args("$player", Collections.singletonList(Nb_GetPlayer(event.getPlayer()))),
                block = new Function.Args("$block", event.getBlock()),
                selector_s = new Function.Args("@s", player.value),
                selector_b = new Function.Args("@b", block.value);

        try {
            Nb_SL_GetEventManager()
                    .getHandler()
                    .doEvent(OnPlayerBreakBlockEvent,
                            player, block, selector_s, selector_b);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
