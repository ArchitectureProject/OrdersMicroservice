package com.efrei.ordersmicroservice.provider.catalog;

import com.efrei.ordersmicroservice.config.Properties;
import com.efrei.ordersmicroservice.exception.custom.CatalogMicroserviceException;
import com.efrei.ordersmicroservice.model.dto.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.List;

@Service
public class CatalogProviderImpl implements CatalogProvider {

    private final RestTemplate restTemplate;
    private final Properties properties;

    public CatalogProviderImpl(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public List<Product> getProductByIds(String bearerToken, List<String> productIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);

        HttpEntity<List<String>> requestEntity = new HttpEntity<>(productIds, headers);

        try {
            ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(
                    properties.getCatalogMicroserviceBaseUrl() + CatalogEndpoints.getProductsByIds,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<Product>>() {});

            return responseEntity.getBody();
        } catch (Exception e) {
            throw new CatalogMicroserviceException("Catalog microservice /getProductByIds sent back an error", e);
        }
    }
}
