package day2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.readProgramInput;

class ProgramAlarmTest {

    @Test
    void testPrograms() {
        assertThat(ProgramAlarm.process(new int[]{1, 0, 0, 0, 99})).isEqualTo(new int[]{2, 0, 0, 0, 99});
        assertThat(ProgramAlarm.process(new int[]{2, 3, 0, 3, 99})).isEqualTo(new int[]{2, 3, 0, 6, 99});
        assertThat(ProgramAlarm.process(new int[]{2, 4, 4, 5, 99, 0})).isEqualTo(new int[]{2, 4, 4, 5, 99, 9801});
        assertThat(ProgramAlarm.process(new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99})).isEqualTo(new int[]{30, 1, 1, 4, 2, 5, 6, 0, 99});
    }

    @Test
    void assignment1() {
        int result = runProgram(readProgramInput("day2/input.txt"), 12, 2);
        System.out.println("result = " + result);
    }

    @Test
    void assignment2() {
        int[] input = readProgramInput("day2/input.txt");
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                if (runProgram(input, noun, verb) == 19690720) {
                    System.out.println("noun = " + noun + " verb = " + verb + " result=" + runProgram(input, noun, verb));
                    System.out.println("submission = " + (100 * noun + verb));
                }
            }
        }
    }

    private int runProgram(int[] input, int noun, int verb) {
        int[] programCopy = input.clone();
        programCopy[1] = noun;
        programCopy[2] = verb;
        return ProgramAlarm.process(programCopy)[0];
    }

}
