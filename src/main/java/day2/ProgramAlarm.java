package day2;

import java.util.Map;
import java.util.function.BiFunction;

class ProgramAlarm {

    private static final Map<Integer, BiFunction<Integer, Integer, Integer>> BINARY_OPERATORS = Map.of(
            1, Integer::sum,
            2, (x, y) -> x * y
    );

    static int[] process(int[] program) {
        for (int i = 0; i < program.length; i = i + 4) {
            int opCode = program[i];
            if (BINARY_OPERATORS.containsKey(opCode)) {
                int leftOperand = program[program[i + 1]];
                int rightOperand = program[program[i + 2]];
                int resultPosition = program[i + 3];
                program[resultPosition] = BINARY_OPERATORS.get(opCode).apply(leftOperand, rightOperand);
            } else if (opCode == 99) {
                return program;
            } else {
                throw new IllegalArgumentException("Encountered unknown opcode " + opCode);
            }
        }
        return program;
    }
}
