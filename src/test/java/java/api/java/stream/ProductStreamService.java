package java.api.java.stream;

import java.api.java.collection.sort.Product;

import java.util.Comparator;
import java.util.List;

public class ProductStreamService {
    public List<String> findTopAvailableProductNames(
            List<Product> products,
            int limit
    ) {
        if (products == null) {
            throw new IllegalArgumentException("상품 리스트가 null일 수 없습니다.");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit는 음수일 수 없습니다.");
        }
        if (limit == 0 || products.isEmpty()) {
            return List.of();
        }
        return products.stream()
                .filter(product -> product.getStock() >= 1)
                .sorted(Comparator.comparing(Product::getPrice).thenComparing(Product::getId))
                .map(Product::getName)
                .limit(limit)
                .toList();
    }

    public long calculateTotalInventoryValue(
            List<Product> products
    ) {
        if (products == null) {
            throw new IllegalArgumentException("products는 null일 수 없습니다.");
        }
        return products.stream()
                .filter(product -> product.getStock() >= 1)
                .mapToLong(product -> (long) product.getPrice() * product.getStock())
                .sum();
    }
}
