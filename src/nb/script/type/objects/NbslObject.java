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
package nb.script.type.objects;

public abstract class NbslObject {

    public abstract Object onOperator(OperatorFunction function, Object obj);
    public abstract OperatorFunction[] getSupportedOperators();

    public enum OperatorFunction {
        Plus("+"), PlusEquals("+="),
        Minus("-"), MinusEquals("-="),
        Assign("="), Equals("=="), Unequals("!="), Reverse("!"),
        BitNot("~"), BitOr("|"), BitAnd("&"), BitXor("^"), BitNor("!|"), BitNand("!&"),
        BitNotEquals("~="), BitOrEquals("|="), BitAndEquals("&="), BitXorEquals("^="),
        BitNorEquals("!|="), BitNandEquals("!&=");

        private final String sequence;
        OperatorFunction(final String sequence) {
            this.sequence = sequence;
        }

        public String getSequence() {
            return sequence;
        }

        public static OperatorFunction getOperator(String sequence) {
            for (OperatorFunction operator : values()) {
                if (operator.sequence.equals(sequence))
                    return operator;
            }

            return null;
        }
    }
}
