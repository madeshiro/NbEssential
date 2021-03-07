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

package nb.essential.blockmeta;

import nb.essential.main.NbEssential;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import zaesora.madeshiro.parser.ParseException;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONObject;
import zaesora.madeshiro.parser.json.JSONParser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static nb.essential.main.NbEssential.getBukkitServer;

/**
 * Class of NbEssential
 */
public class ChunkBlockData {

    private final Chunk chunk;
    private String jsonData;
    private JSONArray array;
    private final ArrayList<Location> blocks;

    ChunkBlockData(Chunk chunk) {
        this . chunk = chunk;
        this . jsonData = "[]";
        this . array = new JSONArray();
        blocks = new ArrayList<>();
    }

    public ChunkBlockData(Chunk chunk, String json) throws IllegalArgumentException {
        this . chunk = chunk;
        this . jsonData = json;
        this . blocks = new ArrayList<>();
        try {
            array = parse();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid json format exception");
        }
    }

    public final Chunk getChunk() {
        return chunk;
    }

    public void addBlock(Block block) {
        if (!blocks.contains(block.getLocation()))
            blocks.add(block.getLocation());
    }

    public void addBlock(Location location) {
        if (!blocks.contains(location))
            blocks.add(location);
    }

    public String generateJson() throws IOException {
        JSONArray main = new JSONArray();
        for (Location location : blocks) {
            JSONObject object = new JSONObject();
            object.put("x", location.getBlockX());
            object.put("y", location.getBlockY());
            object.put("z", location.getBlockZ());
            object.put("world", location.getWorld().getName());

            Block block = location.getBlock();
            JSONObject metadata = new JSONObject();
            for (MetaManager.KnowMeta meta : MetaManager.KnowMeta.values()) {
                List<org.bukkit.metadata.MetadataValue> values = block.getMetadata(meta.toString());
                if (values == null || values.isEmpty())
                    continue;

                for (org.bukkit.metadata.MetadataValue value : values) {
                    if (value instanceof MetadataValue)
                        if (((MetadataValue) value).isSavable())
                            metadata.put(meta.toString(), ((MetadataValue) value).saveValue());
                    else if (value.getOwningPlugin().equals(NbEssential.getPlugin()))
                        metadata.put(meta.toString(), value.value());
                    else continue;
                    break;
                }
            }

            if (metadata.isEmpty())
                continue;
            object.put("metadata", metadata);
            main.add(object);
        }

        StringWriter writer = new StringWriter();
        main.writeForFile(writer);
        jsonData = writer.toString();
        return jsonData;
    }

    public boolean load() {
        if (array == null)
            return false;

        for (Object obj : array) {
            if (obj instanceof JSONObject) {
                int x = ((JSONObject) obj).<Long>getObject("x").intValue();
                int y = ((JSONObject) obj).<Long>getObject("y").intValue();
                int z = ((JSONObject) obj).<Long>getObject("z").intValue();
                World world = getBukkitServer().getWorld((String) ((JSONObject) obj).get("world"));
                if (world == null) {
                    continue;
                }
                Block block = world.getBlockAt(x,y,z);
                JSONObject metadata = (JSONObject) ((JSONObject) obj).get("metadata");
                if (metadata == null || metadata.isEmpty())
                    continue;
                for (String metaName : metadata.keySet()) {
                    if (MetaManager.KnowMeta.valueOf(metaName) != null) {
                        block.setMetadata(metaName,
                           MetaManager.KnowMeta.valueOf(metaName).getMetadata(metadata.get(metaName)));
                    }
                    else
                        block.setMetadata(metaName, new MetadataValue(metadata.get(metaName)));
                    blocks.add(block.getLocation());
                }
            }
        }

        return true;
    }

    private JSONArray parse() throws ParseException {
        JSONParser parser = new JSONParser();
        return parser.parse(jsonData);
    }

    byte[] json() {
        return jsonData.getBytes();
    }
}
