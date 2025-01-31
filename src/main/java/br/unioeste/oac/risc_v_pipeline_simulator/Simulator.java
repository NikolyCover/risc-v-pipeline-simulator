package br.unioeste.oac.risc_v_pipeline_simulator;

import java.util.HashMap;
import java.util.Map;

// NOP -> null

class Simulator {
    private int[] registers = new int[32];
    private Map<Integer, Integer> memory = new HashMap<>();
    private int PC = 0;

    private Instruction IFIDIR = null, IDEXIR = null, EXMEMIR = null, MEMWBIR = null;
    private int IDEXA, IDEXB, EXMEMALUOut, MEMWBValue;

    void run(Instruction[] instructions) {
        initPipeline();

        for (int cycle = 0; cycle < instructions.length + 4; cycle++) {
            printCycleHeader(cycle);

            // execução dos 5 estágios do pipeline
            writeBack();
            memoryAccess();
            execute();
            instructionDecode();
            instructionFetch(instructions);

            printState();
        }
    }

    private void initPipeline() {
        for (int i = 0; i < 32; i++) {
            registers[i] = i;
        }
    }

    private void instructionFetch(Instruction[] instructions) {
        if (PC / 4 < instructions.length) {
            IFIDIR = instructions[PC / 4];
            printStage("Buscando", IFIDIR);
        } else {
            IFIDIR = null;
        }
        PC += 4;
    }

    //TODO
    private void instructionDecode() {
        IDEXIR = IFIDIR;

        if (IDEXIR != null) {
            printStage("Decodificando", IDEXIR);
            IDEXA = registers[IDEXIR.rs1];
            IDEXB = registers[IDEXIR.rs2];
        }
    }

    private void execute() {
        EXMEMIR = IDEXIR;

        if (EXMEMIR != null) {
            printStage("Executando", EXMEMIR);

            switch (EXMEMIR.type) {
                case R:
                    if ("add".equals(EXMEMIR.operation)) {
                        EXMEMALUOut = IDEXA + IDEXB;
                    } else if ("sub".equals(EXMEMIR.operation)) {
                        EXMEMALUOut = IDEXA - IDEXB;
                    }
                    break;

                case I:
                    if ("lw".equals(EXMEMIR.operation)) {
                        EXMEMALUOut = IDEXA + EXMEMIR.immediate;
                    }
                    break;

                case S:
                    if ("sw".equals(EXMEMIR.operation)) {
                        EXMEMALUOut = IDEXA + EXMEMIR.immediate;
                    }
                    break;
            }
        }
    }

    private void memoryAccess() {
        MEMWBIR = EXMEMIR;

        if (MEMWBIR != null) {
            printStage("Acessando memória", MEMWBIR);

            if (MEMWBIR.type == Instruction.Type.I && "lw".equals(MEMWBIR.operation)) {
                MEMWBValue = memory.getOrDefault(EXMEMALUOut, 0);
            } else if (MEMWBIR.type == Instruction.Type.S && "sw".equals(MEMWBIR.operation)) {
                memory.put(EXMEMALUOut, IDEXB);
            } else {
                MEMWBValue = EXMEMALUOut;
            }
        }
    }

    private void writeBack() {
        if (MEMWBIR != null) {

            printStage("Escrevendo", MEMWBIR);

            if (MEMWBIR.type == Instruction.Type.R || MEMWBIR.type == Instruction.Type.I) {
                registers[MEMWBIR.rd] = MEMWBValue;
            }
        }
    }

    private void printCycleHeader(int cycle) {
        System.out.println("CICLO " + cycle + ":\n");
    }

    private void printState() {
        System.out.println("Registradores:");
        for (int i = 0; i < 32; i++) {
            System.out.printf("x%-3d", i);
        }
        System.out.println();
        for (int i = 0; i < 32; i++) {
            System.out.printf("%-4d", registers[i]);
        }
        System.out.println("\n\nMemória: " + memory + "\n");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------\n");
    }

    private void printStage(String stage, Instruction instruction) {
        System.out.println(stage + " instrução " + (PC / 4) + " (" + instruction.operation + ")" + "\n");
    }
}
