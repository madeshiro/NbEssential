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
import org.bukkit.metadata.FixedMetadataValue;
/**
 * Class of NbEssential
 */
public class MetadataValue extends FixedMetadataValue {

    private final boolean savable;
    private final Class<?> originalType;

    public <T> MetadataValue(T value) {
        super(NbEssential.getPlugin(), value);

        this . savable = true;
        this . originalType = value.getClass();
    }

    public <T> MetadataValue(T value, boolean save) {
        super(NbEssential.getPlugin(), value);

        this . savable = save;
        this . originalType = value.getClass();
    }

    public Class<?> getOriginalType() {
        return originalType;
    }

    public boolean isSavable() {
        return savable;
    }

    protected Object saveValue() {
        return this.value();
    }
}
