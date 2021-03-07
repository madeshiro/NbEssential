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
package nb.script.type;

import nb.script.exception.ScriptException;
import nb.script.parser.ParseInfo;
import nb.script.type.interfaces.Conditionalizable;
import nb.script.type.interfaces.Copyable;
import nb.script.type.interfaces.RuntimeCondition;

/**
 * Class of NbEssential in package nb.script.type
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class Conditions extends CodeChunk {

    private Condition condition;
    private Step actions, nactions;

    private boolean value, alreadyRun = false;

    public Conditions(ParseInfo info) {
        super(info);
    }

    protected Conditions(Conditions chunk, CodeChunk parent) {
        super(chunk, parent);

        this.condition = chunk.condition;
        this.actions = (Step) chunk.actions.copy(parent);
        this.nactions = (Step) chunk.nactions.copy(parent);
    }

    @Override
    public void run() throws ScriptException {

        if (!alreadyRun) {
            condition.run(parent);
            value = condition.returnv;
        }
        alreadyRun = true;

        if (value && actions != null)
            actions.run();
        else if (!value && nactions != null)
            nactions.run();
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setActions(Step actions) {
        this . actions = actions;
    }

    public void setNactions(Step nactions) {
        this.nactions = nactions;
    }

    public void interrupt() throws ScriptException {
        actions.interrupt();
        nactions.interrupt();
    }

    /**
     * Defines which CodeChunk is allowed to be declared in this chunk.
     *
     * @return An array of allowed {@link CodeChunk} classes.
     * @see #isAllow(CodeChunk)
     */
    @Override
    protected Class<? extends CodeChunk>[] allowCodeChunks() {
        return new Class[0];
    }

    @Override
    public void addAtTag(MetaInstruction atTag) {
        if (atTag.isCompatibleWith(this)) {
            atTags.add(atTag);

            switch (atTag) {
                case Sync:
                    atTags.remove(MetaInstruction.Async);
                    break;
                case Async:
                    atTags.remove(MetaInstruction.Sync);
                    break;
            }
        }
    }

    @Override
    public Copyable copy(CodeChunk parent) {
        return new Conditions(this, parent);
    }

    public static class Condition implements Conditionalizable {
        private Conditionalizable cond1, cond2;
        private Operator operator = Operator.Single;

        private final int line;
        private final String fileName, scriptName;

        private boolean returnv;

        public Condition(ParseInfo info) {
            line = info.getLine();
            fileName = info.getFileName();
            scriptName = info.getRoot().getName();

            cond2 = new Conditionalizable() {
                @Override
                public Object getReturnValue() {
                    return true;
                }

                @Override
                public int getLine() {
                    return info.getLine();
                }

                @Override
                public String getFileName() {
                    return info.getFileName();
                }

                @Override
                public String getScriptName() {
                    return info.getRoot().getName();
                }

                @Override
                public void run(CodeChunk parent) {
                }
            };
        }

        @Override
        public Object getReturnValue() {
            return returnv;
        }

        /**
         * Gets the declaration line of this instruction
         *
         * @return the declaration file line.
         */
        @Override
        public int getLine() {
            return line;
        }

        /**
         * Gets the declaration file name.
         *
         * @return the declaration file name.
         */
        @Override
        public String getFileName() {
            return fileName;
        }

        /**
         * Gets the declaration script name.
         *
         * @return the declaration script name.
         */
        @Override
        public String getScriptName() {
            return scriptName;
        }

        /**
         * Runs the statement by specifying the parent containing the statement.
         *
         * @param parent The statement parent
         * @throws ScriptException If an exception occured while running the instruction.
         */
        @Override
        public void run(CodeChunk parent) throws ScriptException {
            try {
                returnv = operator.a((boolean) cond1.getReturnValue(), (boolean) cond2.getReturnValue());
            } catch (ClassCastException e) {
                throw new ScriptException(parent.getRoot(), e.getMessage(), e.getCause());
            }
        }

        public void setCondition1(Conditionalizable cond1) {
            this.cond1 = cond1;
        }

        public void setCondition2(Conditionalizable cond2) {
            this.cond2 = cond2;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }

        public static Conditionalizable buildConditionalizable(final ParseInfo info, final RuntimeCondition condition) {
            return new Conditionalizable() {
                private boolean returnv;

                @Override
                public Object getReturnValue() {
                    return returnv;
                }

                @Override
                public int getLine() {
                    return info.getLine();
                }

                @Override
                public String getFileName() {
                    return info.getFileName();
                }

                @Override
                public String getScriptName() {
                    return info.getRoot().getName();
                }

                @Override
                public void run(CodeChunk parent) throws ScriptException {
                    returnv = condition.run(parent);
                }
            };
        }

        public enum Operator {
            And, Or, Nor, Xor, Single
            ;

            public boolean a(boolean var1, boolean var2) {
                switch (this) {
                    case And:
                        return var1 && var2;
                    case Or:
                        return var1 || var2;
                    case Nor:
                        return !(var1 || var2);
                    case Xor:
                        return (var1 || var2) && (var1 != var2);
                    case Single:
                        return var1;
                    default:
                        return false;
                }
            }
        }
    }
}
