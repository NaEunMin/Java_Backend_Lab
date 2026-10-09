package java.api.java.collection.set;

import java.util.Objects;

public class CouponIssueKey {
    private final Long memberId;
    private final Long couponId;

    public CouponIssueKey(Long memberId, Long couponId) {
        if(memberId == null || couponId == null){
            throw new IllegalArgumentException("멤버ID와 쿠폰ID는 null이 될 수 없습니다.");
        }
        this.memberId = memberId;
        this.couponId = couponId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }

        if(!(o instanceof CouponIssueKey key)){
            return false;
        }

        return this.memberId.equals(key.memberId) && this.couponId.equals(key.couponId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, couponId);
    }
}
