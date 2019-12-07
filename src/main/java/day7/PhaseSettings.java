package day7;

import day5.Program;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

class PhaseSettings {

    static int calculateThrusterOutput(int[] program, List<Integer> phaseSettings) {
        int latestOutput = 0;
        for (int phaseSetting : phaseSettings) {
            int[] outputs = Program.process(program.clone(), phaseSetting, latestOutput);
            latestOutput = outputs[0];
        }
        return latestOutput;
    }

    static int calculateMaxThrusterOutput(int[] program) {
        return combinations(5).stream()
                .mapToInt(phaseSettings -> calculateThrusterOutput(program, phaseSettings))
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

    static int calculateFeedbackThrusterOutput(int[] programCode, List<Integer> phaseSettings) {
        List<Program> amplifiers = new LinkedList<>();
        int latestOutput = 0;
        for (Integer phaseSetting : phaseSettings) {
            Program amplifier = new Program(programCode.clone());
            amplifier.run(phaseSetting, latestOutput);
            latestOutput = amplifier.getLatestOutput();
            amplifiers.add(amplifier);
        }
        while (!amplifiers.stream().allMatch(Program::isComplete)) {
            for (Program amplifier : amplifiers) {
                amplifier.run(latestOutput);
                latestOutput = amplifier.getLatestOutput();
            }
        }
        return latestOutput;
    }

    static int calculateFeedbackThrusterMaxOutput(int[] program) {
        return combinations(5, 5).stream()
                .mapToInt(phaseSettings -> calculateFeedbackThrusterOutput(program, phaseSettings))
                .max().orElseThrow();
    }
}
