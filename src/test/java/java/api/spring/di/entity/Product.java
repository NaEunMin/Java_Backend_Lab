package java.api.spring.di.entity;

public class Product {
    private final long id;
    private final String name;
    private final int price;

    public Product(long id, String name, int price) {
        if(id <= 0){
            throw new IllegalArgumentException("ID는 양수여야 합니다.");
        }
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("이름은 null이거나 비어있을 수 없습니다.");
        }
        if(price <= 0){
            throw new IllegalArgumentException("price는 양수여야 합니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
