package day1;

import org.junit.jupiter.api.Test;
import util.FileHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class FuelCalculatorTest {

    @Test
    void calculatesFuelAccordingToFormula() {
        assertThat(FuelCalculator.calculateFuelForModule(12)).isEqualTo(2);
        assertThat(FuelCalculator.calculateFuelForModule(14)).isEqualTo(2);
        assertThat(FuelCalculator.calculateFuelForModule(1969)).isEqualTo(654);
        assertThat(FuelCalculator.calculateFuelForModule(100756)).isEqualTo(33583);
    }

    @Test
    void calculatesFuelForModuleAndFuelAccordingToFormula() {
        assertThat(FuelCalculator.calculateFuelForModuleAndFuel(0)).isEqualTo(0);
        assertThat(FuelCalculator.calculateFuelForModuleAndFuel(0)).isEqualTo(0);
        assertThat(FuelCalculator.calculateFuelForModuleAndFuel(12)).isEqualTo(2);
        assertThat(FuelCalculator.calculateFuelForModuleAndFuel(14)).isEqualTo(2);
        assertThat(FuelCalculator.calculateFuelForModuleAndFuel(1969)).isEqualTo(966);
        assertThat(FuelCalculator.calculateFuelForModuleAndFuel(100756)).isEqualTo(50346);
    }

    @Test
    void assignment() {
        Scanner scanner = FileHelper.getScanner("day1/input.txt");
        List<Integer> modules = new LinkedList<>();
        while (scanner.hasNextLine()) {
            modules.add(Integer.parseInt(scanner.nextLine()));
        }
        int fuelForModule = modules.stream().mapToInt(FuelCalculator::calculateFuelForModule).sum();
        System.out.println("fuelForModule = " + fuelForModule);
        int fuelForModuleAndFuel = modules.stream().mapToInt(FuelCalculator::calculateFuelForModuleAndFuel).sum();
        System.out.println("fuelForModuleAndFuel = " + fuelForModuleAndFuel);
    }

}