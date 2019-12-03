package day3;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import static day3.CrossedWires.Coordinate.coordinate;
import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.getScanner;

class CrossedWiresTest {

    @Test
    void returnsOneWhenWiresIntersectImmediatelyToTheRight() {
        List<String> wire1 = List.of("R2");
        List<String> wire2 = List.of("R2");
        assertThat(CrossedWires.getClosestIntersectionDistance(wire1, wire2)).isEqualTo(1);
    }

    @Test
    void calculatesIntersectionDiagonally() {
        List<String> wire1 = List.of("R1", "U1");
        List<String> wire2 = List.of("U1", "R1");
        assertThat(CrossedWires.getClosestIntersectionDistance(wire1, wire2)).isEqualTo(2);
    }

    @Test
    void closestIntersectionTestCase1() {
        List<String> wire1 = List.of("R8", "U5", "L5", "D3");
        List<String> wire2 = List.of("U7", "R6", "D4", "L4");
        assertThat(CrossedWires.getClosestIntersectionDistance(wire1, wire2)).isEqualTo(6);
    }

    @Test
    void closestIntersectionTestCase2() {
        List<String> wire1 = List.of("R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72");
        List<String> wire2 = List.of("U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83");
        assertThat(CrossedWires.getClosestIntersectionDistance(wire1, wire2)).isEqualTo(159);
    }

    @Test
    void closestIntersectionTestCase3() {
        List<String> wire1 = List.of("R98", "U47", "R26", "D63", "R33", "U87", "L62", "D20", "R33", "U53", "R51");
        List<String> wire2 = List.of("U98", "R91", "D20", "R16", "D67", "R40", "U7", "R15", "U6", "R7");
        assertThat(CrossedWires.getClosestIntersectionDistance(wire1, wire2)).isEqualTo(135);
    }

    @Test
    void minSumOfStepsToIntersectionTestCase1() {
        List<String> wire1 = List.of("R8", "U5", "L5", "D3");
        List<String> wire2 = List.of("U7", "R6", "D4", "L4");
        assertThat(CrossedWires.getSumOfStepsToNearestIntersection(wire1, wire2)).isEqualTo(30);
    }

    @Test
    void minSumOfStepsToIntersectionTestCase2() {
        List<String> wire1 = List.of("R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72");
        List<String> wire2 = List.of("U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83");
        assertThat(CrossedWires.getSumOfStepsToNearestIntersection(wire1, wire2)).isEqualTo(610);
    }

    @Test
    void minSumOfStepsToIntersectionTestCase3() {
        List<String> wire1 = List.of("R98", "U47", "R26", "D63", "R33", "U87", "L62", "D20", "R33", "U53", "R51");
        List<String> wire2 = List.of("U98", "R91", "D20", "R16", "D67", "R40", "U7", "R15", "U6", "R7");
        assertThat(CrossedWires.getSumOfStepsToNearestIntersection(wire1, wire2)).isEqualTo(410);
    }

    @Test
    void assignment() throws FileNotFoundException {
        Scanner scanner = getScanner("day3/input.txt");

        Function<String, List<String>> toPath =
                line -> Arrays.stream(line.split(",")).collect(Collectors.toList());

        List<String> wire1 = toPath.apply(scanner.nextLine());
        List<String> wire2 = toPath.apply(scanner.nextLine());

        System.out.println("getIntersectionDistance(wire1, wire2) = " + CrossedWires.getClosestIntersectionDistance(wire1, wire2));
        System.out.println("getSumOfStepsToNearestIntersection(wire1, wire2) = " + CrossedWires.getSumOfStepsToNearestIntersection(wire1, wire2));
    }

    @Test
    void calculatesVisitedCoordinatesFromMovement() {
        assertThat(CrossedWires.calculateVisitedCoordinates(coordinate(0, 0), "R1"))
                .containsExactly(coordinate(1, 0));

        assertThat(CrossedWires.calculateVisitedCoordinates(coordinate(0, 0), "R2"))
                .containsExactly(coordinate(1, 0), coordinate(2, 0));

        assertThat(CrossedWires.calculateVisitedCoordinates(coordinate(1, 1), "R2"))
                .containsExactly(coordinate(2, 1), coordinate(3, 1));

        assertThat(CrossedWires.calculateVisitedCoordinates(coordinate(0, 0), "L1"))
                .containsExactly(coordinate(-1, 0));

        assertThat(CrossedWires.calculateVisitedCoordinates(coordinate(0, 0), "U1"))
                .containsExactly(coordinate(0, 1));

        assertThat(CrossedWires.calculateVisitedCoordinates(coordinate(0, 0), "D1"))
                .containsExactly(coordinate(0, -1));
    }

    @Test
    void calculatesVisitedCoordinatesForPath() {
        assertThat(CrossedWires.calculateVisitedCoordinates(List.of("R1", "U1"))).containsExactly(
                coordinate(1, 0),
                coordinate(1, 1)
        );
    }

}
