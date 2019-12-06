package day6;

import java.util.*;

class OrbitMap {

    static int totalNumberOfOrbits(List<OrbitRelationship> orbits) {
        String center = orbits.stream()
                .filter(thisRelationship -> orbits.stream().noneMatch(thatRelationship -> thisRelationship.center.equals(thatRelationship.orbiting)))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find center of the map")).center;

        Stack<String> planetsToProcess = new Stack<>();
        planetsToProcess.add(center);

        Map<String, Integer> orbitCountPerPlanet = new HashMap<>();
        orbitCountPerPlanet.put(center, 0);

        while (!planetsToProcess.isEmpty()) {
            String current = planetsToProcess.pop();
            Integer currentOrbitCount = orbitCountPerPlanet.get(current);
            orbits.stream()
                    .filter(orbit -> orbit.center.equals(current))
                    .map(orbit -> orbit.orbiting)
                    .forEach(planet -> {
                        planetsToProcess.add(planet);
                        orbitCountPerPlanet.put(planet, currentOrbitCount + 1);
                    });
        }
        return orbitCountPerPlanet.values().stream().reduce(0, Integer::sum);
    }

    static int numberOfTransfers(List<OrbitRelationship> orbits, String from, String to) {
        String source = orbits.stream().filter(orbit -> orbit.orbiting.equals(from)).findFirst().orElseThrow().center;
        String target = orbits.stream().filter(orbit -> orbit.orbiting.equals(to)).findFirst().orElseThrow().center;
        return findDistance(orbits, source, target);
    }

    private static int findDistance(List<OrbitRelationship> orbits, String source, String target) {
        Map<String, Integer> distances = new HashMap<>();
        distances.put(source, 0);
        Queue<String> queue = new LinkedList<>();
        queue.add(source);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            Integer currentDistance = distances.get(current);
            if (current.equals(target)) {
                return currentDistance;
            } else {
                orbits.stream()
                        .filter(orbit -> orbit.contains(current))
                        .map(orbit -> orbit.center.equals(current) ? orbit.orbiting : orbit.center)
                        .filter(planet -> !distances.containsKey(planet))
                        .forEach(planet -> {
                            distances.put(planet, currentDistance + 1);
                            queue.add(planet);
                        });
            }
        }
        throw new IllegalArgumentException("target planet not found!");
    }

    static class OrbitRelationship {
        private String center;
        private String orbiting;

        static OrbitRelationship fromString(String relationship) {
            String[] split = relationship.split("\\)");
            if (split.length != 2) throw new IllegalArgumentException("Expected orbit relationship format is AAA)BBB");
            return new OrbitRelationship(split[0], split[1]);
        }

        OrbitRelationship(String center, String orbiting) {
            this.center = center;
            this.orbiting = orbiting;
        }

        boolean contains(String planet) {
            return center.equals(planet) || orbiting.equals(planet);
        }

        @Override
        public String toString() {
            return center + ")" + orbiting;
        }
    }

}
