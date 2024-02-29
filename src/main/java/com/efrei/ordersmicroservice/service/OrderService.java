package com.efrei.ordersmicroservice.service;

import com.efrei.ordersmicroservice.model.ClientOrder;
import com.efrei.ordersmicroservice.model.dto.OrderToCreate;

import java.util.List;

public interface OrderService {

    ClientOrder createOrder(String bearerToken, OrderToCreate orderToCreate);

    List<ClientOrder> getAllOrders(String bearerToken);

    ClientOrder getOrderById(String bearerToken, String id);

    List<ClientOrder> getOrdersByUserBowlingPark(String bearerToken);

    List<ClientOrder> getUnpaidOrderByClientId(String bearerToken, String clientId);

    void markOrderAsPaid(String bearerToken, List<String> commandIds);

    void deleteOrderById(String bearerToken, String orderId);
}
