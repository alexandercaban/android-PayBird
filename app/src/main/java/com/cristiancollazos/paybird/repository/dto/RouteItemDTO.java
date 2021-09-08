package com.cristiancollazos.paybird.repository.dto;

import com.cristiancollazos.paybird.view.adapter.interfaces.SpinnerAdaptable;
import com.google.gson.annotations.SerializedName;

public class RouteItemDTO implements SpinnerAdaptable {

    @SerializedName("order")
    private Integer nuOrder;

    @SerializedName("customer_name")
    private String sbCustomerName;

    @SerializedName("address")
    private String sbAddress;

    public RouteItemDTO(Integer nuOrder, String sbCustomerName, String sbAddress) {
        this.nuOrder = nuOrder;
        this.sbCustomerName = sbCustomerName;
        this.sbAddress = sbAddress;
    }

    public RouteItemDTO() {
    }

    public Integer getNuOrder() {
        return nuOrder;
    }

    public void setNuOrder(Integer nuOrder) {
        this.nuOrder = nuOrder;
    }

    public String getSbCustomerName() {
        return sbCustomerName;
    }

    public void setSbCustomerName(String sbCustomerName) {
        this.sbCustomerName = sbCustomerName;
    }

    public String getSbAddress() {
        return sbAddress;
    }

    public void setSbAddress(String sbAddress) {
        this.sbAddress = sbAddress;
    }

    @Override
    public String getCode() {
        return String.valueOf(nuOrder);
    }

    @Override
    public String getDisplayValue() {
        return sbCustomerName;
    }

}
