package com.walt;

import com.walt.dao.*;
import com.walt.model.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WaltTest {

    public WaltTest() {
    }

    @TestConfiguration
    static class WaltServiceImplTestContextConfiguration {

        @Bean
        public WaltService waltService() {
            return new WaltServiceImpl();
        }
    }

    @Autowired
    WaltService waltService;

    @Resource
    CityRepository cityRepository;

    @Resource
    CustomerRepository customerRepository;

    @Resource
    DriverRepository driverRepository;

    @Resource
    DeliveryRepository deliveryRepository;

    @Resource
    RestaurantRepository restaurantRepository;

    @BeforeEach()
    public void prepareData() {

        City jerusalem = new City("Jerusalem");
        City tlv = new City("Tel-Aviv");
        City bash = new City("Beer-Sheva");
        City haifa = new City("Haifa");

        cityRepository.save(jerusalem);
        cityRepository.save(tlv);
        cityRepository.save(bash);
        cityRepository.save(haifa);

        createDrivers(jerusalem, tlv, bash, haifa);

        createCustomers(jerusalem, tlv, haifa,bash);

        createRestaurant(jerusalem, tlv, haifa,bash);
    }

    private void createRestaurant(City jerusalem, City tlv, City haifa,City bash ) {
        Restaurant meat = new Restaurant("meat", jerusalem, "All meat restaurant");
        Restaurant vegan = new Restaurant("vegan", tlv, "Only vegan");
        Restaurant cafe = new Restaurant("cafe", tlv, "Coffee shop");
        Restaurant chinese = new Restaurant("chinese", tlv, "chinese restaurant");
        Restaurant mexican = new Restaurant("restaurant", tlv, "mexican restaurant ");
        Restaurant middleEastern = new Restaurant("middleEastern", haifa, "middleEastern restaurant ");
        Restaurant kfc = new Restaurant("kfc", bash, "fast food restaurant");
        Restaurant italian = new Restaurant("italian", bash, "italian food restaurant");
        restaurantRepository.saveAll(Lists.newArrayList(meat, vegan, cafe, chinese, mexican,middleEastern,kfc,italian));
    }

    private void createCustomers(City jerusalem, City tlv, City haifa,City bash) {
        Customer beethoven = new Customer("Beethoven", tlv, "Ludwig van Beethoven");
        Customer mozart = new Customer("Mozart", jerusalem, "Wolfgang Amadeus Mozart");
        Customer chopin = new Customer("Chopin", haifa, "Frédéric François Chopin");
        Customer rachmaninoff = new Customer("Rachmaninoff", tlv, "Sergei Rachmaninoff");
        Customer bach = new Customer("Bach", tlv, "Sebastian Bach. Johann");
        Customer bara = new Customer("Bara", bash, "bara takrori");
        Customer daniel = new Customer("Daniel", bash, "daniel daniel");

        customerRepository.saveAll(Lists.newArrayList(beethoven, mozart, chopin, rachmaninoff, bach,bara,daniel));
    }

    private void createDrivers(City jerusalem, City tlv, City bash, City haifa) {
        Driver mary = new Driver("Mary", tlv);
        Driver patricia = new Driver("Patricia", tlv);
        Driver jennifer = new Driver("Jennifer", haifa);
        Driver james = new Driver("James", bash);
        Driver john = new Driver("John", bash);
        Driver robert = new Driver("Robert", jerusalem);
        Driver david = new Driver("David", jerusalem);
        Driver daniel = new Driver("Daniel", tlv);
        Driver noa = new Driver("Noa", haifa);
        Driver ofri = new Driver("Ofri", haifa);
        Driver nata = new Driver("Neta", jerusalem);

        driverRepository.saveAll(Lists.newArrayList(mary, patricia, jennifer, james, john, robert, david, daniel, noa, ofri, nata));
    }


    @Test
    public void testBasics() {

        assertEquals(((List<City>) cityRepository.findAll()).size(), 4);
        assertEquals((driverRepository.findAllDriversByCity(cityRepository.findByName("Beer-Sheva")).size()), 2);
    }


    @Test
    public void createNewDeliveryPositiveJerusalem() {

        City city = cityRepository.findByName("Jerusalem");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertEquals(deliveryRepository.findById(delivery.getId()).get(), delivery);

    }


    @Test
    public void createNewDeliveryPositiveTelAviv() {
        City city = cityRepository.findByName("Tel-Aviv");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertEquals(deliveryRepository.findById(delivery.getId()).get(), delivery);
    }


    @Test
    public void createNewDeliveryPositiveBeerSheva() {

        City city = cityRepository.findByName("Beer-Sheva");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertEquals(deliveryRepository.findById(delivery.getId()).get(), delivery);
    }

    @Test
    public void createNewDeliveryPositiveHaifa() {
        City city = cityRepository.findByName("Haifa");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertEquals(deliveryRepository.findById(delivery.getId()).get(), delivery);
    }
    @Test
    public void createNewDeliveryInHaifaWithCustomerAndResturantInDifferentCity() {
        City city = cityRepository.findByName("Haifa");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> !restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertNull(delivery);
    }
    @Test
    public void createNewDeliveryInTelAvivaWithCustomerAndResturantInDifferentCity() {
        City city = cityRepository.findByName("Tel-Aviv");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> !restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertNull(delivery);
    }
    @Test
    public void createNewDeliveryInBeerShevaaWithCustomerAndResturantInDifferentCity() {
        City city = cityRepository.findByName("Beer-Sheva");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> !restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertNull(delivery);
    }
    @Test
    public void createNewDeliveryInJerusalemaWithCustomerAndResturantInDifferentCity() {
        City city = cityRepository.findByName("Jerusalem");
        Customer customer = ((List<Customer>) customerRepository.findAll()).stream().filter(customer1 -> customer1.getCity().equals(city)).findAny().get();
        Restaurant restaurant = ((List<Restaurant>) restaurantRepository.findAll()).stream().filter(restaurant1 -> !restaurant1.getCity().equals(city)).findAny().get();
        Date date = new Date();
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertNull(delivery);
    }

    @Test
    public void testDriverRankReport(){
        List<DriverDistance> driverDistanceList=(List<DriverDistance>) waltService.getDriverRankReport();
    }
    @Test
    public void testDriverRankReportInHaifa(){
        List<DriverDistance> driverDistanceList=(List<DriverDistance>) waltService.getDriverRankReportByCity(cityRepository.findByName("Haifa"));
    }
    @Test
    public void testDriverRankReportInBeerSheva(){
        List<DriverDistance> driverDistanceList=(List<DriverDistance>) waltService.getDriverRankReportByCity(cityRepository.findByName("Beer-Sheva"));
    }
    @Test
    public void testDriverRankReportInTelAviv(){
        List<DriverDistance> driverDistanceList=(List<DriverDistance>) waltService.getDriverRankReportByCity(cityRepository.findByName("Tel-Aviv"));
    }
    @Test
    public void testDriverRankReportInJerusalem(){
        List<DriverDistance> driverDistanceList=(List<DriverDistance>) waltService.getDriverRankReportByCity(cityRepository.findByName("Jerusalem"));
    }


}
