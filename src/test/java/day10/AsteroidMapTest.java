package day10;

import day10.AsteroidMap.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static day10.AsteroidMap.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static util.FileHelper.getScanner;

class AsteroidMapTest {

    @Test
    void readsSingleAsteroid() {
        Set<Asteroid> asteroids = readAsteroidsFromMap(List.of("#"));
        assertThat(asteroids).containsExactly(asteroid(0, 0));
    }

    @Test
    void returnsEmptyListWhenThereAreNoAsteroids() {
        Set<Asteroid> asteroids = readAsteroidsFromMap(List.of("."));
        assertThat(asteroids).isEmpty();
    }

    @Test
    void readsMultipleAsteroidsOnSameRow() {
        Set<Asteroid> asteroids = readAsteroidsFromMap(List.of("#.#."));
        assertThat(asteroids).containsExactlyInAnyOrder(
                asteroid(0, 0),
                asteroid(2, 0)
        );
    }

    @Test
    void readsMultipleAsteroidsOnMultipleRows() {
        Set<Asteroid> asteroids = readAsteroidsFromMap(List.of(
                "..#.",
                ".#.."
        ));
        assertThat(asteroids).containsExactlyInAnyOrder(
                asteroid(2, 0),
                asteroid(1, 1)
        );
    }

    @Test
    void countsVisibleAsteroids() {
        Map<Asteroid, Integer> result = countVisibleAsteroids(List.of(
                "##"
        ));
        assertThat(result).containsEntry(asteroid(0, 0), 1);
        assertThat(result).containsEntry(asteroid(1, 0), 1);
    }

    @Test
    void returnsZeroCountForSingleAsteroid() {
        Map<Asteroid, Integer> result = countVisibleAsteroids(List.of(
                "#"
        ));
        assertThat(result).containsEntry(asteroid(0, 0), 0);
    }

    @Test
    void asteroidIsNotVisibleIfAnotherIsInTheWay() {
        Map<Asteroid, Integer> result = countVisibleAsteroids(List.of(
                "###"
        ));
        assertThat(result).containsEntry(asteroid(0, 0), 1);
        assertThat(result).containsEntry(asteroid(1, 0), 2);
        assertThat(result).containsEntry(asteroid(2, 0), 1);
    }

    @Test
    void findVaporizationOrderOfAsteroidsClockwise() {
        List<Asteroid> order = AsteroidMap.findVaporizationOrder(asteroid(1, 1), List.of(
                "###",
                "###",
                "###"
        ));
        assertThat(order).containsExactly(
                asteroid(1, 0),
                asteroid(2, 0),
                asteroid(2, 1),
                asteroid(2, 2),
                asteroid(1, 2),
                asteroid(0, 2),
                asteroid(0, 1),
                asteroid(0, 0)
        );
    }

    @Test
    void testCase1() {
        Map<Asteroid, Integer> result = countVisibleAsteroids(List.of(
                "......#.#.",
                "#..#.#....",
                "..#######.",
                ".#.#.###..",
                ".#..#.....",
                "..#....#.#",
                "#..#....#.",
                ".##.#..###",
                "##...#..#.",
                ".#....####"
        ));

        assertThat(result).containsEntry(asteroid(5, 8), 33);
    }

    @Test
    void testCase2() {
        Map<Asteroid, Integer> result = countVisibleAsteroids(List.of(
                "#.#...#.#.",
                ".###....#.",
                ".#....#...",
                "##.#.#.#.#",
                "....#.#.#.",
                ".##..###.#",
                "..#...##..",
                "..##....##",
                "......#...",
                ".####.###."
        ));

        assertThat(result).containsEntry(asteroid(1, 2), 35);
    }

    @Test
    void testCase3() {
        Map<Asteroid, Integer> result = countVisibleAsteroids(List.of(
                ".#..#..###",
                "####.###.#",
                "....###.#.",
                "..###.##.#",
                "##.##.#.#.",
                "....###..#",
                "..#.#..#.#",
                "#..#.#.###",
                ".##...##.#",
                ".....#.#.."
        ));

        assertThat(result).containsEntry(asteroid(6, 3), 41);
    }

    @Test
    void testCase4() {
        List<String> map = List.of(
                ".#..##.###...#######",
                "##.############..##.",
                ".#.######.########.#",
                ".###.#######.####.#.",
                "#####.##.#.##.###.##",
                "..#####..#.#########",
                "####################",
                "#.####....###.#.#.##",
                "##.#################",
                "#####.##.###..####..",
                "..######..##.#######",
                "####.##.####...##..#",
                ".#####..#.######.###",
                "##...#.##########...",
                "#.##########.#######",
                ".####.#.###.###.#.##",
                "....##.##.###..#####",
                ".#.#.###########.###",
                "#.#.#.#####.####.###",
                "###.##.####.##.#..##"
        );
        Map<Asteroid, Integer> result = countVisibleAsteroids(map);

        assertThat(result.get(asteroid(11, 13))).isEqualTo(210);

        List<Asteroid> vaporizationOrder = findVaporizationOrder(asteroid(11, 13), map);
        assertThat(vaporizationOrder.get(0)).isEqualTo(asteroid(11, 12));
        assertThat(vaporizationOrder.get(1)).isEqualTo(asteroid(12, 1));
        assertThat(vaporizationOrder.get(2)).isEqualTo(asteroid(12, 2));
        assertThat(vaporizationOrder.get(9)).isEqualTo(asteroid(12, 8));
        assertThat(vaporizationOrder.get(19)).isEqualTo(asteroid(16, 0));
        assertThat(vaporizationOrder.get(49)).isEqualTo(asteroid(16, 9));
        assertThat(vaporizationOrder.get(99)).isEqualTo(asteroid(10, 16));
        assertThat(vaporizationOrder.get(198)).isEqualTo(asteroid(9, 6));
        assertThat(vaporizationOrder.get(199)).isEqualTo(asteroid(8, 2));
        assertThat(vaporizationOrder.get(200)).isEqualTo(asteroid(10, 9));
        assertThat(vaporizationOrder.get(298)).isEqualTo(asteroid(11, 1));
    }

    @Test
    void testCase0() {
        Map<Asteroid, Integer> counts = countVisibleAsteroids(List.of(
                ".#..#",
                ".....",
                "#####",
                "....#",
                "...##"
        ));
        assertThat(counts.get(asteroid(1, 0))).isEqualTo(7);
        assertThat(counts.get(asteroid(4, 0))).isEqualTo(7);
        assertThat(counts.get(asteroid(0, 2))).isEqualTo(6);
        assertThat(counts.get(asteroid(1, 2))).isEqualTo(7);
        assertThat(counts.get(asteroid(2, 2))).isEqualTo(7);
        assertThat(counts.get(asteroid(3, 2))).isEqualTo(7);
        assertThat(counts.get(asteroid(4, 2))).isEqualTo(5);
        assertThat(counts.get(asteroid(4, 3))).isEqualTo(7);
        assertThat(counts.get(asteroid(3, 4))).isEqualTo(8);
        assertThat(counts.get(asteroid(4, 4))).isEqualTo(7);
    }

    @Test
    void assignment() {
        LinkedList<String> map = readMap();
        Map<Asteroid, Integer> counts = countVisibleAsteroids(map);
        Asteroid bestAsteroid = counts.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).orElseThrow().getKey();
        Integer result = counts.get(bestAsteroid);
        System.out.println("result = " + result);
        assertThat(result).isEqualTo(276);

        List<Asteroid> vaporizationOrder = findVaporizationOrder(bestAsteroid, map);
        Asteroid targetAsteroid = vaporizationOrder.get(199);
        System.out.println("targetAsteroid = " + targetAsteroid);
        assertThat(targetAsteroid).isEqualTo(asteroid(13, 21));
        System.out.println("submission = " + (targetAsteroid.getX() * 100 + targetAsteroid.getY()));
    }

    private LinkedList<String> readMap() {
        Scanner scanner = getScanner("day10/input.txt");
        LinkedList<String> map = new LinkedList<>();
        while (scanner.hasNextLine()) {
            map.add(scanner.nextLine());
        }
        return map;
    }
}