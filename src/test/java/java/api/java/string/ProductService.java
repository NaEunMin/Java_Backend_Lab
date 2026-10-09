package java.api.java.string;

import java.util.List;

public class ProductService {
    public String joinProducts(List<String> products) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            sb.append(products.get(i));
            if (i != products.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
