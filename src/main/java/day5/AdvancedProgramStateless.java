package day5;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class AdvancedProgramStateless {

    private static final int PARAMETER_MODE_BY_VALUE = 1;
    private static final int PARAMETER_MODE_BY_REFERENCE = 0;

    static int[] process(int[] program, int... inputs) {
        int current = 0;
        List<Integer> input = asList(inputs);
        List<Integer> output = new LinkedList<>();
        while (current != -1) {
            int operationCode = program[current] % 100;
            int parameterModes = program[current] / 100;
            Operation operation = Operation.BY_CODE.get(operationCode);
            if (operation == null)
                throw new IllegalArgumentException("Unknown operation " + operationCode + " at " + current);
            current = operation.process(program, current, input, output, parameterModes);
        }
        return output.stream().mapToInt(i -> i).toArray();
    }

    private static LinkedList<Integer> asList(int[] inputs) {
        return Arrays.stream(inputs).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

    private static int extractParameter(int[] program, int programIndex, int parameterIndex, int parameterModes) {
        int parameterMode = getNthDigitFromTheEnd(parameterModes, parameterIndex);
        return getParameterValue(program, programIndex + 1 + parameterIndex, parameterMode);
    }

    private static int extractTargetAddressParameter(int[] program, int programIndex, int parameterIndex) {
        // Target address value is never a reference
        return getParameterValue(program, programIndex + 1 + parameterIndex, PARAMETER_MODE_BY_VALUE);
    }

    private static int getParameterValue(int[] program, int address, int mode) {
        if (mode != PARAMETER_MODE_BY_REFERENCE && mode != PARAMETER_MODE_BY_VALUE)
            throw new IllegalArgumentException("Invalid parameter mode " + mode);
        return (mode == PARAMETER_MODE_BY_REFERENCE) ? program[program[address]] : program[address];
    }

    private static int getNthDigitFromTheEnd(int number, int positionFromTheEnd) {
        return number / (int) Math.pow(10, positionFromTheEnd) % 10;
    }

    interface Operation {
        int process(int[] program, int currentPosition, List<Integer> input, List<Integer> output, int parameterModes);

        Map<Integer, Operation> BY_CODE = Map.of(
                1, binaryOperation(Integer::sum),
                2, binaryOperation((x, y) -> x * y),
                3, inputOperation(),
                4, outputOperation(),
                5, conditionalOperation(i -> i != 0),
                6, conditionalOperation(i -> i == 0),
                7, binaryOperation((x, y) -> (x < y) ? 1 : 0),
                8, binaryOperation((x, y) -> (x.equals(y)) ? 1 : 0),
                99, terminateOperation()
        );

        private static Operation binaryOperation(BiFunction<Integer, Integer, Integer> operation) {
            return (program, currentPosition, input, output, parameterModes) -> {
                int left = extractParameter(program, currentPosition, 0, parameterModes);
                int right = extractParameter(program, currentPosition, 1, parameterModes);
                int target = extractTargetAddressParameter(program, currentPosition, 2);
                program[target] = operation.apply(left, right);
                return currentPosition + 4;
            };
        }

        private static Operation conditionalOperation(Predicate<Integer> condition) {
            return (program, currentPosition, input, output, parameterModes) -> {
                int parameter = extractParameter(program, currentPosition, 0, parameterModes);
                if (condition.test(parameter)) {
                    return extractParameter(program, currentPosition, 1, parameterModes);
                } else {
                    return currentPosition + 3;
                }
            };
        }

        private static Operation inputOperation() {
            return (program, currentPosition, input, output, parameterModes) -> {
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Not enough inputs for program");
                }
                int targetPosition = extractTargetAddressParameter(program, currentPosition, 0);
                program[targetPosition] = input.remove(0);
                return currentPosition + 2;
            };
        }

        private static Operation outputOperation() {
            return (program, currentPosition, input, output, parameterModes) -> {
                output.add(extractParameter(program, currentPosition, 0, parameterModes));
                return currentPosition + 2;
            };
        }

        static Operation terminateOperation() {
            return (program, currentPosition, input, output, parameterModes) -> -1;
        }
    }

}
