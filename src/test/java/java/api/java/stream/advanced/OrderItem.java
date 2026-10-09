package java.api.java.stream.advanced;

public class OrderItem {
    private final String productName;
    private final int price;
    private final int quantity;

    public OrderItem(String productName, int price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }
}
