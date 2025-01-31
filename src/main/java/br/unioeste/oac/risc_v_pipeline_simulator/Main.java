package br.unioeste.oac.risc_v_pipeline_simulator;

public class Main {
    public static void main(String[] args) {
        Simulator simulator = new Simulator();

        Instruction[] instructions = {
                // sub x18, x31, x24
                // x18 = x31 - x24
                // x18 = 31 - 24  ->  x18 = 7
                Instruction.sub(18, 31, 24),

                // add x3, x1, x6
                // x3 = x1 + x6
                // x3 = 1 + 6  ->  x3 = 7
                Instruction.add(3, 1, 6),

                // sw x5, 12(x2)
                // Mem[x2 + 12] = x5
                // Mem[2 + 12] = 5  ->  Mem[14] = 5
                Instruction.sw(5, 12, 2),

                // sub x20, x30, x1
                // x20 = x30 - x1
                // x20 = 30 - 1  ->  x20 = 29
                Instruction.sub( 20, 30, 1),

                // sw x3, 8(x1)
                // Mem[x1 + 8] = x3
                // Mem[1 + 8] = 7  ->  Mem[9] = 7
                Instruction.sw(3, 8, 1),

                // lw x0, 7(x18)
                // x0 = Mem[x18 + 7]
                // x0 = Mem[7 + 7]  ->  x0 = Mem[14]   ->  x0 = 5
                Instruction.lw(0, 7, 18)
        };

        simulator.run(instructions);
    }
}
