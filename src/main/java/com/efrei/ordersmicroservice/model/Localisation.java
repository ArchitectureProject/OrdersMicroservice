package com.efrei.ordersmicroservice.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Localisation(String bowlingParkId, int alleyNumber) {
}
