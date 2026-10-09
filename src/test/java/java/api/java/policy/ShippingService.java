package java.api.java.policy;

public class ShippingService {

    private final GradePolicy gradePolicy;

    public ShippingService(GradePolicy gradePolicy) {
        this.gradePolicy = gradePolicy;
    }

    public int calculateFee(int orderAmount) {
        return gradePolicy.amountPolicy(orderAmount);
    }
}