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

import nb.script.exception.ScriptException;
import nb.script.parser.ParseInfo;
import nb.script.type.interfaces.AtTag;
import nb.script.type.interfaces.Copyable;
import nb.script.type.interfaces.Instruction;
import nb.script.type.interfaces.VariableContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static nb.script.ScriptManager.Nb_SL_GetEventManager;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public abstract class CodeChunk implements VariableContainer, Copyable {

    private String name;

    private int line;
    private String fileName;

    protected RootChunk root;
    protected CodeChunk parent;

    protected boolean eventLoaded = false;
    protected HashSet<Event> events;
    protected HashMap<String, CodeChunk> declarations;

    private int instruction;
    private ArrayList<Instruction> instructions;
    protected boolean wait = false;
    protected boolean rturn = false;

    protected HashMap<String, Object> variables;
    protected HashSet<AtTag> atTags;

    /* --- constructors --- */

    public CodeChunk(ParseInfo info) {
        this . name = info.getContext().getName();
        this . line = info.getLine();
        this . fileName = info.getFileName();

        this . root = info.getRoot();
        this . parent = info.getParent();


        this . declarations = new HashMap<>();
        this . events = new HashSet<>();

        this . instruction = 0;
        this . instructions = new ArrayList<>();

        this . variables = new HashMap<>();
        this . atTags = new HashSet<>();
    }

    protected CodeChunk(CodeChunk chunk, CodeChunk parent) {
        this.name = chunk.name;
        this.line = chunk.line;
        this.fileName = chunk.fileName;

        this.root = chunk.root;
        this.parent = parent;

        this.eventLoaded = chunk.eventLoaded;
        this.declarations = CopyMaps(chunk.declarations);
        this.events = CopySets(chunk.events);

        this.instruction = chunk.instruction;
        this.instructions = chunk.instructions;

        this . wait = chunk.wait;
        this . rturn = chunk.rturn;

        variables = new HashMap<>(chunk.variables);
        atTags = new HashSet<>(chunk.atTags);
    }

    private <T extends CodeChunk> HashSet<T> CopySets(HashSet<T> collection) {
        HashSet<T> chunks = new HashSet<>();
        for (T chunk : collection)
            chunks.add((T) chunk.copy(/* parent */this));
        return chunks;
    }

    private <T extends Copyable> HashMap<String, T> CopyMaps(HashMap<String, T> hashMap) {
        HashMap<String, T> map = new HashMap<>();
        for (String key : hashMap.keySet())
            map.put(key, (T) hashMap.get(key).copy(/* parent */this));
        return map;
    }

    /* --- action --- */

    public void run() throws ScriptException {
        if (!eventLoaded) {
            this.instruction = 0;
            this.wait = false;
            this.rturn = false;
            this.eventLoaded = true;

            for (Event event : events)
                if (!event.hasAtTag(MetaInstruction.DisabledEvent))
                    event.run();
        }

        do {
            if (isWaiting())
                return;
        } while(!rturn && nextInstruction());

        Delete();
    }

    private boolean nextInstruction() throws ScriptException {
        if (instructions.size() <= instruction)
            return false;

        instructions.get(instruction).run(this);
        instruction++;
        return true;
    }

    public boolean isWaiting() {
        return wait;
    }

    /* --- properties --- */

    public boolean isRoot() {
        return false;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public int getInstructionsAmount() {
        return instructions.size();
    }

    /**
     * Defines which CodeChunk is allowed to be declared in this chunk.
     *
     * @see #isAllow(CodeChunk)
     * @return An array of allowed {@link CodeChunk} classes.
     */
    protected abstract Class<? extends CodeChunk>[] allowCodeChunks();

    public <T extends CodeChunk> boolean isAllow(T chunk) {
        for (Class<? extends CodeChunk> cls : allowCodeChunks())
            if (cls.isInstance(chunk))
                return true;
        return false;
    }

    /* --- get --- */

    public String getName() {
        return name;
    }

    public String getScriptName() {
        return root.getName();
    }

    public int getLine() {
        return line;
    }

    public String getFileName() {
        return fileName;
    }

    /* --- meta instruction --- */

    public boolean hasAtTag(String name) {
        return getAtTag("name") != null;
    }

    public boolean hasAtTag(AtTag instruction) {
        return atTags.contains(instruction);
    }

    public <T extends AtTag> T getAtTag(String name) {
        for (AtTag tag : atTags)
            if (tag.getName().equals(name))
                return (T) tag;
        return null;
    }

    /* --- keyword behavior --- */

    public void setWaiting(boolean value) throws ScriptException {

        if (value != wait) {
            if (parent != null)
                parent.setWaiting(value);


            if (!value && isRoot()) {
                this . wait = value;
                run();
            }

            this.wait = value;
        }
    }

    public void Return() {
        this.rturn = true;

        if (hasParent())
            parent.Return();
    }

    public void Delete() {
        if (eventLoaded) {
            for (Event event : events) {
                Nb_SL_GetEventManager().unregisterEvent(event);
            }

            eventLoaded = false;
        }

        Return();
    }

    /* --- parser --- */

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void addAtTag(AtTag atTag) {
        atTags.add(atTag);
    }

    public abstract void addAtTag(MetaInstruction atTag);

    /* --- variables --- */

    @Override
    public HashMap<String, Object> getGlobalVariables() {
        return root.getGlobalVariables();
    }

    @Override
    public HashMap<String, Object> getLocalisedVariables() {
        return variables;
    }

    /**
     * Gets local variable according to his name. In case of undefined var in
     * the current CodeChunk, the function will look at parent CodeChunk.
     *
     * @param name The variable name
     * @return The local variable value if found, Null otherwise.
     */
    @Override
    public Object getLocalVariable(String name) {
        if (variables.containsKey(name))
            return variables.get(name);
        else if (parent != null)
            return parent.getLocalVariable(name);
        else
            return null;
    }

    /**
     * Sets a variable value if this one has been defined. <br/>
     * The function will first searched for local var if <code>isGlobal == false</code>
     * and, in case of non-existing local var, the function will look at global variables.
     *
     * @param name     The variable full-name (including special char like '@' or '$')
     * @param value    The variable value
     * @param isGlobal A boolean, if true, tell the function to directly look at global var. Otherwise,
     *                 the function will look at both var containers.
     * @return True if the variable exists, false otherwise.
     * @see #defineLocalVariable(String, Object)
     * @see #defineGlobalVariables(String, Object)
     */
    @Override
    public boolean setVariable(String name, Object value, boolean isGlobal) {
        if (!isGlobal && isLocalVariableExists(name)) {
            if (variables.containsKey(name))
                variables.put(name, value);
            else
                return parent.setVariable(name, value, false);
        }

        // look at global var
        if (!getGlobalVariables().containsKey(name))
            return false;
        else
            getGlobalVariables().put(name, value);

        return true;
    }

    @Override
    public void defineLocalVariable(String name, Object value) {
        variables.put(name, value);
    }

    @Override
    public void defineGlobalVariables(String name, Object value) {
        root.getGlobalVariables().put(name, value);
    }

    @Override
    public void deleteVariable(String name, boolean isGlobal) {
        if (!isGlobal && isLocalVariableExists(name)) {
            if (variables.containsKey(name))
                variables.remove(name);
            else if (parent != null)
                parent.deleteVariable(name, false);
        } else {
            getGlobalVariables().remove(name);
        }
    }

    @Override
    public boolean isLocalVariableExists(String name) {
        return variables.containsKey(name) || (parent != null && parent.isLocalVariableExists(name));
    }

    /* --- CodeChunk caller --- */

    public void runDeclareChunk(String name) throws ScriptException {
        try {
            getDeclareChunk(name, true).run();
        } catch (NullPointerException e) {
            throw new ScriptException(root, "CodeChunk Not Found <" + name + "> !");
        }
    }

    public void runDeclareChunk(String name, boolean deep) throws ScriptException {
        try {
            getDeclareChunk(name, deep).run();
        } catch (NullPointerException e) {
            throw new ScriptException(root, "CodeChunk Not Found <"+name+"> !");
        }
    }

    public CodeChunk getDeclareChunk(String name) {
        return getDeclareChunk(name, true);
    }

    public CodeChunk getDeclareChunk(String name, boolean deep) {
        if (declarations.containsKey(name))
            return declarations.get(name);
        else if (deep && hasParent())
            return parent.getDeclareChunk(name, true);
        else return null;
    }

    public RootChunk getRoot() {
        return root;
    }

    public void interrupt() throws ScriptException {
        Delete();
        for (CodeChunk code : declarations.values())
            code.interrupt();
    }
}
