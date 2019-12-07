package day5;

import day5.Program.Operation.OperationResult;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Program {

    private static final int PARAMETER_MODE_BY_VALUE = 1;
    private static final int PARAMETER_MODE_BY_REFERENCE = 0;

    private int[] program;
    private List<Integer> inputs = new LinkedList<>();
    private List<Integer> outputs = new LinkedList<>();
    private State state = State.NOT_STARTED;
    private int current = 0;

    enum State {NOT_STARTED, RUNNING, AWAITING_INPUT, TERMINATED}

    public Program(int[] program) {
        this.program = program;
    }

    public void run(int... inputs) {
        if (this.state == State.TERMINATED) throw new IllegalStateException("The program already terminated");
        this.state = State.RUNNING;
        this.inputs.addAll(asList(inputs));
        while (this.state == State.RUNNING) {
            int operationCode = program[this.current] % 100;
            int parameterModes = program[this.current] / 100;
            Operation operation = Operation.BY_CODE.get(operationCode);
            if (operation == null)
                throw new IllegalArgumentException("Unknown operation " + operationCode + " at " + this.current);
            OperationResult result = operation.process(this.program, this.current, this.inputs, this.outputs, parameterModes);
            this.current = result.nextPosition;
            this.state = result.nextState;
        }
    }

    public boolean isComplete() {
        return this.state == State.TERMINATED;
    }

    public int getLatestOutput() {
        if (outputs.isEmpty()) throw new IllegalArgumentException("There are no outputs");
        return outputs.get(outputs.size() - 1);
    }

    public static int[] process(int[] programCode, int... inputs) {
        Program program = new Program(programCode);
        program.run(inputs);
        return asArray(program.outputs);
    }

    private static LinkedList<Integer> asList(int[] inputs) {
        return Arrays.stream(inputs).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

    private static int[] asArray(List<Integer> output) {
        return output.stream().mapToInt(i -> i).toArray();
    }


    interface Operation {
        OperationResult process(int[] program, int currentPosition, List<Integer> input, List<Integer> output, int parameterModes);

        Map<Integer, Operation> BY_CODE = Map.of(
                1, binaryOperation(Integer::sum),
                2, binaryOperation((x, y) -> x * y),
                3, inputOperation(),
                4, outputOperation(),
                5, conditionalOperation(i -> i != 0),
                6, conditionalOperation(i -> i == 0),
                7, binaryOperation((x, y) -> (x < y) ? 1 : 0),
                8, binaryOperation((x, y) -> (x.equals(y)) ? 1 : 0),
                99, (program1, currentPosition, input, output, parameterModes) -> terminate()
        );

        private static Operation binaryOperation(BiFunction<Integer, Integer, Integer> operation) {
            return (program, currentPosition, input, output, parameterModes) -> {
                int left = extractParameter(program, currentPosition, 0, parameterModes);
                int right = extractParameter(program, currentPosition, 1, parameterModes);
                int target = extractTargetAddressParameter(program, currentPosition, 2);
                program[target] = operation.apply(left, right);
                return continueAt(currentPosition + 4);
            };
        }

        private static Operation conditionalOperation(Predicate<Integer> condition) {
            return (program, currentPosition, input, output, parameterModes) -> {
                int parameter = extractParameter(program, currentPosition, 0, parameterModes);
                if (condition.test(parameter)) {
                    return continueAt(extractParameter(program, currentPosition, 1, parameterModes));
                } else {
                    return continueAt(currentPosition + 3);
                }
            };
        }

        private static Operation inputOperation() {
            return (program, currentPosition, input, output, parameterModes) -> {
                if (input.isEmpty()) {
                    return awaitInput(currentPosition);
                }
                int targetPosition = extractTargetAddressParameter(program, currentPosition, 0);
                program[targetPosition] = input.remove(0);
                return continueAt(currentPosition + 2);
            };
        }

        private static Operation outputOperation() {
            return (program, currentPosition, input, output, parameterModes) -> {
                output.add(extractParameter(program, currentPosition, 0, parameterModes));
                return continueAt(currentPosition + 2);
            };
        }

        private static int extractParameter(int[] program, int programIndex, int parameterIndex, int parameterModes) {
            int parameterMode = getParameterMode(parameterModes, parameterIndex);
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

        private static int getParameterMode(int parameterModes, int parameterIndex) {
            // we get n-th digit from the end from modes integer
            return parameterModes / (int) Math.pow(10, parameterIndex) % 10;
        }

        static OperationResult continueAt(int nextPosition) {
            return new OperationResult(State.RUNNING, nextPosition);
        }

        static OperationResult awaitInput(int nextPosition) {
            return new OperationResult(State.AWAITING_INPUT, nextPosition);
        }

        static OperationResult terminate() {
            return new OperationResult(State.TERMINATED, -1);
        }

        class OperationResult {
            private State nextState;
            private int nextPosition;

            private OperationResult(State nextState, int nextPosition) {
                this.nextState = nextState;
                this.nextPosition = nextPosition;
            }
        }

        class NotEnoughInputsException extends RuntimeException {
            NotEnoughInputsException() {
                super("Not enough inputs");
            }
        }
    }

}
