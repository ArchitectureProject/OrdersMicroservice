package com.efrei.ordersmicroservice.controller;

import com.efrei.ordersmicroservice.model.ClientOrder;
import com.efrei.ordersmicroservice.model.dto.OrderToCreate;
import com.efrei.ordersmicroservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ClientOrder createOrder(@RequestHeader(name = "Authorization") String bearerToken,
                                   @RequestBody OrderToCreate orderToCreate){
        return orderService.createOrder(bearerToken, orderToCreate);
    }

    @GetMapping("/order")
    public List<ClientOrder> getAllOrders(@RequestHeader(name = "Authorization") String bearerToken){
        return orderService.getAllOrders(bearerToken);
    }

    @GetMapping("/order/{id}")
    public ClientOrder getOrderById(@RequestHeader(name = "Authorization") String bearerToken,
                                    @PathVariable String id){
        return orderService.getOrderById(bearerToken, id);
    }

    @GetMapping("/order/byBowlingPark")
    public List<ClientOrder> getOrdersByBowlingParkManager(@RequestHeader(name = "Authorization") String bearerToken){
        return orderService.getOrdersByUserBowlingPark(bearerToken);
    }

    @GetMapping("/order/unpaid/{id}")
    public List<ClientOrder> getUnpaidOrdersByClientId(@RequestHeader(name = "Authorization") String bearerToken,
                                                       @PathVariable String id){
        return orderService.getUnpaidOrderByClientId(bearerToken, id);
    }

    @PutMapping("/order/payed")
    public void setOrderAsPayed(@RequestHeader(name = "Authorization") String bearerToken,
                                @RequestBody List<String> ids){
        orderService.markOrderAsPaid(bearerToken, ids);
    }

    @DeleteMapping("/order/{id}")
    public void deleteOrderById(@RequestHeader(name = "Authorization") String bearerToken,
                                       @PathVariable String id){
        orderService.deleteOrderById(bearerToken, id);
    }

}
