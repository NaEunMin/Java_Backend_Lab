package java.api;

import java.api.java.policy.ShippingService;
import java.api.java.policy.StandardPolicy;

public class Main {
    public static void main(String[] args) {
        ShippingService shippingService = new ShippingService(new StandardPolicy());

        System.out.println(shippingService.calculateFee(50000));
    }
}