package day2;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static util.FileHelper.getScanner;

class ProgramAlarmTest {

    @Test
    void testPrograms() {
        assertThat(ProgramAlarm.process(new int[]{1, 0, 0, 0, 99})).isEqualTo(new int[]{2, 0, 0, 0, 99});
        assertThat(ProgramAlarm.process(new int[]{2, 3, 0, 3, 99})).isEqualTo(new int[]{2, 3, 0, 6, 99});
        assertThat(ProgramAlarm.process(new int[]{2, 4, 4, 5, 99, 0})).isEqualTo(new int[]{2, 4, 4, 5, 99, 9801});
        assertThat(ProgramAlarm.process(new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99})).isEqualTo(new int[]{30, 1, 1, 4, 2, 5, 6, 0, 99});
    }

    @Test
    void assignment1() throws FileNotFoundException {
        int result = runProgram(readInput(), 12, 2);
        System.out.println("result = " + result);
    }

    @Test
    void assignment2() throws FileNotFoundException {
        int[] input = readInput();
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

    private int[] readInput() throws FileNotFoundException {
        Scanner scanner = getScanner("day2/input.txt");
        String[] inputAsStrings = scanner.nextLine().split(",");
        int[] input = new int[inputAsStrings.length];
        for (int i = 0; i < inputAsStrings.length; i++) {
            input[i] = Integer.parseInt(inputAsStrings[i]);
        }
        return input;
    }

}