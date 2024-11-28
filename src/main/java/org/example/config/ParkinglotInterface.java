package org.example.config;



import org.example.model.Vehicle;

import java.util.Map;

public interface ParkinglotInterface {
    public void init(int floorsCount, Map<String, Integer> vehicleSpacesPerFloor);
    public void configureCostStrategy(Map<String, Integer> rates, String currency);
    public String addVehicle(Vehicle vehicle, long entryTimestamp);
    public String removeVehicle(String tokenId, long exitTimestamp);
    public int checkAvailability(int floorNumber, String vehicleType);
    public void displayStatus();
}
