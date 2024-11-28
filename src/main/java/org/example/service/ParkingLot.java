package org.example.service;


import org.example.config.ParkinglotInterface;
import org.example.model.Floor;
import org.example.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ParkingLot  implements ParkinglotInterface {
    public List<Floor> floors;
    private CostStrategy costStrategy;

    public ParkingLot() {
        this.floors = new ArrayList<>();
    }

    public void init(int floorsCount, Map<String, Integer> vehicleSpacesPerFloor) {
        for (int i = 1; i <= floorsCount; i++) {
            floors.add(new Floor(i, vehicleSpacesPerFloor));
        }
    }

    public void configureCostStrategy(Map<String, Integer> rates, String currency) {
        this.costStrategy = new CostStrategy(rates, currency);
    }

    public String addVehicle(Vehicle vehicle, long entryTimestamp) {
        for (Floor floor : floors) {
            if (floor.hasSpace(vehicle.getType())) {
                String tokenId = UUID.randomUUID().toString();
                vehicle.setTokenId(tokenId);
                vehicle.setEntryTimestamp(entryTimestamp);
                floor.parkVehicle(vehicle);
                return tokenId;
            }
        }
        throw new IllegalStateException("Parking lot is full for vehicle type: " + vehicle.getType());
    }

    public String removeVehicle(String tokenId, long exitTimestamp) {
        for (Floor floor : floors) {
            Vehicle vehicle = floor.findAndRemoveVehicle(tokenId);
            if (vehicle != null) {
                long hoursParked = Math.max(1, (exitTimestamp - vehicle.getEntryTimestamp()) / 3600000);
                int cost = costStrategy.calculateCost(vehicle.getType(), hoursParked);
                return "model.Vehicle Removed: " + vehicle + "\nTotal Cost: " + cost + " " + costStrategy.getCurrency();
            }
        }
        throw new IllegalArgumentException("Invalid token ID.");
    }

    public int checkAvailability(int floorNumber, String vehicleType) {
        return floors.get(floorNumber - 1).getAvailableSpaces(vehicleType);
    }

    public void displayStatus() {
        for (Floor floor : floors) {
            System.out.println(floor);
        }
    }
}
