package org.example;

import org.example.config.ParkinglotInterface;
import org.example.model.Vehicle;
import org.example.service.ParkingLot;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ParkinglotInterface parkingLot = (ParkinglotInterface) new ParkingLot();
        parkingLot.init(2, Map.of("Car", 2, "Bike", 3, "Truck", 1));
        parkingLot.configureCostStrategy(Map.of("Car", 20, "Bike", 10, "Truck", 30), "₹");

        try {
            String token1 = parkingLot.addVehicle(new Vehicle("Car", "KA01AB1234", "Red"), System.currentTimeMillis());
            String token2 = parkingLot.addVehicle(new Vehicle("Car", "KA02CD5678", "Blue"), System.currentTimeMillis());
            System.out.println("Tokens issued: " + token1 + ", " + token2);

            parkingLot.displayStatus();

            System.out.println(parkingLot.removeVehicle(token1, System.currentTimeMillis() + 7200000));
            parkingLot.displayStatus();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ;
    
    }
}