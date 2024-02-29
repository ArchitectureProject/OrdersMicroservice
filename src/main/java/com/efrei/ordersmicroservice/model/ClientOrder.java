package com.efrei.ordersmicroservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ClientOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long placedAt;

    private CustomerInfos customerInfos;

    private Localisation localisation;

    @ElementCollection
    private List<ProductCommand> productCommands;

    private float totalPrice;

    private boolean paidByCustomer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(Long placedAt) {
        this.placedAt = placedAt;
    }

    public CustomerInfos getCustomerInfos() {
        return customerInfos;
    }

    public void setCustomerInfos(CustomerInfos customerInfos) {
        this.customerInfos = customerInfos;
    }

    public Localisation getLocalisation() {
        return localisation;
    }

    public void setLocalisation(Localisation localisation) {
        this.localisation = localisation;
    }

    public List<ProductCommand> getProductCommands() {
        return productCommands;
    }

    public void setProductCommands(List<ProductCommand> productCommands) {
        this.productCommands = productCommands;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaidByCustomer() {
        return paidByCustomer;
    }

    public void setPaidByCustomer(boolean paidByCustomer) {
        this.paidByCustomer = paidByCustomer;
    }
}
