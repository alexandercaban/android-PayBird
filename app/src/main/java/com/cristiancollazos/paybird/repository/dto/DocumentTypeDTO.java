package com.cristiancollazos.paybird.repository.dto;

import com.cristiancollazos.paybird.view.adapter.interfaces.SpinnerAdaptable;
import com.google.gson.annotations.SerializedName;

public class DocumentTypeDTO implements SpinnerAdaptable {

    @SerializedName("definition")
    private String sbDefinition;
    @SerializedName("value")
    private String sbValue;

    public DocumentTypeDTO(String sbDefinition, String sbValue) {
        this.sbDefinition = sbDefinition;
        this.sbValue = sbValue;
    }

    public DocumentTypeDTO() {
    }

    public String getSbDefinition() {
        return sbDefinition;
    }

    public void setSbDefinition(String sbDefinition) {
        this.sbDefinition = sbDefinition;
    }

    public String getSbValue() {
        return sbValue;
    }

    public void setSbValue(String sbValue) {
        this.sbValue = sbValue;
    }

    @Override
    public String getDisplayValue() {
        return sbValue;
    }

    @Override
    public String getCode() {
        return sbDefinition;
    }

}
