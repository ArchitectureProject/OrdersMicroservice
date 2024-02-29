package com.efrei.ordersmicroservice.service;

import com.efrei.ordersmicroservice.exception.custom.OrderNotFoundException;
import com.efrei.ordersmicroservice.exception.custom.WrongUserRoleException;
import com.efrei.ordersmicroservice.model.CustomerInfos;
import com.efrei.ordersmicroservice.model.Localisation;
import com.efrei.ordersmicroservice.model.ClientOrder;
import com.efrei.ordersmicroservice.model.UserRole;
import com.efrei.ordersmicroservice.model.dto.OrderToCreate;
import com.efrei.ordersmicroservice.repository.OrderRepository;
import com.efrei.ordersmicroservice.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final JwtUtils jwtUtils;

    public OderServiceImpl(OrderRepository orderRepository, JwtUtils jwtUtils) {
        this.orderRepository = orderRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ClientOrder createOrder(String bearerToken, OrderToCreate orderToCreate) {
        if(!jwtUtils.validateJwt(bearerToken.substring(7), UserRole.CUSTOMER)){
            throw new WrongUserRoleException("User role does not gives him rights to call this endpoint");
        }
        ClientOrder clientOrder = mapOrderToCreateIntoOrder(orderToCreate);
        clientOrder.setPlacedAt(Instant.now().toEpochMilli());
        clientOrder.getCustomerInfos().setUserId(jwtUtils.getUserIdFromJWT(bearerToken.substring(7)));
        //TODO : Appel Catalog pour calculer le total price
        clientOrder.setTotalPrice(100);
        clientOrder.setPaidByCustomer(false);
        //TODO : Update Session total price
        return orderRepository.save(clientOrder);
    }

    @Override
    public List<ClientOrder> getAllOrders(String bearerToken) {
        if(!jwtUtils.validateJwt(bearerToken.substring(7), UserRole.AGENT)){
            throw new WrongUserRoleException("User role does not gives him rights to call this endpoint");
        }
        return orderRepository.findAll();
    }

    @Override
    public ClientOrder getOrderById(String bearerToken, String id) {
        if(!jwtUtils.validateJwt(bearerToken.substring(7), UserRole.AGENT)){
            throw new WrongUserRoleException("User role does not gives him rights to call this endpoint");
        }
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Override
    public List<ClientOrder> getOrdersByUserBowlingPark(String bearerToken) {
        if(!jwtUtils.validateJwt(bearerToken.substring(7), UserRole.AGENT)){
            throw new WrongUserRoleException("User role does not gives him rights to call this endpoint");
        }
        //TODO : Get Bowling Park Id from agent's UserId
        String bowlingParkId = "bowlingParkId";
        return orderRepository.findByLocalisationBowlingParkId(bowlingParkId);
    }

    @Override
    public List<ClientOrder> getUnpaidOrderByClientId(String bearerToken, String clientId) {
        jwtUtils.validateJwt(bearerToken.substring(7), null);
        return orderRepository.findByCustomerInfosUserIdAndPaidByCustomerIsFalse(clientId);
    }

    @Override
    public void markOrderAsPaid(String bearerToken, List<String> commandIds) {
        jwtUtils.validateJwt(bearerToken.substring(7), null);
        orderRepository.setOrderToPayed(commandIds);
    }

    @Override
    public void deleteOrderById(String bearerToken, String orderId) {
        jwtUtils.validateJwt(bearerToken.substring(7), UserRole.AGENT);
        orderRepository.deleteById(orderId);
    }

    private ClientOrder mapOrderToCreateIntoOrder(OrderToCreate orderToCreate){
        ClientOrder clientOrder = new ClientOrder();
        CustomerInfos customerInfos = new CustomerInfos();
        customerInfos.setEmail(orderToCreate.getCustomerInfos().email());
        customerInfos.setName(orderToCreate.getCustomerInfos().name());
        customerInfos.setPhoneNumber(orderToCreate.getCustomerInfos().phoneNumber());
        clientOrder.setCustomerInfos(customerInfos);
        clientOrder.setProductCommands(orderToCreate.getProductCommands());
        //TODO : Appel BowlingPark pour avoir une fonction qui donne l'objet localisation en fonction de son QRCode
        Localisation localisation = new Localisation("bowlingId", 3);
        clientOrder.setLocalisation(localisation);
        return clientOrder;
    }
}
