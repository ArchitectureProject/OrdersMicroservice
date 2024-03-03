package com.efrei.ordersmicroservice.provider.bowling;

import com.efrei.ordersmicroservice.model.Localisation;
import com.efrei.ordersmicroservice.model.dto.BowlingPark;

import java.util.List;

public interface BowlingParkProvider {

    List<BowlingPark> getBowlingParkByManagerId(String bearerToken, String managerId);

    Localisation getBowlingParkAlleyFromQrCode(String bearerToken, String qrCode);
}
