package java.member;

import java.api.java.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    @DisplayName("회원번호가 같고 이름이 다른 두 멤버는 같은회원으로 판단한다")
    @Test
    public void equalsTest(){
        Member member1 = new Member(1L, "test1");
        Member member2 = new Member(1L, "test2");

        assertEquals(member1, member2);
        assertEquals(member2, member1);
    }

    @DisplayName("회원 번호가 다르면 이름이 같아도 false")
    @Test
    public void notEqualsTest(){
        Member member1 = new Member(1L, "test1");
        Member member2 = new Member(2L, "test1");

        assertNotEquals(member1, member2);
        assertNotEquals(member2, member1);
    }

    @DisplayName("같은 회원이라면 같은 hashcode를 가진다")
    @Test
    public void equalsHashcodeTest(){
        Member member1 = new Member(1L, "test1");
        Member member2 = new Member(1L, "test2");

        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
    }

    @DisplayName("HashSet으로 동일한 칸에 저장이 되는지 확인합니다")
    @Test
    public void hashSetTest(){
        Member member1 = new Member(1L, "test1");
        Member member2 = new Member(1L, "test2");

        Set<Member> set = new HashSet<>();
        set.add(member1);
        set.add(member2);

        assertEquals(1, set.size());
    }

    @DisplayName("HashMap으로 동일한 객체인지 확인합니다")
    @Test
    public void hashMapTest(){
        Member member1 = new Member(1L, "test1");
        Member member2 = new Member(1L, "test2");

        Map<Member, String> members = new HashMap<>();

        members.put(member1, "저장된 회원");
        assertEquals("저장된 회원", members.get(member2));
    }
}
