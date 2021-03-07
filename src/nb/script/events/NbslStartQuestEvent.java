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
package nb.script.events;

import nb.script.type.Quest;
import nb.script.type.objects.NbslPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class NbslStartQuestEvent extends Event {

    private Quest quest;
    private boolean resume;
    private String resumeStep;
    private Collection<NbslPlayer> players;
    private final static HandlerList handlers = new HandlerList();

    public NbslStartQuestEvent(Quest quest, Collection<NbslPlayer> concernedPlayers) {
        this.players = concernedPlayers;
        this.quest = quest;

        this.resume = false;
        this.resumeStep = "";
    }

    public NbslStartQuestEvent(Quest quest, Collection<NbslPlayer> concernedPlayers, String resumeStep) {
        this(quest, concernedPlayers);

        this.resume = true;
        this.resumeStep = resumeStep;
    }

    public Quest getQuest() {
        return quest;
    }

    public Collection<NbslPlayer> getPlayers() {
        return players;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isResume() {
        return resume;
    }
}
