package com.efrei.ordersmicroservice.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductCommand(String productId, int quantity) {
}