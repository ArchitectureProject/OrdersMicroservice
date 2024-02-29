package com.efrei.ordersmicroservice.repository;

import com.efrei.ordersmicroservice.model.ClientOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<ClientOrder, String> {
    List<ClientOrder> findByLocalisationBowlingParkId(String bowlingParkId);

    List<ClientOrder> findByCustomerInfosUserIdAndPaidByCustomerIsFalse(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE ClientOrder p SET p.paidByCustomer = true WHERE p.id IN :orderIds")
    void setOrderToPayed(List<String> orderIds);
}
