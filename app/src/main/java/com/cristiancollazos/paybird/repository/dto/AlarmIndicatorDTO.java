package com.cristiancollazos.paybird.repository.dto;

import com.google.gson.annotations.SerializedName;

public class AlarmIndicatorDTO {

    @SerializedName("indicator")
    private Boolean blIndicator;

    public AlarmIndicatorDTO(Boolean blIndicator) {
        this.blIndicator = blIndicator;
    }

    public AlarmIndicatorDTO() {
    }

    public Boolean getBlIndicator() {
        return blIndicator;
    }

    public void setBlIndicator(Boolean blIndicator) {
        this.blIndicator = blIndicator;
    }

}
