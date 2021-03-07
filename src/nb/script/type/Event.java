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
package nb.script.type;

import nb.script.EventManager;
import nb.script.exception.ScriptException;
import nb.script.functions.Function;
import nb.script.parser.ParseInfo;
import nb.script.type.interfaces.Copyable;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;

import static nb.script.ScriptManager.Nb_SL_GetEventManager;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class Event extends CodeChunk {

    private boolean listen;
    private EventManager.SupportedEvent eventType;

    private HashSet<Entity> Selectors;
    private ArrayList<String> renamedArgs;

    public Event(ParseInfo info, EventManager.SupportedEvent eventType) {
        super(info);

        this.listen = false;
        this.eventType = eventType;
        this.Selectors = new HashSet<>();
        this.renamedArgs = new ArrayList<>();
    }

    protected Event(Event chunk, CodeChunk parent) {
        super(chunk, parent);

        this.listen = false;
        this.eventType = chunk.eventType;
        this.Selectors = chunk.Selectors;
        this.renamedArgs = chunk.renamedArgs;
    }

    @Override
    public void run() {
        // listen event
        if (!listen && (listen=true))
            Nb_SL_GetEventManager().listenEvent(this);
    }

    public void onEvent(Function.Args... args) throws ScriptException {
        for (int i = 0; i < renamedArgs.size() && i < args.length && !args[i].name.startsWith("@"); i++)
            args[i].name = renamedArgs.get(i);

        for (Function.Args arg : args)
            defineLocalVariable(arg.name, arg.value);

        //noinspection SuspiciousMethodCalls
        if (eventType.isSupportSelectors() && !Selectors.isEmpty() && !Selectors.contains(getLocalVariable("Selector")))
            return;

        super.run();
    }

    public void Return() {
        rturn = true;
    }

    @Override
    public void setWaiting(boolean value) {
        // no effect
        this.wait = value;
    }

    @Override
    public void addAtTag(MetaInstruction atTag) {
        //noinspection Duplicates
        if (atTag.isCompatibleWith(this)) {
            atTags.add(atTag);

            switch (atTag) {
                case Sync:
                    atTags.remove(MetaInstruction.Async);
                    break;
                case Async:
                    atTags.remove(MetaInstruction.Sync);
                    break;
            }
        }
    }

    /**
     * Defines which CodeChunk is allowed to be declared in this chunk.
     *
     * @return An array of allowed {@link CodeChunk} classes.
     * @see #isAllow(CodeChunk)
     */
    @Override
    protected Class<? extends CodeChunk>[] allowCodeChunks() {
        return new Class[] {
                Loop.class,
                Step.class,
                Conditions.class
        };
    }

    @Override
    public Copyable copy(CodeChunk parent) {
        return new Event(this, parent);
    }

    public EventManager.SupportedEvent getEventType() {
        return eventType;
    }
}
