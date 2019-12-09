package day8;

import day5.Program;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.readProgramInput;

class SensorBoost {

    @Test
    void assignment() {
        int[] programCode = readProgramInput("day9/input.txt");
        List<Long> result1 = Program.initialize(programCode).run(1).getOutputs();
        System.out.println("result1 = " + result1);
        assertThat(result1).containsExactly(4234906522L);

        List<Long> result2 = Program.initialize(programCode).run(2).getOutputs();
        System.out.println("result2 = " + result2);
        assertThat(result2).containsExactly(60962L);
    }
}
