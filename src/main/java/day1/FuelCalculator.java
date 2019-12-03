package day1;

class FuelCalculator {
    static int calculateFuelForModule(int mass) {
        return (mass / 3) - 2;
    }

    static int calculateFuelForModuleAndFuel(int mass) {
        int fuelForModule = (mass / 3) - 2;
        if (fuelForModule < 0) {
            return 0;
        }
        return fuelForModule + calculateFuelForModuleAndFuel(fuelForModule);
    }
}
