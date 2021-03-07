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

package nb.essential.scheduler;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 28/08/2016
 *
 * @apiNote This interface was made to resolve a problem arriving regularly.
 * The problem was as follows: often, it was necessary to cancel the task in its
 * runnable however, he was difficult to access the {@code taskID}. Now, this one
 * will be always sended as parameter in this new {@code runnable}.
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
@FunctionalInterface
public interface IScheduleRunnable {

    /**
     *
     * @param taskId the task id
     */
    void run(int taskId);
}
