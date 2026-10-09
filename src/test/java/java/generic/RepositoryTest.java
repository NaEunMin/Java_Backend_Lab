package java.generic;

import java.api.java.generic.Repository;
import java.api.java.generic.SingleValueRepository;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    @Test
    public void String_Long타입으로_repository에_데이터를_저장한다(){
        //given
        Repository<String, Long> repository = new SingleValueRepository<>();

        //when
        repository.save(1L, "member1");
        String member = repository.findById(1L);

        //then
        assertEquals("member1", member);
    }

    @Test
    public void Integer_String타입으로_repository에_데이터를_저장한다(){

        //given
        Repository<Integer, String> repository = new SingleValueRepository<>();

        //when
        repository.save("PRODUCT_A", 1);
        Integer product = repository.findById("PRODUCT_A");

        //then
        assertEquals(1,product);
    }

    @Test
    public void 저장되지_않은_ID로_조회시_예외를_반환한다(){

        //given
        Repository<String, Long> repository = new SingleValueRepository<>();

        //when
        repository.save(1L, "member1");

        //then
        assertThrows(NoSuchElementException.class, () -> {
            repository.findById(2L);
        }, "저장되지 않는 ID로 조회했다면 NoSuchElementException이 발생해야합니다.");
    }

    @Test
    public void 아무것도_저장되지_않은_저장소를_조회시_예외를_반환한다(){

        //given
        Repository<String, Long> repository = new SingleValueRepository<>();


        //when


        //then
        assertThrows(NoSuchElementException.class, () -> {
            repository.findById(1L);
        });
    }

    @Test
    public void 빈_저장소를_null로_조회를_시도하면_예외를_반환한다(){

        //given
        Repository<String, Long> repository = new SingleValueRepository<>();

        assertThrows(NoSuchElementException.class, () -> {
            repository.findById(null);
        });
    }
}
