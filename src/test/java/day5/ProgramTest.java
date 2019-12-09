package day5;

import org.junit.jupiter.api.Test;

import java.util.List;

import static day5.Program.initialize;
import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.readProgramInput;

class ProgramTest {

    @Test
    void testInput() {
        int[] programCode = {3, 0, 99};
        Program program = initialize(programCode).run(1);
        assertThat(program.memory[0]).isEqualTo(1);
    }

    @Test
    void testMultipleInput() {
        int[] programCode = {3, 0, 3, 1, 99};
        Program program = initialize(programCode).run(100, 200);
        assertThat(program.memory[0]).isEqualTo(100);
        assertThat(program.memory[1]).isEqualTo(200);
    }

    @Test
    void testOutput() {
        int[] program = {4, 2, 99};
        long output = initialize(program).run().getLatestOutput();
        assertThat(output).isEqualTo(99);
    }

    @Test
    void testMultipleOutput() {
        int[] program = {4, 0, 4, 4, 99};
        List<Long> outputs = initialize(program).run().getOutputs();
        assertThat(outputs).containsExactly(4L, 99L);
    }

    @Test
    void testMultiplicationByValue() {
        int[] programCode = {1102, 4, 3, 0, 99};
        Program program = initialize(programCode).run();
        assertThat(program.memory[0]).isEqualTo(12);
    }

    @Test
    void testMixedMultiplication() {
        int[] programCode1 = {102, 4, 1, 0, 99};
        Program program1 = initialize(programCode1).run();
        assertThat(program1.memory[0]).isEqualTo(16);

        int[] programCode2 = {1002, 2, 4, 0, 99};
        Program program2 = initialize(programCode2).run();
        assertThat(program2.memory[0]).isEqualTo(16);
    }

    @Test
    void testMixedAddition() {
        int[] programCode1 = {101, 4, 1, 0, 99};
        Program program1 = initialize(programCode1).run();
        assertThat(program1.memory[0]).isEqualTo(8);

        int[] programCode2 = {1001, 2, 4, 0, 99};
        Program program2 = initialize(programCode2).run();
        assertThat(program2.memory[0]).isEqualTo(8);
    }

    @Test
    void jumpIfTrue() {
        // it will output 888 if second element is not 0, otherwise will exit early
        int[] program = {1105, 1, 4, 99, 104, 888, 99};
        long output = initialize(program).run().getLatestOutput();
        assertThat(output).isEqualTo(888);
    }

    @Test
    void jumpIfTrueByReference() {
        // it will output 888 if second element is pointing to non-zero value, otherwise will exit early
        int[] program = {5, 0, 0, 99, -1, 104, 888, 99};
        long output = initialize(program).run().getLatestOutput();
        assertThat(output).isEqualTo(888);
    }

    @Test
    void jumpIfFalse() {
        // it will output 888 if second element is 0, otherwise will exit early
        int[] program = {1106, 0, 4, 99, 104, 888, 99};
        long output = initialize(program).run().getLatestOutput();
        assertThat(output).isEqualTo(888);
    }

    @Test
    void jumpIfFalseByReference() {
        // it will output 888 if second element is pointing to zero value, otherwise will exit early
        int[] program = {6, 2, 0, 99, -1, -1, 104, 888, 99};
        long output = initialize(program).run().getLatestOutput();
        assertThat(output).isEqualTo(888);
    }

    @Test
    void lessThanTrue() {
        int[] programCode = new int[]{1107, 3, 4, 5, 99, -1};
        Program program = initialize(programCode).run();
        assertThat(program.memory[5]).isEqualTo(1);

    }

    @Test
    void lessThanFalse() {
        int[] programCode = new int[]{1107, 4, 4, 5, 99, -1};
        Program program = initialize(programCode).run();
        assertThat(program.memory[5]).isEqualTo(0);
    }

    @Test
    void equalsTrue() {
        int[] programCode = new int[]{1108, 4, 4, 5, 99, -1};
        Program program = initialize(programCode).run();
        assertThat(program.memory[5]).isEqualTo(1);
    }

    @Test
    void equalsFalse() {
        int[] programCode = new int[]{1108, 6, 4, 5, 99, -1};
        Program program = initialize(programCode).run();
        assertThat(program.memory[5]).isEqualTo(0);
    }

    @Test
    void testCase1() {
        int[] program = {3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9};
        assertThat(initialize(program).run(0).getLatestOutput()).isEqualTo(0);
        assertThat(initialize(program).run(123).getLatestOutput()).isEqualTo(1);
    }

    @Test
    void testCase2() {
        int[] program = {3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1};
        assertThat(initialize(program).run(0).getLatestOutput()).isEqualTo(0);
        assertThat(initialize(program).run(123).getLatestOutput()).isEqualTo(1);
    }

    @Test
    void testCase3() {
        int[] program = {3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99};
        assertThat(initialize(program).run(7).getLatestOutput()).isEqualTo(999);
        assertThat(initialize(program).run(8).getLatestOutput()).isEqualTo(1000);
        assertThat(initialize(program).run(9).getLatestOutput()).isEqualTo(1001);
    }

    @Test
    void assignment() {
        int[] program = readProgramInput("day5/input.txt");
        long result1 = initialize(program).run(1).getLatestOutput();
        System.out.println("result1 = " + result1);
        long result2 = initialize(program).run(5).getLatestOutput();
        System.out.println("result2 = " + result2);
    }

    @Test
    void relativeParameterModeBehavesSameAsPositionalWhenNoOffset() {
        int[] programCode = {2202, 5, 6, 7, 99, 3, 4, -1};
        Program program = initialize(programCode).run();
        assertThat(program.memory[7]).isEqualTo(12);
    }

    @Test
    void relativeParameterModeWithOffset() {
        int[] programCode = {
                109, 1, // set offset to 1
                2202, 6, 7, 9, // multiply using relative addresses
                99,
                3, // first param at position 7
                4, // second param at position 8
                -1 // result placeholder at position 9
        };
        Program program = initialize(programCode).run();
        assertThat(program.memory[9]).isEqualTo(12);
    }

    @Test
    void relativeParameterModeWithOffsetChangingSeveralTimes() {
        int[] programCode = {
                109, 1, // adjust offset by 1
                109, 1, // adjust offset by 1
                2202, 7, 8, 11, // multiply parameters relatively and save in last index
                99,
                3, // first parameter at position 9
                4, // second position at position 10
                -1 // result placeholder
        };
        Program program = initialize(programCode).run();
        assertThat(program.memory[11]).isEqualTo(12);
    }

    @Test
    void day9testCase1() {
        int[] programCode = {109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99};
        List<Long> outputs = initialize(programCode).run().getOutputs();
        for (int i = 0; i < programCode.length; i++) {
            assertThat(outputs.get(i)).describedAs("Output at index %d", i).isEqualTo(programCode[i]);
        }
    }

    @Test
    void day9testCase2() {
        int[] programCode = {1102, 34915192, 34915192, 7, 4, 7, 99, 0};
        long output = initialize(programCode).run().getLatestOutput();
        assertThat(String.valueOf(output)).hasSize(16);
    }

    @Test
    void day9testCase3() {
        long[] programCode = {104, 1125899906842624L, 99};
        long output = initialize(programCode).run().getLatestOutput();
        assertThat(output).isEqualTo(programCode[1]);
    }
}