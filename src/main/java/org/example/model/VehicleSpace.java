package org.example.model;

public class VehicleSpace {
    private int spaceNumber;
    private boolean isAvailable;
    private String vehicleType;
    private Vehicle vehicle;

    public VehicleSpace(int spaceNumber, String vehicleType) {
        this.spaceNumber = spaceNumber;
        this.vehicleType = vehicleType;
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void assignVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.isAvailable = false;
    }

    public void removeVehicle() {
        this.vehicle = null;
        this.isAvailable = true;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
