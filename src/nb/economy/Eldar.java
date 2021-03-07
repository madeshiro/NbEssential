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

package nb.economy;


/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 17/09/2016
 */
public class Eldar {

    private Double amount;

    public Eldar() {
        this.amount = 0d;
    }

    public Eldar(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public int E() {
        return amount.intValue();
    }

    public int cents() {
        return (int) ((amount - E()) * 100);
    }

    public void add(double amount) {
        this.amount += amount;
    }

    public void substract(double amount) {
        this.amount -= amount;
    }
}
