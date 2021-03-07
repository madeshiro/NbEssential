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

import nb.essential.loader.Loader;
import nb.essential.loader.PluginData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;

/**
 * Class of NbEssential
 */
@Loader("block_metadata")
public class BlockData implements PluginData {

    private final String metaFolder = "plugins/NbEssential/blockmeta/";
    private HashMap<String, ChunkBlockData> container;

    public String getName() {
        return "BlockMetadata";
    }

    /**
     * Called to save this data of this database
     *
     * @return A boolean, true if the save action ended with success, false otherwise.
     */
    @Override
    public boolean save() {
        boolean status = true;
        for (ChunkBlockData data : container.values()) {
            status = saveChunk(data.getChunk()) && status;
        }

        return status;
    }

    /**
     * Called to load the data from file and store it in this database.
     * This function is often called by the {@link #reload()} function.
     *
     * @return A boolean, true if the load action ended with success, false otherwise.
     */
    @Override
    public boolean load() {
        container = new HashMap<>();

        boolean status = true;
        for (File file : new File(metaFolder).listFiles()) {
            Chunk chunk = b(file.getName());
            if (chunk == null)
                logwarning("Unable to load " + file.getName());
            else if (loadChunk(chunk)) {
                loginfo("Load chunk '(" + chunk.getX()+";" + chunk.getZ() + ";" + chunk.getWorld().getName() + ")");
            } else {
                status = false;
                logwarning("Unable to load chunk '(" + chunk.getX()+";" + chunk.getZ() + ";" + chunk.getWorld().getName() + ")");
            }
        }
        return status;
    }

    public boolean loadChunk(Chunk chunk) {
        if (!chunk.isLoaded())
            chunk.load();

        File chunkFile = new File(metaFolder + a(chunk) + ".dat");
        if (!chunkFile.exists())
            return true;

        String read;
        if ((read = read(chunkFile)) == null)
            return false;

        try {
            container.put(chunk.toString(), new ChunkBlockData(chunk, read));
            return container.get(chunk.toString()).load();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveChunk(Chunk chunk) {
        if (!container.containsKey(chunk.toString()))
            return false;

        File chunkFile = new File(metaFolder + a(chunk) + ".dat");
        try {
            if (!chunkFile.exists())
                chunkFile.createNewFile();
            return write(container.get(chunk.toString()).generateJson().getBytes(), chunkFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String a(Chunk chunk) {
        return "mcd_" + chunk.getX() + "#" + chunk.getZ() + "#" + chunk.getWorld().getName();
    }

    private Chunk b(String f) {
        f = f.replace("mcd_", "").replace(".dat", "");
        String[] args = f.split("#");

        int x = Integer.parseInt(args[0]);
        int z = Integer.parseInt(args[1]);
        World world = Bukkit.getWorld(args[2]);
        if (world == null) {
            logwarning("World '" + args[2] + "' doesn't exists ! Cannot load <" + f + ">");
            return null;
        } else
            return world.getChunkAt(x,z);
    }

    public void put(Block block) {
        if (!container.containsKey(block.getChunk().toString()))
            container.put(block.getChunk().toString(), new ChunkBlockData(block.getChunk()));

        container.get(block.getChunk().toString()).addBlock(block);
    }

    /**
     * Called to reload data.
     * @implNote The function will do nothing here ! reload's function isn't supported
     * @return A boolean, true if the reload action ended with success, false otherwise.
     */
    @Override
    public boolean reload() {
        return true;
    }

    public boolean write(byte[] jsondata, File file) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(Base64.getEncoder().encode(jsondata));
            stream.close();
        } catch (IOException e) {
            return false;
        }

        loginfo("Successfuly write data in <" + file.getName() + ">");
        return true;
    }

    public String read(File file) {
        try {
            String read = "";
            for (String str : Files.readAllLines(file.toPath()))
                read += str;

            return new String(Base64.getDecoder().decode(read));
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isLoad(Chunk chunk) {
        return container.containsKey(chunk.toString());
    }
}
