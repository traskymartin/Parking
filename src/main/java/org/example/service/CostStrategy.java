package org.example.service;

import java.util.Map;

public class CostStrategy {
    private Map<String, Integer> rates;
    private String currency;

    public CostStrategy(Map<String, Integer> rates, String currency) {
        this.rates = rates;
        this.currency = currency;
    }

    public int calculateCost(String vehicleType, long hours) {
        return rates.get(vehicleType) * (int) hours;
    }

    public String getCurrency() {
        return currency;
    }
}
