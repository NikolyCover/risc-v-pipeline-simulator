package br.unioeste.oac.risc_v_pipeline_simulator;

import java.util.HashMap;
import java.util.Map;

// NOP -> null

class Simulator {
    private int[] registers = new int[32];
    private Map<Integer, Integer> memory = new HashMap<>();
    private int PC = 0;

    // registradores do pipeline
    private Instruction IFIDIR = null, IDEXIR = null, EXMEMIR = null, MEMWBIR = null;
    private int IDEXA, IDEXB, EXMEMB, EXMEMALUOut, MEMWBValue;

    // Registradores para armazenar os índices das instruções (somente para facilitar o entendimento)
    private int IFIDIndex = -1, IDEXIndex = -1, EXMEMIndex = -1, MEMWBIndex = -1;

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

            printCycleState();
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
            IFIDIndex = PC / 4;
            printStage("Buscando", IFIDIR, IFIDIndex);
        } else {
            IFIDIR = null;
        }
        PC += 4;
    }

    private void instructionDecode() {
        IDEXIR = IFIDIR;
        IDEXIndex = IFIDIndex;

        if (IDEXIR != null) {
            printStage("Decodificando", IDEXIR, IDEXIndex);
            IDEXA = registers[IDEXIR.rs1];
            IDEXB = registers[IDEXIR.rs2];
        }
    }

    private void execute() {
        EXMEMIR = IDEXIR;
        EXMEMB = IDEXB;
        EXMEMIndex = IDEXIndex;

        if (EXMEMIR != null) {
            printStage("Executando", EXMEMIR, EXMEMIndex);

            switch (EXMEMIR.type) {
                case R:
                    if (Instruction.Operation.ADD == EXMEMIR.operation) {
                        EXMEMALUOut = IDEXA + IDEXB;
                    } else if (Instruction.Operation.SUB == (EXMEMIR.operation)) {
                        EXMEMALUOut = IDEXA - IDEXB;
                    }
                    break;

                case I:
                    if (Instruction.Operation.LW ==(EXMEMIR.operation)) {
                        EXMEMALUOut = IDEXA + EXMEMIR.immediate;
                    }
                    break;

                case S:
                    if (Instruction.Operation.SW == (EXMEMIR.operation)) {
                        EXMEMALUOut = IDEXA + EXMEMIR.immediate;
                    }
                    break;
            }
        }
    }

    private void memoryAccess() {
        MEMWBIR = EXMEMIR;
        MEMWBIndex = EXMEMIndex;

        if (MEMWBIR != null) {
            printStage("Acessando memória", MEMWBIR, MEMWBIndex);

            if (MEMWBIR.type == Instruction.Type.I && Instruction.Operation.LW == (MEMWBIR.operation)) {
                MEMWBValue = memory.getOrDefault(EXMEMALUOut, 0);
            } else if (MEMWBIR.type == Instruction.Type.S && Instruction.Operation.SW == (MEMWBIR.operation)) {
                memory.put(EXMEMALUOut, EXMEMB);
            } else {
                MEMWBValue = EXMEMALUOut;
            }
        }
    }

    private void writeBack() {
        if (MEMWBIR != null) {

            printStage("Escrevendo", MEMWBIR, MEMWBIndex);

            if (MEMWBIR.type == Instruction.Type.R || MEMWBIR.type == Instruction.Type.I) {
                registers[MEMWBIR.rd] = MEMWBValue;
            }
        }
    }

    private void printCycleHeader(int cycle) {
        System.out.println("CICLO " + (cycle + 1) + ":\n");
    }

    private void printCycleState() {
        System.out.println("PC:" + PC);
        System.out.println("\nRegistradores:");
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

    private void printStage(String stage, Instruction instruction, int instructionIndex) {
        System.out.println(stage + " instrução " + (instructionIndex + 1) + " (" + instruction.operation + ")" + "\n");
    }
}
