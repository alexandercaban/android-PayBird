package com.cristiancollazos.paybird.repository.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CreditDTO {

    @SerializedName("code")
    private Integer nuCode;

    @SerializedName("created_date")
    private Date dtCreatedDate;

    @SerializedName("value")
    private Float flValue;

    @SerializedName("interest")
    private Float flInterest;

    @SerializedName("status")
    private String sbStatus;

    @SerializedName("remainder")
    private Float flRemainder;

    @SerializedName("last_payment")
    private Date dtLastPayment;

    @SerializedName("period")
    private Integer nuPeriod;

    @SerializedName("co_owner")
    private String sbCoOwner;

    @SerializedName("customer_code")
    private Integer nuCustomerCode;

    @SerializedName("length")
    private Integer nuLength;

    @SerializedName("co_owner_code")
    private Integer nuCoOwnerCode;

    @SerializedName("next_payment")
    private Date dtNextPayment;

    @SerializedName("installment_value")
    private Float flInstallmentValue;

    @SerializedName("installment_quantity")
    private Integer nuInstallmentQuantity;

    @SerializedName("customer_name")
    private String sbCustomerName;

    public CreditDTO(Integer nuCode,
                     Date dtCreatedDate,
                     Float flValue,
                     Float flInterest,
                     String sbStatus,
                     Float flRemainder,
                     Date dtLastPayment,
                     Integer nuPeriod,
                     String sbCoOwner,
                     Integer nuCustomerCode,
                     Integer nuLength,
                     Integer nuCoOwnerCode,
                     Date dtNextPayment,
                     Float flInstallmentValue,
                     Integer nuInstallmentQuantity,
                     String sbCustomerName) {
        this.nuCode = nuCode;
        this.dtCreatedDate = dtCreatedDate;
        this.flValue = flValue;
        this.flInterest = flInterest;
        this.sbStatus = sbStatus;
        this.flRemainder = flRemainder;
        this.dtLastPayment = dtLastPayment;
        this.nuPeriod = nuPeriod;
        this.sbCoOwner = sbCoOwner;
        this.nuCustomerCode = nuCustomerCode;
        this.nuLength = nuLength;
        this.nuCoOwnerCode = nuCoOwnerCode;
        this.dtNextPayment = dtNextPayment;
        this.flInstallmentValue = flInstallmentValue;
        this.nuInstallmentQuantity = nuInstallmentQuantity;
        this.sbCustomerName = sbCustomerName;
    }

    public CreditDTO() {
    }

    public Integer getNuCode() {
        return nuCode;
    }

    public void setNuCode(Integer nuCode) {
        this.nuCode = nuCode;
    }

    public Date getDtCreatedDate() {
        return dtCreatedDate;
    }

    public void setDtCreatedDate(Date dtCreatedDate) {
        this.dtCreatedDate = dtCreatedDate;
    }

    public Float getFlValue() {
        return flValue;
    }

    public void setFlValue(Float flValue) {
        this.flValue = flValue;
    }

    public Float getFlInterest() {
        return flInterest;
    }

    public void setFlInterest(Float flInterest) {
        this.flInterest = flInterest;
    }

    public String getSbStatus() {
        return sbStatus;
    }

    public void setSbStatus(String sbStatus) {
        this.sbStatus = sbStatus;
    }

    public Float getFlRemainder() {
        return flRemainder;
    }

    public void setFlRemainder(Float flRemainder) {
        this.flRemainder = flRemainder;
    }

    public Date getDtLastPayment() {
        return dtLastPayment;
    }

    public void setDtLastPayment(Date dtLastPayment) {
        this.dtLastPayment = dtLastPayment;
    }

    public String getSbCoOwner() {
        return sbCoOwner;
    }

    public void setSbCoOwner(String sbCoOwner) {
        this.sbCoOwner = sbCoOwner;
    }

    public Integer getNuPeriod() {
        return nuPeriod;
    }

    public void setNuPeriod(Integer nuPeriod) {
        this.nuPeriod = nuPeriod;
    }

    public Integer getNuCustomerCode() {
        return nuCustomerCode;
    }

    public void setNuCustomerCode(Integer nuCustomerCode) {
        this.nuCustomerCode = nuCustomerCode;
    }

    public Integer getNuLength() {
        return nuLength;
    }

    public void setNuLength(Integer nuLength) {
        this.nuLength = nuLength;
    }

    public Integer getNuCoOwnerCode() {
        return nuCoOwnerCode;
    }

    public void setNuCoOwnerCode(Integer nuCoOwnerCode) {
        this.nuCoOwnerCode = nuCoOwnerCode;
    }

    public Date getDtNextPayment() {
        return dtNextPayment;
    }

    public void setDtNextPayment(Date dtNextPayment) {
        this.dtNextPayment = dtNextPayment;
    }

    public Float getFlInstallmentValue() {
        return flInstallmentValue;
    }

    public void setFlInstallmentValue(Float flInstallmentValue) {
        this.flInstallmentValue = flInstallmentValue;
    }

    public Integer getNuInstallmentQuantity() {
        return nuInstallmentQuantity;
    }

    public void setNuInstallmentQuantity(Integer nuInstallmentQuantity) {
        this.nuInstallmentQuantity = nuInstallmentQuantity;
    }

    public String getSbCustomerName() {
        return sbCustomerName;
    }

    public void setSbCustomerName(String sbCustomerName) {
        this.sbCustomerName = sbCustomerName;
    }

}
