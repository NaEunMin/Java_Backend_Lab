package java.api.java.nested;

public class OrderMessageService {
    private final String shopName;

    public OrderMessageService(String shopName) {
        this.shopName = shopName;
    }

    public String createMessage(long orderId) {
        String prefix = "[주문]";

        class MessageFormatter {
            public String format(){
                return prefix + " " + shopName + " 주문번호: " + orderId;
            }
        }

        MessageFormatter formatter = new MessageFormatter();
        return formatter.format();
    }
}
