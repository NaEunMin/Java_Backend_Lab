package java.api.java.policy;

public class StandardPolicy implements GradePolicy {

    @Override
    public int amountPolicy(int orderAmount) {
        if(orderAmount >= 50000){
            return 0;
        }
        return 3000;
    }
}
