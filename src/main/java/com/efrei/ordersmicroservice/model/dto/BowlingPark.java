package com.efrei.ordersmicroservice.model.dto;

import java.util.List;

public record BowlingPark(String id, String adress, String managerId, List<BowlingAlley> bowlingAlleys) {
}
