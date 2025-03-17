/**
 * Main class that demonstrates the Rally Championship Management System.
 */

 package main;

 import java.util.List;
 
 public class Main {
     public static void main(String[] args) {
         // Get the Championship Manager instance (Singleton)
         ChampionshipManager manager = ChampionshipManager.getInstance();
         
         // Create drivers
         Driver ogier = new Driver("Sébastien Ogier", "France");
         Driver tanak = new Driver("Ott Tänak", "Estonia");
         Driver rovanpera = new Driver("Kalle Rovanperä", "Finland");
         Driver neuville = new Driver("Thierry Neuville", "Belgium");
         
         // Create cars
         GravelCar gravelCar = new GravelCar("Toyota", "Yaris WRC", 380, 10.5, 9.2);
         AsphaltCar asphaltCar = new AsphaltCar("Hyundai", "i20 Coupe WRC", 380, 9.5, 9.8);
         
         // Assign cars to drivers
         ogier.setCar(gravelCar);
         tanak.setCar(gravelCar);
         rovanpera.setCar(asphaltCar);
         neuville.setCar(asphaltCar);
         
         // Register drivers with the championship
         manager.registerDriver(ogier);
         manager.registerDriver(tanak);
         manager.registerDriver(rovanpera);
         manager.registerDriver(neuville);
         
         // Simulate Rally Finland race
         RallyRaceResult finlandRally = new RallyRaceResult("Rally Finland", "Jyväskylä");
         finlandRally.recordResult(ogier, 1);
         finlandRally.recordResult(tanak, 2);
         finlandRally.recordResult(rovanpera, 3);
         finlandRally.recordResult(neuville, 4);
         manager.addRaceResult(finlandRally);
         
         // Switch cars for the next race
         ogier.setCar(asphaltCar);
         rovanpera.setCar(gravelCar);
         
         // Simulate Monte Carlo Rally
         RallyRaceResult monteCarloRally = new RallyRaceResult("Monte Carlo Rally", "Monaco");
         monteCarloRally.recordResult(rovanpera, 1);
         monteCarloRally.recordResult(neuville, 2);
         monteCarloRally.recordResult(ogier, 3);
         monteCarloRally.recordResult(tanak, 4);
         manager.addRaceResult(monteCarloRally);
         
         // Display championship standings
         System.out.println("Championship Standings:");
         List<Driver> standings = manager.getChampionshipStandings();
         int position = 1;
         for (Driver driver : standings) {
             System.out.println(position + ". " + driver);
             position++;
         }
         
         // Display championship leader
         System.out.println("\n===== CHAMPIONSHIP LEADER =====");
         Driver leader = manager.getLeadingDriver();
         System.out.println(leader.getName() + " with " + leader.getTotalPoints() + " points");
         
         // Display championship statistics
         System.out.println("\n===== CHAMPIONSHIP STATISTICS =====");
         System.out.println("Total Drivers: " + ChampionshipManager.getTotalDrivers());
         System.out.println("Total Races: " + ChampionshipManager.getTotalRaces());
         System.out.printf("Average Points Per Driver: %.2f%n", 
                 ChampionshipStatistics.calculateAveragePointsPerDriver(manager));
         System.out.println("Most Successful Country: " + 
                 ChampionshipStatistics.findMostSuccessfulCountry(manager));
         System.out.println("Total Championship Points: " + 
                 manager.calculateTotalChampionshipPoints());
         
         // Display race results
         System.out.println("\n===== RACE RESULTS =====");
         for (RaceResult result : manager.getRaceResults()) {
             System.out.println(result);
         }
         
         // Display car performance ratings
         System.out.println("===== CAR PERFORMANCE RATINGS =====");
         System.out.println("Gravel Car Performance: " + gravelCar.calculatePerformance());
         System.out.println("Asphalt Car Performance: " + asphaltCar.calculatePerformance());
     }
 }