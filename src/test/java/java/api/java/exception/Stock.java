package java.api.java.exception;

public class Stock {
    private int quantity;

    public Stock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("초기 재고 수량은 0이상이어야 합니다.");
        }
        this.quantity = quantity;
    }

    public void decrease(int amount) {
        if(amount <= 0){
            throw new IllegalArgumentException("요청 재고는 1 이상이어야 합니다");
        }

        if (quantity < amount) {
            throw new InsufficientStockException(
                    quantity,
                    amount
            );
        }
        quantity -= amount;
    }

    public int getQuantity() {
        return quantity;
    }
}
