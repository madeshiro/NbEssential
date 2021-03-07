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
package nb.economy.job;

import org.bukkit.Material;

import static nb.economy.job.JobEnum.*;

/**
 * Class of NbEssential in package nb.economy.job
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
public enum BlockList {
    //FIXME MATERIAL ENUM CHANGE

    Air(Material.AIR, all(), all(), all(), all()),
    Stone(Material.STONE, a(Miner), a(Craftsman), all(), neither()),
    Grass(Material.GRASS, all(), neither(), all(), neither()),
    Dirt(Material.DIRT, all(), neither(), all(), neither()),
    Cobblestone(Material.COBBLESTONE, a(Miner), neither(), neither(), neither()),
    WoodPlank(Material.OAK_PLANKS, neither(), a(Craftsman, Woodcutter), neither(), neither()),
    Sapling(Material.OAK_SAPLING, all(), neither(), all(), all()),
    Bedrock(Material.BEDROCK),
    Water(Material.WATER, all(), neither(), all(), all()),
    Stationary_Water(Material.WATER_BUCKET, all(), neither(), all(), all()),
    Lava(Material.LAVA, a(Blacksmith, Miner), neither(), all(), all()),
    Stationary_Lava(Material.LAVA_BUCKET, a(Blacksmith, Miner), neither(), all(), all()),
    Sand(Material.SAND, all(), neither(), all(), all()),
    Gravel(Material.GRAVEL, all(), neither(), all(), all()),
    Gold_Ore(Material.GOLD_ORE, a(Miner), neither(), a(Miner), all()),
    Iron_Ore(Material.IRON_ORE, a(Miner), neither(), a(Miner), all()),
    Coal_Ore(Material.COAL_ORE, a(Miner), neither(), a(Miner), all()),
    Log(Material.OAK_LOG, a(Woodcutter), neither(), neither(), a(Craftsman, Woodcutter)),
    Leaves(Material.OAK_LEAVES, all(), neither(), all(), all()),
    Sponge(Material.SPONGE, all(), all(), all(), all()),
    Glass(Material.GLASS, a(Craftsman), a(Craftsman), neither(), all()),
    Lapis_Ore(Material.LAPIS_ORE, a(Miner), neither(), neither(), all()),
    Lapis_Block(Material.LAPIS_BLOCK, neither(), a(Craftsman, Miner), neither(), all()),
    Dispenser(Material.DISPENSER, all(), a(Engineer), all(), all()),
    Sandstone(Material.SANDSTONE, all(), all(), neither(), all()),
    Note_Block(Material.NOTE_BLOCK, all(), a(Engineer), all(), all()),
    Bed(Material.RED_BED, all(), a(Craftsman), all(), all()),
    Powered_Rail(Material.POWERED_RAIL, a(Engineer), a(Engineer), neither(), all()),
    Detector_Rail(Material.DETECTOR_RAIL, a(Engineer), a(Engineer), neither(), all()),
    Sticky_Piston(Material.STICKY_PISTON, a(Engineer), a(Engineer), neither(), all()),
    Cobweb(Material.COBWEB, all(), neither(), all(), all()),
    Long_Grass(Material.TALL_GRASS, all(), neither(), all(), all()),
    Dead_Bush(Material.DEAD_BUSH, all(), neither(), all(), all()),
    Piston(Material.PISTON, a(Engineer), a(Engineer), neither(), all()),
    Piston_Head(Material.PISTON_HEAD, a(Engineer), neither(), neither(), neither()),
    Wool(Material.WHITE_WOOL, neither(), a(Tailor), neither(), all()),
    Yellow_Flower(Material.SUNFLOWER, all(), neither(), all(), all()),
    Red_Flower(Material.ROSE_RED, all(), neither(), all(), all()),
    Brown_Mushroom(Material.BROWN_MUSHROOM, all(), neither(), all(), all()),
    Red_Mushromm(Material.RED_MUSHROOM, all(), neither(), all(), all()),
    Gold_Block(Material.GOLD_BLOCK, neither(), a(Blacksmith), neither(), all()),
    Iron_Block(Material.IRON_BLOCK, neither(), a(Blacksmith), neither(), all()),

    /* Craftsman("Artisan"),
     * Architect("Architecte"),
     * Alchemist("Alchimiste"),
     * Pyrotechnician("Artificier"),
     * Woodcutter("Bucheron"),
     * Enchanter("Enchanteur"),
     * Explorer("Exploreur"),
     * Farmer("Fermier"),
     * Blacksmith("Forgeron"),
     * Builder("Architecte"),
     * Mercenary("Mercenaire"),
     * Miner("Mineur"),
     * Sinner("Pécheur"),
     * Engineer("Ingénieur"),
     * Tailor("Tailleur");
     */
    ;
    private final Material material;
    private final int pickup, craft, pose, use;

    private static int a(JobEnum... jobs) {
        int _f = 0;
        for (JobEnum job : jobs)
            _f |= (1 << job.ordinal());
        return _f;
    }

    private static int a(int... job_id) {
        int _f = 0;
        for (int id : job_id)
            _f |= (1 << id);
        return _f;
    }

    private static int all() {
        return 0xffff;
    }

    private static int neither() {
        return 0x0;
    }

    public static BlockList getBlockByMat(Material material) {
        for (BlockList block : values())
            if (block.material.equals(material))
                return block;
        return Air; // default all authorized
    }

    @SuppressWarnings("SameParameterValue")
    BlockList(Material material) {
        this.material = material;

        pickup = neither();
        craft = neither();
        pose = neither();
        use = neither();
    }

    BlockList(Material material, int pickup, int craft, int pose, int use) {
        this.material = material;

        this.pickup = pickup;
        this.craft = craft;
        this.pose = pose | Architect.getAuthFlag();
        this.use = use;
    }

    public int pickupFlag() {
        return pickup;
    }

    public boolean isPickupAllow(Job job) {
        return (pickup & job.getType().getAuthFlag()) != 0;
    }

    public boolean isPickupAllow(JobEnum jobEnum) {
        return (pickup & jobEnum.getAuthFlag()) != 0;
    }

    public int craftFlag() {
        return craft;
    }

    public boolean isCraftAllow(Job job) {
        return (craft & job.getType().getAuthFlag()) != 0;
    }

    public boolean isCraftAllow(JobEnum jobEnum) {
        return (craft & jobEnum.getAuthFlag()) != 0;
    }

    public int poseFlag() {
        return pose;
    }

    public boolean isPoseAllow(Job job) {
        return (pose & job.getType().getAuthFlag()) != 0;
    }

    public boolean isPoseAllow(JobEnum jobEnum) {
        return (pose & jobEnum.getAuthFlag()) != 0;
    }

    public int useFlag() {
        return use;
    }

    public boolean isUseAllow(Job job) {
        return (use & job.getType().getAuthFlag()) != 0;
    }

    public boolean isUseAllow(JobEnum jobEnum) {
        return (use & jobEnum.getAuthFlag()) != 0;
    }
}
