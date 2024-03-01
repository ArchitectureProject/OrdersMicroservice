package com.efrei.ordersmicroservice.service;

import com.efrei.ordersmicroservice.exception.custom.BadOrderException;
import com.efrei.ordersmicroservice.exception.custom.OrderNotFoundException;
import com.efrei.ordersmicroservice.exception.custom.WrongUserRoleException;
import com.efrei.ordersmicroservice.model.*;
import com.efrei.ordersmicroservice.model.dto.OrderToCreate;
import com.efrei.ordersmicroservice.model.dto.Product;
import com.efrei.ordersmicroservice.provider.catalog.CatalogProvider;
import com.efrei.ordersmicroservice.repository.OrderRepository;
import com.efrei.ordersmicroservice.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final JwtUtils jwtUtils;

    private final CatalogProvider catalogProvider;

    public OderServiceImpl(OrderRepository orderRepository, JwtUtils jwtUtils, CatalogProvider catalogProvider) {
        this.orderRepository = orderRepository;
        this.jwtUtils = jwtUtils;
        this.catalogProvider = catalogProvider;
    }

    @Override
    public ClientOrder createOrder(String bearerToken, OrderToCreate orderToCreate) {
        if(!jwtUtils.validateJwt(bearerToken.substring(7), UserRole.CUSTOMER)){
            throw new WrongUserRoleException("User role does not gives him rights to call this endpoint");
        }

        if(orderToCreate.getCustomerInfos().name() == null){
            throw new BadOrderException("Order miss a user name");
        }

        if(orderToCreate.getCustomerInfos().email() == null){
            throw new BadOrderException("Order miss an email");
        }

        ClientOrder clientOrder = mapOrderToCreateIntoOrder(orderToCreate);
        clientOrder.setPlacedAt(Instant.now().toEpochMilli());
        clientOrder.getCustomerInfos().setUserId(jwtUtils.getUserIdFromJWT(bearerToken.substring(7)));
        clientOrder.setTotalPrice(calculateTotalPrice(bearerToken, orderToCreate.getProductCommands()));
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

    private float calculateTotalPrice(String bearerToken, List<ProductCommand> productCommands) {
        List<String> productIds = productCommands.stream()
                .map(ProductCommand::productId)
                .collect(Collectors.toList());

        List<Product> products = catalogProvider.getProductByIds(bearerToken, productIds);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::id, product -> product));

        float totalPrice = 0;
        for (ProductCommand command : productCommands) {
            Product product = productMap.get(command.productId());
            if (product != null) {
                totalPrice += product.price() * command.quantity();
            }
        }

        return totalPrice;
    }
}
