package com.api.java.money;

public final class Money {
    private final long amount;

    public Money(long amount){
        this.amount = amount;
    }

    public long getAmount(){
        return this.amount;
    }

    public Money add(long value){
        return new Money(this.amount + value);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Money)){
            return false;
        }

        Money money = (Money)obj;

       return this.amount == money.amount;
    }

    @Override
    public int hashCode(){
        return Long.hashCode(this.amount);
    }
}
