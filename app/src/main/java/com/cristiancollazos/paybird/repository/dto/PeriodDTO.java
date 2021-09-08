package com.cristiancollazos.paybird.repository.dto;

import com.cristiancollazos.paybird.view.adapter.interfaces.SpinnerAdaptable;
import com.google.gson.annotations.SerializedName;

public class PeriodDTO implements SpinnerAdaptable {

    @SerializedName("code")
    private Integer nuCode;

    @SerializedName("definition")
    private String sbDefinition;

    public PeriodDTO(Integer nuCode,
                     String sbDefinition) {
        this.nuCode = nuCode;
        this.sbDefinition = sbDefinition;
    }

    public PeriodDTO() {
    }

    public Integer getNuCode() {
        return nuCode;
    }

    public void setNuCode(Integer nuCode) {
        this.nuCode = nuCode;
    }

    public String getSbDefinition() {
        return sbDefinition;
    }

    public void setSbDefinition(String sbDefinition) {
        this.sbDefinition = sbDefinition;
    }

    @Override
    public String getCode() {
        return String.valueOf(nuCode);
    }

    @Override
    public String getDisplayValue() {
        return sbDefinition;
    }

}
