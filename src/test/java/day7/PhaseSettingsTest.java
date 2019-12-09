package day7;

import org.junit.jupiter.api.Test;

import java.util.List;

import static day7.PhaseSettings.combinations;
import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.readProgramInput;

class PhaseSettingsTest {

    @Test
    void testCase1() {
        int[] program = {3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0};
        long output = PhaseSettings.calculateThrusterOutput(program, List.of(4, 3, 2, 1, 0));
        assertThat(output).isEqualTo(43210);
    }

    @Test
    void testCase1MaxOutput() {
        int[] program = {3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0};
        long output = PhaseSettings.calculateMaxThrusterOutput(program);
        assertThat(output).isEqualTo(43210);
    }

    @Test
    void testCase2() {
        int[] program = {3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23,
                101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0};
        long output = PhaseSettings.calculateThrusterOutput(program, List.of(0, 1, 2, 3, 4));
        assertThat(output).isEqualTo(54321);
    }

    @Test
    void testCase2MaxOutput() {
        int[] program = {3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23,
                101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0};
        long output = PhaseSettings.calculateMaxThrusterOutput(program);
        assertThat(output).isEqualTo(54321);
    }

    @Test
    void testCase3() {
        int[] program = {3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33,
                1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0};
        long output = PhaseSettings.calculateThrusterOutput(program, List.of(1, 0, 4, 3, 2));
        assertThat(output).isEqualTo(65210);
    }

    @Test
    void testCase3MaxOutput() {
        int[] program = {3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33,
                1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0};
        long output = PhaseSettings.calculateMaxThrusterOutput(program);
        assertThat(output).isEqualTo(65210);
    }

    @Test
    void feedbackLoopTestCase1() {
        int[] program = {3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26,
                27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5};
        long output = PhaseSettings.calculateFeedbackThrusterOutput(program, List.of(9, 8, 7, 6, 5));
        assertThat(output).isEqualTo(139629729);
    }

    @Test
    void feedbackLoopTestCase1MaxOutput() {
        int[] program = {3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26,
                27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5};
        long output = PhaseSettings.calculateFeedbackThrusterMaxOutput(program);
        assertThat(output).isEqualTo(139629729);
    }

    @Test
    void feedbackLoopTestCase2() {
        int[] program = {3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54,
                -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4,
                53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10};
        long output = PhaseSettings.calculateFeedbackThrusterOutput(program, List.of(9, 7, 8, 5, 6));
        assertThat(output).isEqualTo(18216);
    }

    @Test
    void feedbackLoopTestCase2MaxOutput() {
        int[] program = {3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54,
                -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4,
                53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10};
        long output = PhaseSettings.calculateFeedbackThrusterMaxOutput(program);
        assertThat(output).isEqualTo(18216);
    }

    @Test
    void testCombinations() {
        assertThat(combinations(2)).containsExactly(List.of(0, 1), List.of(1, 0));
        assertThat(combinations(3)).containsExactlyInAnyOrder(
                List.of(0, 1, 2),
                List.of(0, 2, 1),
                List.of(1, 0, 2),
                List.of(1, 2, 0),
                List.of(2, 1, 0),
                List.of(2, 0, 1)
        );
    }

    @Test
    void testCombinationsWithStartIndex() {
        assertThat(combinations(2, 5)).containsExactly(List.of(5, 6), List.of(6, 5));
        assertThat(combinations(3, 5)).containsExactlyInAnyOrder(
                List.of(5, 6, 7),
                List.of(5, 7, 6),
                List.of(6, 5, 7),
                List.of(6, 7, 5),
                List.of(7, 5, 6),
                List.of(7, 6, 5)
        );
    }

    @Test
    void assignment() {
        int[] program = readProgramInput("day7/input.txt");
        long result1 = PhaseSettings.calculateMaxThrusterOutput(program);
        System.out.println("result = " + result1);
        long result2 = PhaseSettings.calculateFeedbackThrusterMaxOutput(program.clone());
        System.out.println("result2 = " + result2);
    }
}