package br.unioeste.oac.risc_v_pipeline_simulator;

public class Main {
    public static void main(String[] args) {
        Simulator simulator = new Simulator();

        Instruction[] instructions = {
                // add x3, x1, x2
                // x3 = x1 + x2
                new Instruction(Instruction.Type.R, "add", 3, 1, 7, 0),

                // sw x3, 8(x1)
                // Mem[x1 + 8] = x3
                new Instruction(Instruction.Type.S, "sw", 0, 1, 3, 8),

                // lw x4, 8(x1)
                // x4 = Mem[x1 + 8]
                new Instruction(Instruction.Type.I, "lw", 4, 1, 0, 8),

                // sub x5, x4, x1
                // x5 = x4 - x1
                new Instruction(Instruction.Type.R, "sub", 5, 4, 1, 0),

                // sw x5, 12(x2)
                // Mem[x2 + 12] = x5
                new Instruction(Instruction.Type.S, "sw", 0, 2, 5, 12)
        };

        simulator.run(instructions);
    }
}
