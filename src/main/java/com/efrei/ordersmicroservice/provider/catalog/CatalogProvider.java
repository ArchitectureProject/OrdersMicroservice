package com.efrei.ordersmicroservice.provider.catalog;

import com.efrei.ordersmicroservice.model.dto.Product;

import java.util.List;

public interface CatalogProvider {

    List<Product> getProductByIds(String bearerToken, List<String> productIds);
}
