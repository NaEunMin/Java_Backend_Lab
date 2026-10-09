package com.api.java.collection.set;

import java.util.HashSet;
import java.util.Set;

public class CouponIssueRegistry {
    private final Set<CouponIssueKey> issuedCoupons = new HashSet<>();

    public boolean issue(Long memberId, Long couponId) {
        return issuedCoupons.add(new CouponIssueKey(memberId, couponId));
    }

    public boolean isIssued(Long memberId, Long couponId) {
        return issuedCoupons.contains(new CouponIssueKey(memberId, couponId));
    }

    public int count(){
        return issuedCoupons.size();
    }

}
