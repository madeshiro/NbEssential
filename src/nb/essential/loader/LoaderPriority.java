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

package nb.essential.loader;

/**
 * Represents a PluginData's priotiy during loading.
 * <p>
 *     <h5><u>Note</u></h5>
 *     More the priority is high, more the data is likely to be loaded in last.
 * </p>
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
public enum LoaderPriority {

    /**
     * The lowest {@link LoaderPriority}. The data will be loaded in first
     */
    Lowest(0),
    Low(1),
    Normal(2),
    High(3),
    Highest(4),
    /**
     * The (very) Highest {@link LoaderPriority}. The data will be loaded in last.
     */
    Critical(5);

    LoaderPriority(int value) {
        this . priorityValue = value;
    }

    int priorityValue;

    /**
     * Gets the number value of the priority (0 (lowest) -> 5 (Critical))
     * @return The value assigned to the enum constant
     */
    int getPriorityValue() {
        return this.priorityValue;
    }

    /**
     * Tests if this enum value is higher than another.
     * @param priority The enum constant to compare with.
     * @return {@code True} if this > priority, else false.
     */
    public boolean isHigherThan(LoaderPriority priority) {
        return priorityValue > priority.priorityValue;
    }

    /**
     * Tests if this enum value is smaller than another.
     * @param priority The enum constant to compare with.
     * @return {@code True} if this < priority, else false.
     */
    public boolean isSmallerThan(LoaderPriority priority) {
        return priorityValue < priority.priorityValue;
    }
}
