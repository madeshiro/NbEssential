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

import nb.essential.stick.Chairs;
import nb.essential.stick.LockDoor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import zaesora.madeshiro.parser.json.JSONObject;

import static nb.essential.main.NbEssential.getPlugin;
import static nb.essential.main.NbEssential.getDataLoader;

/**
 * Class of NbEssential
 */
public class MetaManager {

    public static <T> void Nb_BlockSetMeta(Block block, String meta, T value) {
        Nb_BlockSetMeta(block, meta, new MetadataValue(value));
    }

    public static void Nb_BlockSetMeta(Block block, String meta, MetadataValue value) {
        block.setMetadata(meta, value);
        getBlockData().put(block); // if already exists, nothing happened
    }

    public static <T extends MetadataValue> T Nb_BlockGetMeta(Block block, String meta) {
        for (org.bukkit.metadata.MetadataValue value : block.getMetadata(meta))
            if (value.getOwningPlugin().equals(getPlugin()))
                return (T) value;

        return null;
    }

    public static void Nb_BlockRemoveMeta(Block block, String meta) {
        if (Nb_BlockHasMeta(block, meta))
            block.removeMetadata(meta, getPlugin());
    }

    public static boolean Nb_BlockHasMeta(Block block, String meta) {
        for (org.bukkit.metadata.MetadataValue value : block.getMetadata(meta))
            if (value.getOwningPlugin().equals(getPlugin()))
                return true;

        return false;
    }

    public static boolean Nb_BlockLoadMeta(Chunk chunk) {
        return getBlockData().loadChunk(chunk);
    }

    public static boolean Nb_IsChunkDataLoaded(Chunk chunk) {
        return getBlockData().isLoad(chunk);
    }

    private static BlockData getBlockData() {
        return getDataLoader().getData("block_metadata");
    }

    enum KnowMeta {
        chairs(value -> new Chairs.ChairsMetadataValue((Boolean) value)),
        streetlight(value -> new MetadataValue(value, true)),
        lockdoor(value -> new LockDoor.LockMetadataValue((JSONObject) value));

        final CustomRunnable runnable;
        KnowMeta(CustomRunnable runnable) {
            this . runnable = runnable;
        }

        public org.bukkit.metadata.MetadataValue getMetadata(Object value) {
            return runnable.createMetadata(value);
        }

        private interface CustomRunnable {
            org.bukkit.metadata.MetadataValue createMetadata(Object value);
        }
    }
}
