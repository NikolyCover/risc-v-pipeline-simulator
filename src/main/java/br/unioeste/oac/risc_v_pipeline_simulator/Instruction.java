package br.unioeste.oac.risc_v_pipeline_simulator;

class Instruction {
    enum Type { R, I, S }
    enum Operation { SW, LW, ADD, SUB }

    Type type;
    Operation operation;
    int rd, rs1, rs2, immediate;

    private Instruction(Operation operation, int rd, int rs1, int rs2, int immediate, Type type) {
        this.operation = operation;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.immediate = immediate;
        this.type = type;
    }

    // add rd, rs1, rs2  ->  rd = rs1 + rs2
    static Instruction add(int rd, int rs1, int rs2) {
        return new Instruction(Operation.ADD, rd, rs1, rs2, 0, Type.R);
    }

    // sub rd, rs1, rs2  ->  rd = rs1 - rs2
    static Instruction sub(int rd, int rs1, int rs2) {
        return new Instruction(Operation.SUB, rd, rs1, rs2, 0, Type.R);
    }

    // lw rd, immediate(rs1)  ->  rd = Mem[rs1 + immediate]
    static Instruction lw(int rd, int immediate, int rs1) {
        return new Instruction(Operation.LW, rd, rs1, 0, immediate, Type.I);
    }

    // sw rs2, immediate(rs1)  -> Mem[rs1 + immediate] = rs2
    static Instruction sw(int rs2, int immediate, int rs1) {
        return new Instruction(Operation.SW, 0, rs1, rs2, immediate, Type.S);
    }
}
