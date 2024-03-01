package com.efrei.ordersmicroservice.model.dto;

public record Product(String id, String name, float price, boolean available) {
}