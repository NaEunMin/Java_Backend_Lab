package java.api.java.member;

public class Member {
    long memberId;
    String name;

    public Member(long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object obj) {

        //자기자신이라면 true
        if(this == obj){
            return true;
        }

        //같은 타입이어야 한다.
        if(!(obj instanceof Member)){
            return false;
        }

        Member member = (Member)obj;

        return this.memberId == member.memberId;
    }

    @Override
    public int hashCode(){
        return Long.hashCode(this.memberId);
    }
}
