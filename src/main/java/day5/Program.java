package day5;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Program {

    private static final int ABSOLUTE_PARAMETER_MODE = 1;
    private static final int POSITIONAL_PARAMETER_MODE = 0;
    private static final int RELATIVE_PARAMETER_MODE = 2;

    private static final int EXTRA_MEMORY = 2048;

    enum State {NOT_STARTED, RUNNING, AWAITING_INPUT, TERMINATED}

    // visible for testing
    long[] memory;
    private List<Long> inputs = new LinkedList<>();
    private List<Long> outputs = new LinkedList<>();
    private State state = State.NOT_STARTED;
    private int current = 0;
    private int relativeOffset = 0;

    private Program(long[] program) {
        this.memory = new long[program.length + EXTRA_MEMORY];
        System.arraycopy(program, 0, this.memory, 0, program.length);
    }

    interface OperationHandler {
        void handle();
    }

    private Map<Integer, OperationHandler> operationsByCode = Map.of(
            1, binaryOperationHandler(Long::sum),
            2, binaryOperationHandler((x, y) -> x * y),
            3, this::handleInput,
            4, this::handleOutput,
            5, conditionalOperation(i -> i != 0),
            6, conditionalOperation(i -> i == 0),
            7, binaryOperationHandler((x, y) -> (x < y) ? 1L : 0L),
            8, binaryOperationHandler((x, y) -> (x.equals(y)) ? 1L : 0L),
            9, this::handleRelativeOffset,
            99, this::handleTerminate
    );

    public static Program initialize(int[] programCode) {
        return new Program(convertToArrayOfLongs(programCode));
    }

    static Program initialize(long[] programCode) {
        return new Program(programCode);
    }

    public List<Long> getOutputs() {
        return List.copyOf(this.outputs);
    }

    public Program run(long... inputs) {
        if (this.state == State.TERMINATED) throw new IllegalStateException("The program already terminated");
        this.state = State.RUNNING;
        this.inputs.addAll(asList(inputs));
        while (this.state == State.RUNNING) {
            int operationCode = (int) memory[this.current] % 100;
            resolveOperationByCode(operationCode).handle();
        }
        return this;
    }

    private OperationHandler resolveOperationByCode(int operationCode) {
        OperationHandler operationHandler = operationsByCode.get(operationCode);
        if (operationHandler == null)
            throw new IllegalArgumentException("Unknown operation " + operationCode + " at " + this.current);
        return operationHandler;
    }

    private int parameterModes() {
        return (int) memory[this.current] / 100;
    }

    public boolean isComplete() {
        return this.state == State.TERMINATED;
    }

    public long getLatestOutput() {
        if (outputs.isEmpty()) throw new IllegalArgumentException("There are no outputs");
        return outputs.get(outputs.size() - 1);
    }

    private OperationHandler binaryOperationHandler(BiFunction<Long, Long, Long> operation) {
        return () -> {
            long left = extractParameter(0);
            long right = extractParameter(1);
            int target = extractTargetAddressParameter(2);
            this.memory[target] = operation.apply(left, right);
            this.current += 4;
        };
    }

    private OperationHandler conditionalOperation(Predicate<Long> condition) {
        return () -> {
            long parameter = extractParameter(0);
            this.current = condition.test(parameter)
                    ? (int) extractParameter(1)
                    : this.current + 3;
        };
    }

    private void handleInput() {
        if (this.inputs.isEmpty()) {
            this.state = State.AWAITING_INPUT;
        } else {
            int targetPosition = extractTargetAddressParameter(0);
            memory[targetPosition] = this.inputs.remove(0);
            this.current += 2;
        }
    }

    private void handleOutput() {
        this.outputs.add(extractParameter(0));
        this.current += 2;
    }

    private void handleTerminate() {
        this.state = State.TERMINATED;
    }

    private void handleRelativeOffset() {
        this.relativeOffset += (int) extractParameter(0);
        this.current += 2;
    }

    private long extractParameter(int parameterIndex) {
        int parameterMode = getParameterMode(parameterModes(), parameterIndex);
        return getParameterValue(this.current + 1 + parameterIndex, parameterMode);
    }

    private int extractTargetAddressParameter(int parameterIndex) {
        int mode = getParameterMode(parameterModes(), parameterIndex);
        switch (mode) {
            case POSITIONAL_PARAMETER_MODE:
                return (int) this.memory[this.current + 1 + parameterIndex];
            case RELATIVE_PARAMETER_MODE:
                return (int) this.memory[this.current + 1 + parameterIndex] + this.relativeOffset;
            case ABSOLUTE_PARAMETER_MODE:
                throw new IllegalArgumentException("Absolute parameter mode is not supported for target address");
            default:
                throw new IllegalArgumentException("Invalid parameter mode " + mode);
        }
    }

    private long getParameterValue(int address, int mode) {
        switch (mode) {
            case POSITIONAL_PARAMETER_MODE:
                return this.memory[(int) this.memory[address]];
            case RELATIVE_PARAMETER_MODE:
                int relativeAddress = (int) this.memory[address];
                return this.memory[relativeAddress + this.relativeOffset];
            case ABSOLUTE_PARAMETER_MODE:
                return this.memory[address];
            default:
                throw new IllegalArgumentException("Invalid parameter mode " + mode);
        }
    }

    private static int getParameterMode(int parameterModes, int parameterIndex) {
        // we get n-th digit from the end from modes integer
        return parameterModes / (int) Math.pow(10, parameterIndex) % 10;
    }

    private static List<Long> asList(long[] inputs) {
        return Arrays.stream(inputs).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

    private static long[] convertToArrayOfLongs(int[] program) {
        long[] programAsLong = new long[program.length];
        for (int i = 0; i < program.length; i++) {
            programAsLong[i] = program[i];
        }
        return programAsLong;
    }

}
