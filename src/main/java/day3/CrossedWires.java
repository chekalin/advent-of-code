package day3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static day3.CrossedWires.Coordinate.coordinate;

class CrossedWires {

    static int getClosestIntersectionDistance(List<String> wire1, List<String> wire2) {
        List<Coordinate> wirePath1 = calculateVisitedCoordinates(wire1);
        List<Coordinate> wirePath2 = calculateVisitedCoordinates(wire2);
        return intersection(wirePath1, wirePath2)
                .stream()
                .mapToInt(Coordinate::manhattanDistanceFromSource)
                .min()
                .orElseThrow();
    }

    static int getSumOfStepsToNearestIntersection(List<String> wire1, List<String> wire2) {
        List<Coordinate> wirePath1 = calculateVisitedCoordinates(wire1);
        List<Coordinate> wirePath2 = calculateVisitedCoordinates(wire2);
        return intersection(wirePath1, wirePath2)
                .stream()
                .mapToInt(intersection -> calculateSumOfStepsToIntersection(wirePath1, wirePath2, intersection))
                .min()
                .orElseThrow();
    }

    static List<Coordinate> calculateVisitedCoordinates(List<String> path) {
        List<Coordinate> visited = new LinkedList<>();
        Coordinate current = coordinate(0, 0);
        for (String movement : path) {
            visited.addAll(calculateVisitedCoordinates(current, movement));
            current = visited.get(visited.size() - 1);
        }
        return visited;
    }

    private static Set<Coordinate> intersection(List<Coordinate> wirePath1, List<Coordinate> wirePath2) {
        Set<Coordinate> intersection = new HashSet<>(wirePath1);
        intersection.retainAll(new HashSet<>(wirePath2));
        return intersection;
    }

    private static int calculateSumOfStepsToIntersection(List<Coordinate> wirePath1, List<Coordinate> wirePath2, Coordinate intersection) {
        return wirePath1.indexOf(intersection) + wirePath2.indexOf(intersection) + 2;
    }

    static List<Coordinate> calculateVisitedCoordinates(Coordinate start, String movement) {
        char direction = movement.charAt(0);
        int numberOfSteps = Integer.parseInt(movement.substring(1));
        return IntStream.range(1, numberOfSteps + 1)
                .mapToObj(i -> {
                    switch (direction) {
                        case 'R':
                            return coordinate(start.x + i, start.y);
                        case 'L':
                            return coordinate(start.x - i, start.y);
                        case 'U':
                            return coordinate(start.x, start.y + i);
                        case 'D':
                            return coordinate(start.x, start.y - i);
                        default:
                            throw new IllegalArgumentException("Unknown direction: " + direction);
                    }
                })
                .collect(Collectors.toList());
    }

    static class Coordinate {
        int x;
        int y;

        static Coordinate coordinate(int x, int y) {
            return new Coordinate(x, y);
        }

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int manhattanDistanceFromSource() {
            return Math.abs(x) + Math.abs(y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
