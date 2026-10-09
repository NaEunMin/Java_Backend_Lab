package java.api.java.payment.exception;

public class OrderPaymentException extends RuntimeException {
  private final long orderId;

    public OrderPaymentException(long orderId, Throwable cause) {
      super("주문 결제에 실패했습니다. 주문Id : " + orderId, cause);
      this.orderId = orderId;
    }

    public long getOrderId() {
      return orderId;
    }
}
