package com.walt;

import com.walt.dao.*;
import com.walt.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaltServiceImpl implements WaltService {


    private static final int MAX_DISTANCE = 21;
    private static final int MIN_DISTANCE = 0;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    CustomerRepository customerRepository;


    @Autowired
    DeliveryRepository deliveryRepository;


    @Autowired
    DriverRepository driverRepository;


    @Autowired
    RestaurantRepository restaurantRepository;

    private static final Logger log = LoggerFactory.getLogger(WaltServiceImpl.class);


      /*Unfortunately I did not receive answers to the questions I sent you
     so I solved according to what I thought was appropriate*/

    @Override
    public Delivery createOrderAndAssignDriver(Customer customer, Restaurant restaurant, Date deliveryTime) {
        if (!customer.getCity().equals(restaurant.getCity())) {
            log.error("Customer and restaurant are not in the same city");
            return null;
        } else {
            List<Driver> availableDrivers = driverRepository.findAllDriversByCity(customer.getCity());
            if (null == availableDrivers || availableDrivers.isEmpty()) {
                log.error("There is no available in this city:{}", customer.getCity());
                return null;
            }

            List<Delivery> currentDeliveries = (List<Delivery>) deliveryRepository.findAll();
            List<Driver> suitableDrivers = availableDrivers.stream().filter(driver -> currentDeliveries
                    .stream()
                    .filter(delivery -> delivery.getDriver().equals(driver)).noneMatch(delivery -> delivery.getDeliveryTime().equals(deliveryTime))).collect(Collectors.toList());

            if (suitableDrivers.isEmpty()) {
                log.error("There is no suitable drivers for the city:{}/DeliveryTime:{} combination", customer.getCity(), deliveryTime);
                return null;
            }

            Optional<Driver> driver = suitableDrivers.stream().min((driver1, driver2) -> {
                long driverOneDeliveriesCount = currentDeliveries.stream().filter((delivery -> delivery.getDriver().equals(driver1))).count();
                long driverTwoDeliveriesCount = currentDeliveries.stream().filter((delivery -> delivery.getDriver().equals(driver2))).count();
                return Long.compare(driverOneDeliveriesCount, driverTwoDeliveriesCount);
            });

            Driver driverWithLessDeliveries = driver.get();
            SecureRandom secureRandom = new SecureRandom();
            Delivery deliv = new Delivery(driverWithLessDeliveries, restaurant, customer, deliveryTime);
            deliv.setDistance(secureRandom.nextInt(MAX_DISTANCE - MIN_DISTANCE) + MIN_DISTANCE);
            return deliveryRepository.save(deliv);

        }

    }


    @Override
    public List<DriverDistance> getDriverRankReport() {
        List<Driver> driversList = (List<Driver>) driverRepository.findAll();
        return getDriversRanking(driversList);
    }

    @Override
    public List<DriverDistance> getDriverRankReportByCity(City city) {
        List<Driver> driversList = driverRepository.findAllDriversByCity(city);
        return getDriversRanking(driversList);
    }

    private List<DriverDistance> getDriversRanking(List<Driver> driversList) {
        List<Delivery> deliveries = (List<Delivery>) deliveryRepository.findAll();
        return driversList.stream()
                .map((driver -> new DriverDistanceImpl(driver, deliveries.stream().filter(delivery -> delivery.getDriver().equals(driver)).collect(Collectors.toList()))))
                .sorted((f1, f2) -> Long.compare(f2.getTotalDistance(), f1.getTotalDistance())).collect(Collectors.toList());

    }
}
