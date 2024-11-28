package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Floor {
    private int floorNumber;
    private Map<String, List<VehicleSpace>> spaces;

    public Floor(int floorNumber, Map<String, Integer> vehicleSpacesPerFloor) {
        this.floorNumber = floorNumber;
        this.spaces = new HashMap<>();
        for (Map.Entry<String, Integer> entry : vehicleSpacesPerFloor.entrySet()) {
            String vehicleType = entry.getKey();
            int capacity = entry.getValue();
            List<VehicleSpace> spaceList = new ArrayList<>();
            for (int i = 1; i <= capacity; i++) {
                spaceList.add(new VehicleSpace(i, vehicleType));
            }
            spaces.put(vehicleType, spaceList);
        }
    }

    public boolean hasSpace(String vehicleType) {
        return spaces.containsKey(vehicleType) && getAvailableSpaces(vehicleType) > 0;
    }

    public void parkVehicle(Vehicle vehicle) {
        List<VehicleSpace> spaceList = spaces.get(vehicle.getType());
        for (VehicleSpace space : spaceList) {
            if (space.isAvailable()) {
                space.assignVehicle(vehicle);
                return;
            }
        }
    }

    public Vehicle findAndRemoveVehicle(String tokenId) {
        for (List<VehicleSpace> spaceList : spaces.values()) {
            for (VehicleSpace space : spaceList) {
                if (!space.isAvailable() && space.getVehicle().getTokenId().equals(tokenId)) {
                    Vehicle vehicle = space.getVehicle();
                    space.removeVehicle();
                    return vehicle;
                }
            }
        }
        return null;
    }

    public int getAvailableSpaces(String vehicleType) {
        return (int) spaces.get(vehicleType).stream().filter(VehicleSpace::isAvailable).count();
    }

    @Override
    public String toString() {
        StringBuilder status = new StringBuilder("model.Floor " + floorNumber + ":\n");
        for (Map.Entry<String, List<VehicleSpace>> entry : spaces.entrySet()) {
            String vehicleType = entry.getKey();
            List<VehicleSpace> spaceList = entry.getValue();
            long available = spaceList.stream().filter(VehicleSpace::isAvailable).count();
            status.append(vehicleType).append(" - Available: ").append(available)
                    .append(", Occupied: ").append(spaceList.size() - available).append("\n");
        }
        return status.toString();
    }
}
