package java.collection.set;

import java.api.java.collection.set.CouponIssueRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CouponIssueRegistryTest {
    private final CouponIssueRegistry registry = new CouponIssueRegistry();

   @Test
    public void 멤버ID와_쿠폰ID는_null이_될_수_없다() {
        assertThrows(IllegalArgumentException.class, () -> {
            registry.issue(null, 1L);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            registry.issue(1L, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            registry.issue(null, null);
        });
    }

    @Test
    public void 처음_쿠폰을_발급하면_true_를_반환한다() {
        assertTrue(registry.issue(1L, 1L));
    }

    @Test
    public void 같은_회원에게_같은_쿠폰을_발급하면_false를_반환한다() {
        registry.issue(1L, 1L);
        assertFalse(registry.issue(1L, 1L));
    }

    @Test
    public void 중복_발급_후에도_발급기록은_1개이다() {
        registry.issue(1L, 1L);
        registry.issue(1L, 1L);
        assertEquals(1, registry.count());
    }

    @Test
    public void 같은_회원에게_서로_다른_쿠폰을_발급할_수_있다() {
        registry.issue(1L, 1L);
        assertTrue(registry.issue(1L, 2L));
    }

    @Test
    public void 서로_다른_회원에게_같은_쿠폰을_발급할_수_있다() {
        registry.issue(1L, 1L);
        assertTrue(registry.issue(2L, 1L));
    }

    @Test
    public void 발급된_쿠폰은_true로_반환한다() {
        registry.issue(1L, 1L);
        assertTrue(registry.isIssued(1L, 1L));
    }

    @Test
    public void 발급되지_않은_쿠폰은_false를_반환한다() {
        registry.issue(1L, 1L);
        assertFalse(registry.isIssued(1L, 2L));
    }
}
