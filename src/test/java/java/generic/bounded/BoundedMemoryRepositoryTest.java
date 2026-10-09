package java.generic.bounded;

import java.api.java.generic.bounded.BoundedMemoryRepository;
import java.api.java.stream.advanced.OrderItem;
import java.api.java.stream.advanced.PurchaseOrder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class BoundedMemoryRepositoryTest {

    private final BoundedMemoryRepository<PurchaseOrder, Long> repository = new BoundedMemoryRepository<>();

    @Test //이 테스트가 저장과 조회를 동시에 증명함
    public void 저장소에_entity를_저장한다() {
        PurchaseOrder order = new PurchaseOrder(1L, List.of());

        repository.save(order);

        assertEquals(order, repository.findById(order.getId()));
    }

    @Test
    public void 서로_다른_ID로_각각_entity를_저장한다() {
        PurchaseOrder order1 = new PurchaseOrder(1L, List.of());
        PurchaseOrder order2 = new PurchaseOrder(2L, List.of());

        repository.save(order1);
        repository.save(order2);

        assertEquals(order1, repository.findById(order1.getId()));
        assertEquals(order2, repository.findById(order2.getId()));
    }

    @Test //HashMap이 자동으로 같은 ID면 덮어쓰기를 하는지 확인이 필요
    public void 같은_ID로_저장_시_기존의_entity를_교체한다() {
        PurchaseOrder order1 = new PurchaseOrder(1L, List.of());
        repository.save(order1);

        OrderItem item = new OrderItem("키보드", 10_000, 1);
        PurchaseOrder order2 = new PurchaseOrder(1L, List.of(item));
        repository.save(order2);

        assertSame(order2, repository.findById(1L));
    }

    @Test
    public void 존재하지_않는_ID로_조회_시_예외를_반환한다() {
        PurchaseOrder order1 = new PurchaseOrder(1L, List.of());
        repository.save(order1);

        assertThrows(NoSuchElementException.class, () ->
                repository.findById(2L));
    }

    @Test
    public void null인_ID로_조회_시_예외를_반환한다(){
        assertThrows(IllegalArgumentException.class, () ->
                repository.findById(null));
    }

    @Test
    public void 저장할_entity가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.save(null));
    }

    @Test
    public void 저장할_entityId가_null이면_예외를_반환한다() {
        PurchaseOrder order = new PurchaseOrder(null, List.of());

        assertThrows(IllegalArgumentException.class, () ->
                repository.save(order));
    }
}
