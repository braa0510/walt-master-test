package com.walt.model;

import java.util.List;

public class DriverDistanceImpl implements DriverDistance {


    private Driver driver;
    private List<Delivery> driverDeliveriesList;

    public DriverDistanceImpl(Driver driver, List<Delivery> driverDeliveriesList) {
        this.driver = driver;
        this.driverDeliveriesList = driverDeliveriesList;
    }

    @Override
    public Driver getDriver() {
        return driver;
    }

    @Override
    public Long getTotalDistance() {
        return driverDeliveriesList.stream()
                .reduce(0L, (partialDistanceResult, delivery) -> partialDistanceResult + (long)delivery.getDistance(), Long::sum);
    }
}
