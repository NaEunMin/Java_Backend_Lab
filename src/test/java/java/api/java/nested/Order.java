package java.api.java.nested;

public class Order {
    private final long orderId;
    private final int totalPrice;

    public Order(long orderId, int totalPrice) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
    }

    public static class DeliveryPolicy {
        private final int standardPrice;
        private final int basicDeliverFee;

        public DeliveryPolicy(int standardPrice, int basicDeliverFee) {
            this.standardPrice = standardPrice;
            this.basicDeliverFee = basicDeliverFee;
        }

        public int calculateFee(int orderPrice) {
            if(orderPrice >= standardPrice) {
                return 0;
            } else{
                return basicDeliverFee;
            }
        }
    }

    public class Receipt{
        public long getOrderId(){
            return orderId;
        }
        public int calculatePaymentAmount(int discountAmount){
            return totalPrice - discountAmount;
        }
    }
}
