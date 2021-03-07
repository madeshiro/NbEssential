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

package nb.essential.ressourcepack;

import org.bukkit.block.Biome;
import static org.bukkit.block.Biome.*;

/**
 * Class of NbEssential
 */
public interface Music extends Sound {

    static Music getMusic(String path) {
        return List.getMusic(path);
    }
    static Music[] getMusics() {
        return List.values();
    }

    @Override
    default Type getSoundType() {
        return Type.Music;
    }

    final class Description {
        private final String name;
        private final String author;
        private final String album;

        private final long duration;

        private Description(String name, String author, long duration) {
            this(name, author, "unknown album", duration);
        }

        private Description(String name, String author, String album, long duration) {
            this . name = name;
            this . author = author;
            this . album = album;
            this . duration = duration;
        }

        public String getName() {
            return name;
        }

        public String getAuthor() {
            return author;
        }

        public String getAlbum() {
            return album;
        }
    }

    Description getDescription();

    enum List implements Music {
        stopMusic("", a("","",0), THE_VOID),

        ambiance_dark_1("newbeginning.ambiance.dark.dark1", a("plain_darkened", "Spellforce - Breath of Winter", 424), SNOWY_TAIGA),
        ambiance_dark_2("newbeginning.ambiance.dark.dark2", a("plain_enchanted", "Spellforce - Breath of Winter", 393), SNOWY_TAIGA),
        ambiance_dark_3("newbeginning.ambiance.dark.dark3", a("forbidden_zone", "Spellforce - The Order of Dawn", 139), SNOWY_TAIGA),
        ambiance_mountain_1("newbeginning.ambiance.mountain.mountain1", a("plain_mystified", "Spellforce - Breath of Winter", 427), SWAMP_HILLS, DARK_FOREST_HILLS, MOUNTAINS),
        ambiance_mountain_2("newbeginning.ambiance.mountain.mountain2", a("plain_waterworld2", "Spellforce - The Order of Dawn", 491), DARK_FOREST_HILLS, SNOWY_MOUNTAINS),
        ambiance_mountain_3("newbeginning.ambiance.mountain.mountain3", a("plain_mountainworld", "Spellforce - The Order of Dawn", 506), DARK_FOREST_HILLS, SNOWY_MOUNTAINS),
        ambiance_plain_1("newbeginning.ambiance.plain.plain1", a("sf2_plain_in_freedom", "Spellforce 2 - Shadow Wars", 376), FOREST, PLAINS, RIVER),
        ambiance_plain_2("newbeginning.ambiance.plain.plain2", a("sf2_location_dun_mora", "Spellforce 2 - Shadow Wars", 137), FOREST, PLAINS, RIVER),
        ambiance_plain_3("newbeginning.ambiance.plain.plain3", a("sf2_plain_in_oblivion", "Spellforce 2 - Shadow Wars", 363), FOREST, PLAINS, RIVER),
        ambiance_plain_4("newbeginning.ambiance.plain.plain4", a("plain_waterwold", "Spellforce - The Order of Dawn", 557), FOREST, PLAINS, RIVER),
        ambiance_plain_5("newbeginning.ambiance.plain.plain5", a("The World of Zarach", "Spellforce 2 - Demons of the Past", 125), FOREST, PLAINS, RIVER),
        ambiance_plain_6("newbeginning.ambiance.plain.plain6", a("ds_06_lullaby_of_death", "Spellforce 2 - Dragon Storm", 125), FOREST, PLAINS, RIVER),
        ambiance_plain_7("newbeginning.ambiance.plain.plain7", a("plain_world", "Spellforce - The Order of Dawn", 482), FOREST, PLAINS, RIVER),

        dungeon_air_ambient("newbeginning.dungeon.air.ambient", a("Ivory Citadel", "Darksiders 2", 90), THE_VOID),
        dungeon_castle_ambient("newbeginning.dungeon.castle.ambient", a("The Crypt", "Darksiders 2", 120), THE_VOID),
        dungeon_castle_boss("newbeginning.dungeon.castle.boss", a("Guanzorumu", "Monster Hunter Frontier", 72), THE_VOID),
        dungeon_end_ambient("newbeginning.dungeon.end.ambient", a("Magical_Dark_Volcanic_Wasteland", "Spellforce 2 - Faith in Destiny", 240), THE_VOID),
        dungeon_end_boss("newbeginning.dungeon.end.boss", a("Prepare_For_The_Battle", "Spellforce 2 - Faith in Destiny", 60), THE_VOID),
        dungeon_end_boss_last("newbeginning.dungeon.end.boss_last", a("A Contest Of Aeons", "Final Fantasy X HD Remastered", 209), THE_VOID),
        dungeon_end_last_step("newbeginning.dungeon.end.last_step", a("A Contest Of Aeons", "Final Fantasy X HD Remastered", 88), THE_VOID),
        dungeon_end_tower("newbeginning.dungeon.end.tower", a("Ithavoll Building - Lower Floor", "Bayonetta", 66), THE_VOID),
        dungeon_generic_battle("newbeginning.dungeon.generic.battle", a("Battle", "Spellforce 2 - Faith in Destiny", 60), THE_VOID),
        dungeon_generic_forest("newbeginning.dungeon.generic.forest", a("Into Eternity", "Darksiders 2", 157), THE_VOID),
        dungeon_volcano_boss1("newbeginning.dungeon.volcano.boss1", a("Death Confront Samael", "Darksiders 2", 79), THE_VOID),
        dungeon_volcano_boss2("newbeginning.dungeon.volcano.boss2", a("G-Rank Crimson Fatalis Part 2", "Monster Hunter Frontier", 79), THE_VOID),
        dungeon_volcano_miboss("newbeginning.dungeon.volcano.miboss", a("Cavalier", "Halo 5 : Guardian", 135), THE_VOID),
        dungeon_water_boss1("newbeginning.dungeon.water.boss1", a("Moonquake", "Monster Hunter 3 Tri", 78), THE_VOID),
        dungeon_water_boss2("newbeginning.dungeon.water.boss2", a("Gammoth Theme", "Monster Hunter X", 69), THE_VOID),
        dungeon_water_dungeon_loop("newbeginning.dungeon.water.dungeon_loop", a("Demise of the Traditions", "Monster Hunter 3 Tri", 19), THE_VOID),

        event_event1("newbeginning.event.event1", a("Snowboard Race Music", "Rayman 3", 127), THE_VOID),
        misc_game_over("newbeginning.misc.game_over", a("Silence(Game_Over)", "Spellforce 2 - Faith in Destiny", 48), THE_VOID),

        town_village_1("newbeginning.town.village_1", a("human_village", "Spellforce - Breath of Winter", 61), THE_VOID),
        town_village_2("newbeginning.town.village_2", a("strategy_and_gameplay", "Spellforce - The Order of Dawn", 129), THE_VOID),
        town_village_3("newbeginning.town.village_3", a("Village", "Spellforce 2 - Demons of the Past", 70), THE_VOID),
        town_village_5("newbeginning.town.village_5", a("sf2_location_lyraine", "Spellforce 2 - Shadow Wars", 96), THE_VOID),
        town_village_6("newbeginning.town.village_6", a("sf2_location_sevencastles", "Spellforce 2 - Shadow Wars", 83), THE_VOID),
        town_village_8("newbeginning.town.village_8", a("First released music", "Spellforce 3", 183), THE_VOID),
        town_ville_1("newbeginning.town.ville_1", a("Loop_4", "Spellforce 2 - Faith in Destiny", 160), THE_VOID);

        private String path;
        private Description description;
        private Biome[] adaptedBiomes;

        List(String path, Description description, Biome... biomes) {
            this . path = path;
            this . description = description;
            this . adaptedBiomes = biomes; // for global zone (note: void == adapted to every biomes)
        }

        public boolean isAdaptedTo(Biome biome) {
            for (Biome b : adaptedBiomes)
                if (b.equals(THE_VOID) || b.equals(biome))
                    return true;
            return false;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public long getDuration() {
            return description.duration;
        }

        @Override
        public Description getDescription() {
            return description;
        }

        public static Music getMusic(String path) {
            for (List l : values())
                if (l.path.equals(path))
                    return l;
            return null;
        }

        private static Description a(String name, String author, long duration) {
            return new Description(name, author, duration);
        }

        private static Description a(String name, String author, String album, long duration) {
            return new Description(name, author, album, duration);
        }
    }
}
