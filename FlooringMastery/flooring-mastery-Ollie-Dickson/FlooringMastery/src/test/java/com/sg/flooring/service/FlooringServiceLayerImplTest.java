package com.sg.flooring.service;

import com.sg.flooring.dao.FlooringDao;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringServiceLayerImplTest {

    private FlooringServiceLayer testService;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("com.sg.flooring.service");
        ctx.refresh();
        testService = ctx.getBean("flooringServiceLayerImpl", FlooringServiceLayerImpl.class);
    }

    @Test
    public void getOrdersTest() throws Exception {
        Order order1 = new Order(1, "Swiper", "WA", new BigDecimal("6.00"), "Wood",
                new BigDecimal("333.30"), new BigDecimal("5.15"), new BigDecimal("4.75"), new BigDecimal("1716.50"),
                new BigDecimal("1583.18"), new BigDecimal("197.98"), new BigDecimal("3497.66"), "Washington");
        Order order2 = new Order(2, "Boots", "KY", new BigDecimal("6.00"), "Laminate",
                new BigDecimal("101.56"), new BigDecimal("1.75"), new BigDecimal("2.10"), new BigDecimal("177.73"),
                new BigDecimal("213.28"), new BigDecimal("23.46"), new BigDecimal("414.47"), "Kentucky");

        List<Order> orders = testService.getOrders(LocalDate.parse("01012022", DateTimeFormatter.ofPattern("MMddyyyy")));
        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    public void checkStateNameTest() throws Exception {
        assertTrue(testService.checkStateName("Texas"));
        assertFalse(testService.checkStateName("Ohio"));
    }

    @Test
    public void createOrderTest() throws Exception {
        Order order6 = new Order(6, "Dora", "TX", new BigDecimal("4.45"), "Carpet",
                new BigDecimal("224.00"), new BigDecimal("2.25"), new BigDecimal("2.10"), new BigDecimal("504.00"),
                new BigDecimal("470.40"), new BigDecimal("43.36"), new BigDecimal("1017.76"), "Texas");

        Order newOrder = testService.createOrder("Dora", "Texas", "Carpet", new BigDecimal("224.00"), 6);
        testService.addOrder(newOrder, LocalDate.now());
        List<Order> orders = testService.getOrders(LocalDate.parse("01012022", DateTimeFormatter.ofPattern("MMddyyyy")));

        assertEquals(3, orders.size());
        assertTrue(orders.contains(order6));
    }
}