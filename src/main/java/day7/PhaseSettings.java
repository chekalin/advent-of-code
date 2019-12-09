package day7;

import day5.Program;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

class PhaseSettings {

    static long calculateThrusterOutput(int[] program, List<Integer> phaseSettings) {
        long latestOutput = 0;
        for (int phaseSetting : phaseSettings) {
            latestOutput = Program.initialize(program).run(phaseSetting, latestOutput).getLatestOutput();
        }
        return latestOutput;
    }

    static long calculateMaxThrusterOutput(int[] program) {
        return combinations(5).stream()
                .mapToLong(phaseSettings -> calculateThrusterOutput(program, phaseSettings))
                .max().orElseThrow();
    }

    static List<List<Integer>> combinations(int length) {
        return combinations(length, 0);
    }

    static List<List<Integer>> combinations(int length, int start) {
        List<List<Integer>> result = new LinkedList<>();
        combinations(result, length, start, new LinkedList<>());
        return result;
    }

    private static void combinations(List<List<Integer>> result, int length, int start, List<Integer> prefix) {
        if (prefix.size() == length) {
            result.add(prefix);
        } else {
            IntStream.range(start, start + length).filter(i -> !prefix.contains(i))
                    .forEach(i -> {
                        LinkedList<Integer> newPrefix = new LinkedList<>(prefix);
                        newPrefix.add(i);
                        combinations(result, length, start, newPrefix);
                    });
        }
    }

    static long calculateFeedbackThrusterOutput(int[] programCode, List<Integer> phaseSettings) {
        List<Program> amplifiers = new LinkedList<>();
        long latestOutput = 0;
        for (Integer phaseSetting : phaseSettings) {
            Program amplifier = Program.initialize(programCode);
            amplifiers.add(amplifier);
            latestOutput = amplifier.run(phaseSetting, latestOutput).getLatestOutput();
        }
        while (!amplifiers.stream().allMatch(Program::isComplete)) {
            for (Program amplifier : amplifiers) {
                latestOutput = amplifier.run(latestOutput).getLatestOutput();
            }
        }
        return latestOutput;
    }

    static long calculateFeedbackThrusterMaxOutput(int[] program) {
        return combinations(5, 5).stream()
                .mapToLong(phaseSettings -> calculateFeedbackThrusterOutput(program, phaseSettings))
                .max().orElseThrow();
    }
}
