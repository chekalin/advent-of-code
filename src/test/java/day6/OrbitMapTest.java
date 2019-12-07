package day6;

import day6.OrbitMap.OrbitRelationship;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static util.FileHelper.getScanner;

class OrbitMapTest {

    @Test
    void twoPlanetsHaveOneOrbit() {
        List<OrbitRelationship> orbits = List.of(OrbitRelationship.fromString("AAA)BBB"));
        int result = OrbitMap.totalNumberOfOrbits(orbits);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void threePlanetsWithDirectOrbitsOnly() {
        List<OrbitRelationship> orbits = List.of(
                OrbitRelationship.fromString("AAA)BBB"),
                OrbitRelationship.fromString("AAA)CCC")
        );
        int result = OrbitMap.totalNumberOfOrbits(orbits);
        assertThat(result).isEqualTo(2);
    }

    @Test
    void threePlanetsWithIndirectOrbit() {
        List<OrbitRelationship> orbits = List.of(
                OrbitRelationship.fromString("AAA)BBB"),
                OrbitRelationship.fromString("BBB)CCC") // CCC directly orbits BBB and indirectly AAA
        );
        int result = OrbitMap.totalNumberOfOrbits(orbits);
        assertThat(result).isEqualTo(3);
    }

    @Test
    void testCase() {
        List<OrbitRelationship> orbits = List.of(
                OrbitRelationship.fromString("COM)B"),
                OrbitRelationship.fromString("B)C"),
                OrbitRelationship.fromString("C)D"),
                OrbitRelationship.fromString("D)E"),
                OrbitRelationship.fromString("E)F"),
                OrbitRelationship.fromString("B)G"),
                OrbitRelationship.fromString("G)H"),
                OrbitRelationship.fromString("D)I"),
                OrbitRelationship.fromString("E)J"),
                OrbitRelationship.fromString("J)K"),
                OrbitRelationship.fromString("K)L")
        );
        int result = OrbitMap.totalNumberOfOrbits(orbits);
        assertThat(result).isEqualTo(42);
    }

    @Test
    void minimumNumberOfTransfersIsZeroIfWeAreAlreadyOrbitingTheTarget() {
        List<OrbitRelationship> orbits = List.of(
                OrbitRelationship.fromString("AAA)BBB"),
                OrbitRelationship.fromString("AAA)CCC")
        );
        int result = OrbitMap.numberOfTransfers(orbits, "BBB", "CCC");
        assertThat(result).isEqualTo(0);
    }

    @Test
    void minimumNumberOfTransfersIsOneIfWeNeedToMakeAHopBetweenOrbits() {
        /*
         * AAA - YOU
         *     \
         *       CCC - SAN
         *
         * One hop AAA -> CCC
         * */

        List<OrbitRelationship> orbits = List.of(
                OrbitRelationship.fromString("AAA)YOU"),
                OrbitRelationship.fromString("AAA)CCC"),
                OrbitRelationship.fromString("CCC)SAN")
        );
        int result = OrbitMap.numberOfTransfers(orbits, "YOU", "SAN");
        assertThat(result).isEqualTo(1);
    }

    @Test
    void minimumNumberOfTransfersTestCase() {
        List<OrbitRelationship> orbits = List.of(
                OrbitRelationship.fromString("COM)B"),
                OrbitRelationship.fromString("B)C"),
                OrbitRelationship.fromString("C)D"),
                OrbitRelationship.fromString("D)E"),
                OrbitRelationship.fromString("E)F"),
                OrbitRelationship.fromString("B)G"),
                OrbitRelationship.fromString("G)H"),
                OrbitRelationship.fromString("D)I"),
                OrbitRelationship.fromString("E)J"),
                OrbitRelationship.fromString("J)K"),
                OrbitRelationship.fromString("K)L"),
                OrbitRelationship.fromString("K)YOU"),
                OrbitRelationship.fromString("I)SAN")
        );
        int result = OrbitMap.numberOfTransfers(orbits, "YOU", "SAN");
        assertThat(result).isEqualTo(4);
    }

    @Test
    void assignment() {
        List<OrbitRelationship> orbits = readOrbitsFromFile();
        int result1 = OrbitMap.totalNumberOfOrbits(orbits);
        System.out.println("result1 = " + result1);
        int result2 = OrbitMap.numberOfTransfers(orbits, "YOU", "SAN");
        System.out.println("result2 = " + result2);
    }

    private List<OrbitRelationship> readOrbitsFromFile() {
        Scanner scanner = getScanner("day6/input.txt");
        List<OrbitRelationship> orbits = new LinkedList<>();
        while (scanner.hasNextLine()) {
            orbits.add(OrbitRelationship.fromString(scanner.nextLine()));
        }
        return orbits;
    }
}