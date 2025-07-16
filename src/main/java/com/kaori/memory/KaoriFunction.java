package com.kaori.memory;

import java.util.List;

import com.kaori.ast.StatementAST;

public abstract class KaoriFunction {

    public static class Function<T> extends KaoriFunction {
        public final List<StatementAST.Variable> parameters;
        public final StatementAST.Block block;
        public final Environment<T> environment;

        public Function(List<StatementAST.Variable> parameters, StatementAST.Block block,
                Environment<T> environment) {
            this.parameters = parameters;
            this.block = block;
            this.environment = new Environment<>();

        }
    }

    public static class Main<T> extends KaoriFunction {
        public final Environment<T> environment;

        public Main(Environment<T> environment) {
            this.environment = environment;
        }
    }
}
