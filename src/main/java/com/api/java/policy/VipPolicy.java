package com.api.java.policy;

public class VipPolicy implements GradePolicy {

    @Override
    public int amountPolicy(int orderAmount) {
        return 0;
    }
}
