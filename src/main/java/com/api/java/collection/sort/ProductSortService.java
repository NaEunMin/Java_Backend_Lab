package com.api.java.collection.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductSortService {
    public List<Product> sortByPriceAscending(List<Product> products) {
        //new ArrayList<>(products)가 products의 복사본을 만들어준다.
        List<Product> sortedProducts = new ArrayList<>(products);

        //복사본을 정렬 후 리턴해주면 된다. comparingInt()의 기본 동작은 오름차순
        sortedProducts.sort(Comparator.comparingInt(Product::getPrice));

        return List.copyOf(sortedProducts);
    }

    public List<Product> sortByPriceDescending(List<Product> products) {
        List<Product> sortedProducts = new ArrayList<>(products);

        sortedProducts.sort(Comparator.comparingInt(Product::getPrice).reversed());
        return List.copyOf(sortedProducts);
    }

    public List<Product> sortByStockDescendingAndPriceAscending(List<Product> products) {
        List<Product> sortedProducts = new ArrayList<>(products);

        //재고를 내림차순으로 정렬하되, 재고가 같으면 가격을 오름차순으로 정렬한다.
        sortedProducts.sort(Comparator.comparingInt(Product::getStock).reversed()
                .thenComparingInt(Product::getPrice)
                .thenComparing(Product::getId));

        return List.copyOf(sortedProducts);
    }

    public List<Product> sortByNaturalOrder(List<Product> products) {
        List<Product> sortedProducts = new ArrayList<>(products);

        sortedProducts.sort(Comparator.naturalOrder());
        return List.copyOf(sortedProducts);
    }
}
