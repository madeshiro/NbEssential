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
package nb.script.functions;

import nb.essential.main.NbEssential;
import nb.script.exception.ScriptException;
import nb.script.parser.ParseInfo;
import nb.script.type.RootChunk;
import nb.script.type.objects.NbslNPC;
import nb.script.type.objects.NbslPlayer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

import static nb.script.ScriptManager.*;

/**
 * Class of NbEssential in package nb.script.functions
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class NbslFunctionAPI {
    private static <T> T GetValue(String key, HashMap<String, Object> args, RootChunk root) throws ScriptException {
        if (!args.containsKey(key))
            throw new ScriptException(root, "Unset Var");

        try {
            return (T) args.get(key);
        } catch (ClassCastException e) {
            throw new ScriptException(root, e.getMessage(), e.getCause());
        }
    }

    public static Method GetAPIMethod(ParseInfo info, String name) throws ScriptException {
        try {
            return NbslFunctionAPI.class.getMethod(name, RootChunk.class, HashMap.class);
        } catch (NoSuchMethodException e) {
            throw new ScriptException(info.getRoot(), e.getMessage(), e.getCause());
        }
    }

    /* --------------------------------------- NPCAPI --------------------------------------- */

    /* --> global functions <-- */

    public static void Broadcast(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        NbEssential.getServer().broadcastMsg(GetValue("Text", args, root));
    }

    public static void LogInfo(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        Nb_SL_LogInfo(root, GetValue("Text", args, root));
    }

    public static void LogWarning(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        Nb_SL_LogWarning(root, GetValue("Text", args, root));
    }

    public static void LogSevere(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        Nb_SL_LogSevere(root, GetValue("Text", args, root));
    }

    /* --> player functions <-- */

    public static void PlayerSendMessage(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        Collection<NbslPlayer> players = GetValue("Player", args, root);
        if (args.containsKey("Text"))
            PlayerFunction.SendMessage(players, (String) args.get("Text"));
        else
            PlayerFunction.SendRawMessage(players, GetValue("Json", args, root));
    }

    /* --> npc functions <-- */

    public static void NPCCry(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        NbslNPC npc = GetValue("Npc", args, root);
        String msg = GetValue("Text", args, root);

        NpcFunction.SendMessage(npc, msg, 2);
    }

    public static void NPCSay(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        NbslNPC npc = GetValue("Npc", args, root);
        String msg = GetValue("Text", args, root);

        NpcFunction.SendMessage(npc, msg, 1);
    }

    public static void NPCWhisper(RootChunk root, HashMap<String, Object> args) throws ScriptException {
        NbslNPC npc = GetValue("Npc", args, root);
        String msg = GetValue("Text", args, root);

        NpcFunction.SendMessage(npc, msg, 2);
    }
}
