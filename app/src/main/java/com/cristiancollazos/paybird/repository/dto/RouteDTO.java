package com.cristiancollazos.paybird.repository.dto;

import com.cristiancollazos.paybird.view.adapter.interfaces.SpinnerAdaptable;
import com.google.gson.annotations.SerializedName;

public class RouteDTO implements SpinnerAdaptable {

    @SerializedName("code")
    private Integer nuCode;
    @SerializedName("name")
    private String sbName;

    public RouteDTO(Integer nuCode, String sbName) {
        this.nuCode = nuCode;
        this.sbName = sbName;
    }

    public Integer getNuCode() {
        return nuCode;
    }

    public void setNuCode(Integer nuCode) {
        this.nuCode = nuCode;
    }

    public String getSbName() {
        return sbName;
    }

    public void setSbName(String sbName) {
        this.sbName = sbName;
    }

    @Override
    public String getDisplayValue() {
        return getSbName();
    }

    @Override
    public String getCode() {
        return nuCode.toString();
    }

}
