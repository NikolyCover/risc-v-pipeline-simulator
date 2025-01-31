package br.unioeste.oac.risc_v_pipeline_simulator;

class Instruction {
    enum Type { R, I, S }

    Type type;
    String operation;
    int rd, rs1, rs2, immediate;

    Instruction(Type type, String operation, int rd, int rs1, int rs2, int immediate) {
        this.type = type;
        this.operation = operation;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.immediate = immediate;
    }
}
