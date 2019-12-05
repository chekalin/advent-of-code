package day5;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.readProgramInput;

class AdvancedProgramStatelessTest {

    @Test
    void testInput() {
        int[] program = {3, 0, 99};
        AdvancedProgramStateless.process(program, 1);
        assertThat(program[0]).isEqualTo(1);
    }

    @Test
    void testMultipleInput() {
        int[] program = {3, 0, 3, 1, 99};
        AdvancedProgramStateless.process(program, 100, 200);
        assertThat(program[0]).isEqualTo(100);
        assertThat(program[1]).isEqualTo(200);
    }

    @Test
    void testOutput() {
        int[] program = {4, 2, 99};
        int[] output = AdvancedProgramStateless.process(program);
        assertThat(output[0]).isEqualTo(99);
    }

    @Test
    void testMultipleOutput() {
        int[] program = {4, 0, 4, 4, 99};
        int[] output = AdvancedProgramStateless.process(program);
        assertThat(output).isEqualTo(new int[]{4, 99});
    }

    @Test
    void testMultiplicationByValue() {
        int[] program = {1102, 4, 3, 0, 99};
        AdvancedProgramStateless.process(program);
        assertThat(program[0]).isEqualTo(12);
    }

    @Test
    void testMixedMultiplication() {
        int[] program1 = {102, 4, 1, 0, 99};
        AdvancedProgramStateless.process(program1);
        assertThat(program1[0]).isEqualTo(16);

        int[] program2 = {1002, 2, 4, 0, 99};
        AdvancedProgramStateless.process(program2);
        assertThat(program2[0]).isEqualTo(16);
    }

    @Test
    void testMixedAddition() {
        int[] program1 = {101, 4, 1, 0, 99};
        AdvancedProgramStateless.process(program1);
        assertThat(program1[0]).isEqualTo(8);

        int[] program2 = {1001, 2, 4, 0, 99};
        AdvancedProgramStateless.process(program2);
        assertThat(program2[0]).isEqualTo(8);
    }

    @Test
    void jumpIfTrue() {
        // it will output 888 if second element is not 0, otherwise will exit early
        int[] program = {1105, 1, 4, 99, 104, 888, 99};
        int[] output = AdvancedProgramStateless.process(program);
        assertThat(output[0]).isEqualTo(888);
    }

    @Test
    void jumpIfTrueByReference() {
        // it will output 888 if second element is pointing to non-zero value, otherwise will exit early
        int[] program = {5, 0, 0, 99, -1, 104, 888, 99};
        int[] output = AdvancedProgramStateless.process(program);
        assertThat(output[0]).isEqualTo(888);
    }

    @Test
    void jumpIfFalse() {
        // it will output 888 if second element is 0, otherwise will exit early
        int[] program = {1106, 0, 4, 99, 104, 888, 99};
        int[] output = AdvancedProgramStateless.process(program);
        assertThat(output[0]).isEqualTo(888);
    }

    @Test
    void jumpIfFalseByReference() {
        // it will output 888 if second element is pointing to zero value, otherwise will exit early
        int[] program = {6, 2, 0, 99, -1, -1, 104, 888, 99};
        int[] output = AdvancedProgramStateless.process(program);
        assertThat(output[0]).isEqualTo(888);
    }

    @Test
    void lessThanTrue() {
        int[] program = new int[]{1107, 3, 4, 5, 99, -1};
        AdvancedProgramStateless.process(program);
        assertThat(program[5]).isEqualTo(1);

    }

    @Test
    void lessThanFalse() {
        int[] program = new int[]{1107, 4, 4, 5, 99, -1};
        AdvancedProgramStateless.process(program);
        assertThat(program[5]).isEqualTo(0);
    }

    @Test
    void equalsTrue() {
        int[] program = new int[]{1108, 4, 4, 5, 99, -1};
        AdvancedProgramStateless.process(program);
        assertThat(program[5]).isEqualTo(1);
    }

    @Test
    void equalsFalse() {
        int[] program = new int[]{1108, 6, 4, 5, 99, -1};
        AdvancedProgramStateless.process(program);
        assertThat(program[5]).isEqualTo(0);
    }

    @Test
    void testCase1() {
        int[] program = {3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9};
        assertThat(AdvancedProgramStateless.process(program.clone(), 0)).isEqualTo(new int[]{0});
        assertThat(AdvancedProgramStateless.process(program.clone(), 123)).isEqualTo(new int[]{1});
    }

    @Test
    void testCase2() {
        int[] program = {3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1};
        assertThat(AdvancedProgramStateless.process(program.clone(), 0)).isEqualTo(new int[]{0});
        assertThat(AdvancedProgramStateless.process(program.clone(), 123)).isEqualTo(new int[]{1});
    }

    @Test
    void testCase3() {
        int[] program = {3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99};
        assertThat(AdvancedProgramStateless.process(program.clone(), 7)).isEqualTo(new int[]{999});
        assertThat(AdvancedProgramStateless.process(program.clone(), 8)).isEqualTo(new int[]{1000});
        assertThat(AdvancedProgramStateless.process(program.clone(), 9)).isEqualTo(new int[]{1001});
    }

    @Test
    void assignment() throws FileNotFoundException {
        int[] program = readProgramInput("day5/input.txt");
        int[] result1 = AdvancedProgramStateless.process(program.clone(), 1);
        System.out.println("result1 = " + Arrays.toString(result1));
        int[] result2 = AdvancedProgramStateless.process(program.clone(), 5);
        System.out.println("result2 = " + Arrays.toString(result2));
    }

}