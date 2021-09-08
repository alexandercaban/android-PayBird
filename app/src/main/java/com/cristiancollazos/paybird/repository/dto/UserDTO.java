package com.cristiancollazos.paybird.repository.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserDTO {

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
    private String sbAddress;
    @SerializedName("city_code")
    private Integer nuCityCode;
    @SerializedName("email")
    private String sbEmail;
    @SerializedName("created_date")
    private Date dtCreatedDate;
    @SerializedName("status")
    private String sbStatus;
    @SerializedName("login")
    private String sbLogin;
    @SerializedName("password")
    private String sbPassword;
    @SerializedName("login_time")
    private Long lgLoginTime;

    public UserDTO(Integer nuCode, String sbDocumentType, String sbDocument, String sbName,
                   String sbLastName, String sbAddress, Integer nuCityCode, String sbEmail,
                   Date dtCreatedDate, String sbStatus, String sbLogin, String sbPassword) {
        this.nuCode = nuCode;
        this.sbDocumentType = sbDocumentType;
        this.sbDocument = sbDocument;
        this.sbName = sbName;
        this.sbLastName = sbLastName;
        this.sbAddress = sbAddress;
        this.nuCityCode = nuCityCode;
        this.sbEmail = sbEmail;
        this.dtCreatedDate = dtCreatedDate;
        this.sbStatus = sbStatus;
        this.sbLogin = sbLogin;
        this.sbPassword = sbPassword;
    }

    public UserDTO() {
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

    public String getSbAddress() {
        return sbAddress;
    }

    public void setSbAddress(String sbAddress) {
        this.sbAddress = sbAddress;
    }

    public Integer getNuCityCode() {
        return nuCityCode;
    }

    public void setNuCityCode(Integer nuCityCode) {
        this.nuCityCode = nuCityCode;
    }

    public String getSbEmail() {
        return sbEmail;
    }

    public void setSbEmail(String sbEmail) {
        this.sbEmail = sbEmail;
    }

    public Date getDtCreatedDate() {
        return dtCreatedDate;
    }

    public void setDtCreatedDate(Date dtCreatedDate) {
        this.dtCreatedDate = dtCreatedDate;
    }

    public String getSbStatus() {
        return sbStatus;
    }

    public void setSbStatus(String sbStatus) {
        this.sbStatus = sbStatus;
    }

    public String getSbLogin() {
        return sbLogin;
    }

    public void setSbLogin(String sbLogin) {
        this.sbLogin = sbLogin;
    }

    public String getSbPassword() {
        return sbPassword;
    }

    public void setSbPassword(String sbPassword) {
        this.sbPassword = sbPassword;
    }

    public Long getLgLoginTime() {
        return lgLoginTime;
    }

    public void setLgLoginTime(Long lgLoginTime) {
        this.lgLoginTime = lgLoginTime;
    }

}
