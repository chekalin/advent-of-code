package day5;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static day5.AdvancedProgram.Operation.*;

class AdvancedProgram {

    private static final Map<Operation, BiFunction<Integer, Integer, Integer>> BINARY_OPERATORS = Map.of(
            Sum, Integer::sum,
            Multiply, (x, y) -> x * y,
            LessThan, (x, y) -> (x < y) ? 1 : 0,
            Equals, (x, y) -> (x.equals(y)) ? 1 : 0
    );

    private static final Map<Operation, Predicate<Integer>> CONDITIONAL_OPERATORS = Map.of(
            JumpIfTrue, i -> i != 0,
            JumpIfFalse, i -> i == 0
    );

    private static final int PARAMETER_MODE_BY_VALUE = 1;
    private static final int PARAMETER_MODE_BY_REFERENCE = 0;

    private int[] program;
    private List<Integer> inputs;
    private int current = 0;

    private AdvancedProgram(int[] program, int[] inputs) {
        this.program = program;
        this.inputs = Arrays.stream(inputs).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

    static int[] process(int[] program, int... inputs) {
        return new AdvancedProgram(program, inputs).run();
    }

    private int[] run() {
        List<Integer> output = new ArrayList<>();
        while (current != -1) {
            int opCode = program[current];
            Operation operation = fromCode(opCode % 100);
            int parameterModes = opCode / 100;
            switch (operation) {
                case Sum:
                case Multiply:
                case LessThan:
                case Equals:
                    BiFunction<Integer, Integer, Integer> operator = BINARY_OPERATORS.get(operation);
                    int left = extractParameter(0, parameterModes);
                    int right = extractParameter(1, parameterModes);
                    int target = extractTargetAddressParameter(2);
                    program[target] = operator.apply(left, right);
                    current += 4;
                    break;
                case JumpIfTrue:
                case JumpIfFalse:
                    Predicate<Integer> condition = CONDITIONAL_OPERATORS.get(operation);
                    int parameter = extractParameter(0, parameterModes);
                    if (condition.test(parameter)) {
                        current = extractParameter(1, parameterModes);
                    } else {
                        current += 3;
                    }
                    break;
                case Input:
                    processInput();
                    break;
                case Output:
                    processOutput(output, parameterModes);
                    break;
                case Terminate:
                    current = -1;
                    break;
                default:
                    throw new IllegalArgumentException("Operation " + operation + " is not implemented");
            }
        }
        return output.stream().mapToInt(i -> i).toArray();
    }

    private int extractTargetAddressParameter(int index) {
        // Target address value is never a reference
        return getOperandValue(current + 1 + index, PARAMETER_MODE_BY_VALUE);
    }

    private int extractParameter(int index, int parameterModes) {
        int parameterMode = getNthDigitFromTheEnd(parameterModes, index);
        return getOperandValue(current + 1 + index, parameterMode);
    }

    static int getNthDigitFromTheEnd(int number, int positionFromTheEnd) {
        return number / (int) Math.pow(10, positionFromTheEnd) % 10;
    }

    private int getOperandValue(int address, int mode) {
        if (mode != PARAMETER_MODE_BY_REFERENCE && mode != PARAMETER_MODE_BY_VALUE)
            throw new IllegalArgumentException("Invalid operand mode " + mode + "for operand in operation " + current);
        return (mode == PARAMETER_MODE_BY_REFERENCE) ? program[program[address]] : program[address];
    }

    private void processOutput(List<Integer> output, int parameterModes) {
        output.add(extractParameter(0, parameterModes));
        current += 2;
    }

    private void processInput() {
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException("Not enough inputs for program");
        }
        int targetPosition = extractTargetAddressParameter(0);
        program[targetPosition] = inputs.remove(0);
        current += 2;
    }

    enum Operation {
        Sum(1),
        Multiply(2),
        Input(3),
        Output(4),
        JumpIfTrue(5),
        JumpIfFalse(6),
        LessThan(7),
        Equals(8),
        Terminate(99),
        ;

        public int code;

        Operation(int code) {
            this.code = code;
        }

        static Operation fromCode(int code) {
            return Arrays.stream(values())
                    .filter(op -> op.code == code)
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown operator " + code));
        }
    }

}
