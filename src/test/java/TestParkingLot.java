import org.example.model.Vehicle;
import org.example.service.CostStrategy;
import org.example.service.ParkingLot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestParkingLot {
    private ParkingLot parkingLot;

    @BeforeEach
    public void setUp() {
        parkingLot = new ParkingLot();
        parkingLot.init(2, Map.of("Car", 2, "Bike", 3, "Truck", 1));
        parkingLot.configureCostStrategy(Map.of("Car", 20, "Bike", 10, "Truck", 30), "₹");
    }

    @Test
    public void testAddVehicle_Success() {
        Vehicle car = new Vehicle("Car", "KA01AB1234", "Red");
        String token = parkingLot.addVehicle(car, System.currentTimeMillis());
        assertNotNull(token, "Token should be generated for the parked vehicle.");
    }

    @Test
    public void testRemoveVehicle_Success() {
        Vehicle bike = new Vehicle("Bike", "KA04GH3456", "Black");
        String token = parkingLot.addVehicle(bike, System.currentTimeMillis());
        long exitTime = System.currentTimeMillis() + 2 * 3600000; // 2 hours later

        String receipt = parkingLot.removeVehicle(token, exitTime);
        assertTrue(receipt.contains("Total Cost: 20 ₹"), "Receipt should calculate cost correctly.");
    }

    @Test
    public void testRemoveVehicle_InvalidToken() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                parkingLot.removeVehicle("INVALID_TOKEN", System.currentTimeMillis())
        );
        assertEquals("Invalid token ID.", exception.getMessage());
    }

    @Test
    public void testCheckAvailability() {
        assertEquals(2, parkingLot.checkAvailability(1, "Car"), "Initially, 2 car spaces should be available on Floor 1.");
        parkingLot.addVehicle(new Vehicle("Car", "KA01AB1234", "Red"), System.currentTimeMillis());
        assertEquals(1, parkingLot.checkAvailability(1, "Car"), "After parking one car, 1 space should be available on Floor 1.");
    }

    @Test
    public void testDisplayStatus() {
        Vehicle truck = new Vehicle("Truck", "KA05IJ7890", "White");
        parkingLot.addVehicle(truck, System.currentTimeMillis());
        String status = parkingLot.floors.get(0).toString(); // Accessing Floor 1 status
        assertTrue(status.contains("Truck - Available: 0, Occupied: 1"), "Status should reflect updated truck availability.");
    }

    @Test
    public void testCostCalculation() {
        CostStrategy costStrategy = new CostStrategy(Map.of("Car", 20, "Bike", 10, "Truck", 30), "₹");
        int cost = costStrategy.calculateCost("Car", 3);
        assertEquals(60, cost, "Cost for 3 hours for a car should be 60 ₹.");
    }

    @Test
    public void testMultipleVehicleTypes() {
        parkingLot.addVehicle(new Vehicle("Car", "KA01AB1234", "Red"), System.currentTimeMillis());
        parkingLot.addVehicle(new Vehicle("Bike", "KA06KL3456", "Blue"), System.currentTimeMillis());
        parkingLot.addVehicle(new Vehicle("Truck", "KA07MN7890", "Green"), System.currentTimeMillis());

        assertEquals(0, parkingLot.checkAvailability(1, "Truck"), "Truck space should be fully occupied.");
        assertEquals(2, parkingLot.checkAvailability(1, "Bike"), "2 Bike spaces should still be available.");
    }

    @Test
    public void testEdgeCase_MinimumParkingTime() {
        Vehicle bike = new Vehicle("Bike", "KA08OP9012", "Yellow");
        String token = parkingLot.addVehicle(bike, System.currentTimeMillis());
        long exitTime = System.currentTimeMillis() + 10 * 60 * 1000; // 10 minutes later (minimum charge is 1 hour)

        String receipt = parkingLot.removeVehicle(token, exitTime);
        assertTrue(receipt.contains("Total Cost: 10 ₹"), "Receipt should reflect the minimum cost for 1 hour.");
    }
}
