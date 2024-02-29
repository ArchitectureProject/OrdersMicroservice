package com.efrei.ordersmicroservice.model.dto;

import com.efrei.ordersmicroservice.model.ProductCommand;

import java.util.List;

public class OrderToCreate {
    private CustomerToCreateInfos customerInfos;
    private String alleyQrCode;
    private List<ProductCommand> productCommands;

    public CustomerToCreateInfos getCustomerInfos() {
        return customerInfos;
    }

    public void setCustomerToCreateInfos(CustomerToCreateInfos customerInfos) {
        this.customerInfos = customerInfos;
    }

    public String getAlleyQrCode() {
        return alleyQrCode;
    }

    public void setAlleyQrCode(String alleyQrCode) {
        this.alleyQrCode = alleyQrCode;
    }

    public List<ProductCommand> getProductCommands() {
        return productCommands;
    }

    public void setProductCommands(List<ProductCommand> productCommands) {
        this.productCommands = productCommands;
    }
}
