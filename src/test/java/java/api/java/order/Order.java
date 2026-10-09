package java.api.java.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Order {
    private final long orderId;
    private final List<String> products;

    public Order(long orderId, List<String> orderList) {
        this.orderId = orderId;
        this.products = new ArrayList<>(orderList);
    }

    public List<String> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
