package day10;

import java.util.*;

class AsteroidMap {

    private static final char ASTEROID_SYMBOL = '#';

    static Set<Asteroid> readAsteroidsFromMap(List<String> map) {
        Set<Asteroid> result = new HashSet<>();
        for (int y = 0; y < map.size(); y++) {
            String row = map.get(y);
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == ASTEROID_SYMBOL) {
                    result.add(new Asteroid(x, y));
                }
            }
        }
        return result;
    }

    static Asteroid asteroid(int x, int y) {
        return new Asteroid(x, y);
    }

    static Map<Asteroid, Integer> countVisibleAsteroids(List<String> map) {
        return AsteroidMap.countVisible(readAsteroidsFromMap(map));
    }

    private static Map<Asteroid, Integer> countVisible(Set<Asteroid> asteroids) {
        Map<Asteroid, Integer> counts = new HashMap<>();
        for (Asteroid sourceAsteroid : asteroids) {
            Set<Double> angles = new HashSet<>();
            int count = 0;
            for (Asteroid targetAsteroid : asteroids) {
                if (!sourceAsteroid.equals(targetAsteroid)) {
                    double angle = calculateAngle(sourceAsteroid, targetAsteroid);
                    if (!angles.contains(angle)) {
                        angles.add(angle);
                        count++;
                    }
                }
            }
            counts.put(sourceAsteroid, count);
        }
        return counts;
    }

    private static double calculateAngle(Asteroid sourceAsteroid, Asteroid targetAsteroid) {
        return Math.atan2(targetAsteroid.getX() - sourceAsteroid.getX(), targetAsteroid.getY() - sourceAsteroid.getY());
    }

    static List<Asteroid> findVaporizationOrder(Asteroid center, List<String> map) {
        Set<Asteroid> asteroids = readAsteroidsFromMap(map);
        asteroids.remove(center);

        TreeMap<Double, TreeSet<Asteroid>> asteroidsByAngle = groupAndSort(center, asteroids);
        return traverseInOrder(asteroidsByAngle);
    }

    private static List<Asteroid> traverseInOrder(TreeMap<Double, TreeSet<Asteroid>> asteroidsByAngle) {
        List<Asteroid> result = new LinkedList<>();
        while (!asteroidsByAngle.isEmpty()) {
            Iterator<Map.Entry<Double, TreeSet<Asteroid>>> iterator = asteroidsByAngle.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Double, TreeSet<Asteroid>> next = iterator.next();
                TreeSet<Asteroid> value = next.getValue();
                if (value.isEmpty()) {
                    iterator.remove();
                } else {
                    Asteroid asteroid = value.iterator().next();
                    result.add(asteroid);
                    value.remove(asteroid);
                }
            }
        }
        return result;
    }

    private static TreeMap<Double, TreeSet<Asteroid>> groupAndSort(Asteroid center, Set<Asteroid> asteroids) {
        TreeMap<Double, TreeSet<Asteroid>> asteroidsByAngle = new TreeMap<>(Comparator.reverseOrder());
        asteroids.forEach(asteroid -> {
            double angle = calculateAngle(center, asteroid);
            if (!asteroidsByAngle.containsKey(angle)) {
                TreeSet<Asteroid> sortedAsteroids = new TreeSet<>(Comparator.comparingDouble(a -> squareDistance(center, a)));
                asteroidsByAngle.put(angle, sortedAsteroids);
            }
            asteroidsByAngle.get(angle).add(asteroid);
        });
        return asteroidsByAngle;
    }

    private static double squareDistance(Asteroid center, Asteroid asteroid) {
        return Math.pow(center.getX() - asteroid.getX(), 2) + Math.pow(center.getY() - asteroid.getY(), 2);
    }

    static class Asteroid {
        private int x;
        private int y;

        private Asteroid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Asteroid asteroid = (Asteroid) o;
            return x == asteroid.x &&
                    y == asteroid.y;
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
