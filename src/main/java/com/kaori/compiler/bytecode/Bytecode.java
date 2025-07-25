package com.kaori.compiler.bytecode;

import java.util.ArrayList;
import java.util.List;

public record Bytecode(List<Instruction> instructions) {
    @Override
    public String toString() {
        List<String> bytecode = new ArrayList<>();

        for (Instruction instruction : this.instructions) {
            bytecode.add(instruction.toString());
        }

        return String.join("\n", bytecode);
    }
}
