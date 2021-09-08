package com.cristiancollazos.paybird.repository.dto;

import com.cristiancollazos.paybird.view.adapter.interfaces.SimpleRecyclerAdaptable;
import com.google.gson.annotations.SerializedName;

public class CustomerDTO implements SimpleRecyclerAdaptable {

    @SerializedName("code")
    private Integer nuCode;
    @SerializedName("document_type")
    private String sbDocumentType;
    @SerializedName("document")
    private String sbDocument;
    @SerializedName("name")
    private String sbName;
    @SerializedName("lastname")
    private String sbLastName;
    @SerializedName("address")
    private String sbHomeAddress;
    @SerializedName("city_code")
    private Integer nuHomeCityCode;
    @SerializedName("phone_number")
    private String sbHomePhoneNumber;
    @SerializedName("mobile_number")
    private String sbMobileNumber;
    @SerializedName("work_address")
    private String sbWorkAddress;
    @SerializedName("work_city_code")
    private Integer nuWorkCityCode;
    @SerializedName("work_phone_number")
    private String sbWorkPhoneNumber;
    @SerializedName("company_name")
    private String sbCompanyName;
    @SerializedName("type")
    private String sbType;
    @SerializedName("route_code")
    private Integer nuRouteCode;
    @SerializedName("neighborhood_code")
    private Integer nuNeighborhoodCode;
    private String sbNeighborhoodName;
    private String sbCityName;

    public CustomerDTO(Integer nuCode, String sbDocumentType, String sbDocument, String sbName,
                       String sbLastName, String sbHomeAddress, Integer nuHomeCityCode,
                       String sbHomePhoneNumber, String sbMobileNumber, String sbWorkAddress,
                       Integer nuWorkCityCode, String sbWorkPhoneNumber, String sbCompanyName,
                       String sbType, Integer nuRouteCode, Integer nuNeighborhoodCode) {
        this.nuCode = nuCode;
        this.sbDocumentType = sbDocumentType;
        this.sbDocument = sbDocument;
        this.sbName = sbName;
        this.sbLastName = sbLastName;
        this.sbHomeAddress = sbHomeAddress;
        this.nuHomeCityCode = nuHomeCityCode;
        this.sbHomePhoneNumber = sbHomePhoneNumber;
        this.sbMobileNumber = sbMobileNumber;
        this.sbWorkAddress = sbWorkAddress;
        this.nuWorkCityCode = nuWorkCityCode;
        this.sbWorkPhoneNumber = sbWorkPhoneNumber;
        this.sbCompanyName = sbCompanyName;
        this.sbType = sbType;
        this.nuRouteCode = nuRouteCode;
        this.nuNeighborhoodCode = nuNeighborhoodCode;
    }

    public CustomerDTO() {
    }

    public Integer getNuCode() {
        return nuCode;
    }

    public void setNuCode(Integer nuCode) {
        this.nuCode = nuCode;
    }

    public String getSbDocumentType() {
        return sbDocumentType;
    }

    public void setSbDocumentType(String sbDocumentType) {
        this.sbDocumentType = sbDocumentType;
    }

    public String getSbDocument() {
        return sbDocument;
    }

    public void setSbDocument(String sbDocument) {
        this.sbDocument = sbDocument;
    }

    public String getSbName() {
        return sbName;
    }

    public void setSbName(String sbName) {
        this.sbName = sbName;
    }

    public String getSbLastName() {
        return sbLastName;
    }

    public void setSbLastName(String sbLastName) {
        this.sbLastName = sbLastName;
    }

    public String getSbHomeAddress() {
        return sbHomeAddress;
    }

    public void setSbHomeAddress(String sbHomeAddress) {
        this.sbHomeAddress = sbHomeAddress;
    }

    public Integer getNuHomeCityCode() {
        return nuHomeCityCode;
    }

    public void setNuHomeCityCode(Integer nuHomeCityCode) {
        this.nuHomeCityCode = nuHomeCityCode;
    }

    public String getSbHomePhoneNumber() {
        return sbHomePhoneNumber;
    }

    public void setSbHomePhoneNumber(String sbHomePhoneNumber) {
        this.sbHomePhoneNumber = sbHomePhoneNumber;
    }

    public String getSbMobileNumber() {
        return sbMobileNumber;
    }

    public void setSbMobileNumber(String sbMobileNumber) {
        this.sbMobileNumber = sbMobileNumber;
    }

    public String getSbWorkAddress() {
        return sbWorkAddress;
    }

    public void setSbWorkAddress(String sbWorkAddress) {
        this.sbWorkAddress = sbWorkAddress;
    }

    public Integer getNuWorkCityCode() {
        return nuWorkCityCode;
    }

    public void setNuWorkCityCode(Integer nuWorkCityCode) {
        this.nuWorkCityCode = nuWorkCityCode;
    }

    public String getSbWorkPhoneNumber() {
        return sbWorkPhoneNumber;
    }

    public void setSbWorkPhoneNumber(String sbWorkPhoneNumber) {
        this.sbWorkPhoneNumber = sbWorkPhoneNumber;
    }

    public String getSbCompanyName() {
        return sbCompanyName;
    }

    public void setSbCompanyName(String sbCompanyName) {
        this.sbCompanyName = sbCompanyName;
    }

    public String getSbType() {
        return sbType;
    }

    public void setSbType(String sbType) {
        this.sbType = sbType;
    }

    public Integer getNuRouteCode() {
        return nuRouteCode;
    }

    public void setNuRouteCode(Integer nuRouteCode) {
        this.nuRouteCode = nuRouteCode;
    }

    public Integer getNuNeighborhoodCode() {
        return nuNeighborhoodCode;
    }

    public void setNuNeighborhoodCode(Integer nuNeighborhoodCode) {
        this.nuNeighborhoodCode = nuNeighborhoodCode;
    }

    public String getSbNeighborhoodName() {
        return sbNeighborhoodName;
    }

    public void setSbNeighborhoodName(String sbNeighborhoodName) {
        this.sbNeighborhoodName = sbNeighborhoodName;
    }

    public String getSbCityName() {
        return sbCityName;
    }

    public void setSbCityName(String sbCityName) {
        this.sbCityName = sbCityName;
    }

    @Override
    public String getRecyclerItemDisplayValue() {
        return sbDocumentType + " " + sbDocument + " - " + sbName + " " + sbLastName;
    }

}
