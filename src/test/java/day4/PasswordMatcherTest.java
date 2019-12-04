package day4;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static day4.PasswordMatcher.*;
import static org.assertj.core.api.Assertions.assertThat;

class PasswordMatcherTest {

    @Test
    void testHasAtLeastTwoAdjacentDigits() {
        assertThat(hasAtLeastTwoAdjacentDigits(1)).isFalse();
        assertThat(hasAtLeastTwoAdjacentDigits(11)).isTrue();
        assertThat(hasAtLeastTwoAdjacentDigits(211)).isTrue();
        assertThat(hasAtLeastTwoAdjacentDigits(121331)).isTrue();
        assertThat(hasAtLeastTwoAdjacentDigits(1111)).isTrue();
    }

    @Test
    void testHasExactlyTwoAdjacentDigits() {
        assertThat(hasExactlyTwoAdjacentDigits(1)).isFalse();
        assertThat(hasExactlyTwoAdjacentDigits(11)).isTrue();
        assertThat(hasExactlyTwoAdjacentDigits(111)).isFalse();
        assertThat(hasExactlyTwoAdjacentDigits(211)).isTrue();
        assertThat(hasExactlyTwoAdjacentDigits(22211)).isTrue();
        assertThat(hasExactlyTwoAdjacentDigits(121331)).isTrue();
    }

    @Test
    void testHasNoDecreasingNumbers() {
        assertThat(hasNoDecreasingNumbers(12)).isTrue();
        assertThat(hasNoDecreasingNumbers(21)).isFalse();
    }

    @Test
    void assignment() {
        long result1 = IntStream.range(124075, 580769 + 1)
                .filter(password -> hasNoDecreasingNumbers(password) && hasAtLeastTwoAdjacentDigits(password))
                .count();
        System.out.println("result = " + result1);

        long result2 = IntStream.range(124075, 580769 + 1)
                .filter(password -> hasNoDecreasingNumbers(password) && hasExactlyTwoAdjacentDigits(password))
                .count();
        System.out.println("result = " + result2);

    }
}