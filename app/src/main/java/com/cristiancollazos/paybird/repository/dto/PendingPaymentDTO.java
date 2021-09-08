package com.cristiancollazos.paybird.repository.dto;

import com.cristiancollazos.paybird.misc.Utilities;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PendingPaymentDTO {

    @SerializedName("code")
    private Integer nuCode;

    @SerializedName("customer_document")
    private String sbCustomerDocument;

    @SerializedName("customer_name")
    private String sbCustomerName;

    @SerializedName("address")
    private String sbHomeAddress;

    @SerializedName("credit_date")
    private Date dtCreditDate;

    @SerializedName("credit_value")
    private Float flCreditValue;

    @SerializedName("interest")
    private Float flInterest;

    @SerializedName("remainder")
    private Float flRemainder;

    @SerializedName("lastpayment_date")
    private Date dtLastPayment;

    @SerializedName("credit_length")
    private Integer nuCreditLength;

    @SerializedName("period")
    private Integer nuPeriod;

    @SerializedName("nextpayment_date")
    private Date dtNextPayment;

    @SerializedName("installment_value")
    private Float flInstallmentValue;

    @SerializedName("installments")
    private Integer nuInstallments;

    @SerializedName("contact_info")
    private String sbContactInfo;

    @SerializedName("order")
    private Integer nuOrder;

    public PendingPaymentDTO() {
    }

    public PendingPaymentDTO(Integer nuCode, String sbCustomerDocument, String sbCustomerName,
                             String sbHomeAddress, Date dtCreditDate, Float flCreditValue,
                             Float flInterest, Float flRemainder, Date dtLastPayment,
                             Integer nuCreditLength, Integer nuPeriod, Date dtNextPayment,
                             Float flInstallmentValue, Integer nuInstallments, String sbContactInfo,
                             Integer nuOrder) {
        this.nuCode = nuCode;
        this.sbCustomerDocument = sbCustomerDocument;
        this.sbCustomerName = sbCustomerName;
        this.sbHomeAddress = sbHomeAddress;
        this.dtCreditDate = dtCreditDate;
        this.flCreditValue = flCreditValue;
        this.flInterest = flInterest;
        this.flRemainder = flRemainder;
        this.dtLastPayment = dtLastPayment;
        this.nuCreditLength = nuCreditLength;
        this.nuPeriod = nuPeriod;
        this.dtNextPayment = dtNextPayment;
        this.flInstallmentValue = flInstallmentValue;
        this.nuInstallments = nuInstallments;
        this.sbContactInfo = sbContactInfo;
        this.nuOrder = nuOrder;
    }

    public Integer getNuCode() {
        return nuCode;
    }

    public void setNuCode(Integer nuCode) {
        this.nuCode = nuCode;
    }

    public String getSbCustomerDocument() {
        return sbCustomerDocument;
    }

    public void setSbCustomerDocument(String sbCustomerDocument) {
        this.sbCustomerDocument = sbCustomerDocument;
    }

    public String getSbCustomerName() {
        return sbCustomerName;
    }

    public void setSbCustomerName(String sbCustomerName) {
        this.sbCustomerName = sbCustomerName;
    }

    public String getSbHomeAddress() {
        return sbHomeAddress;
    }

    public void setSbHomeAddress(String sbHomeAddress) {
        this.sbHomeAddress = sbHomeAddress;
    }

    public Date getDtCreditDate() {
        return dtCreditDate;
    }

    public void setDtCreditDate(Date dtCreditDate) {
        this.dtCreditDate = dtCreditDate;
    }

    public void setFlCreditValue(Float flCreditValue) {
        this.flCreditValue = flCreditValue;
    }

    public Float getFlInterest() {
        return flInterest;
    }

    public void setFlInterest(Float flInterest) {
        this.flInterest = flInterest;
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

    public Integer getNuCreditLength() {
        return nuCreditLength;
    }

    public void setNuCreditLength(Integer nuCreditLength) {
        this.nuCreditLength = nuCreditLength;
    }

    public Integer getNuPeriod() {
        return nuPeriod;
    }

    public void setNuPeriod(Integer nuPeriod) {
        this.nuPeriod = nuPeriod;
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

    public void setNuInstallments(Integer nuInstallments) {
        this.nuInstallments = nuInstallments;
    }

    public String getSbContactInfo() {
        return sbContactInfo;
    }

    public void setSbContactInfo(String sbContactInfo) {
        this.sbContactInfo = sbContactInfo;
    }

    public Float getFlCreditValue() {
        return flCreditValue;
    }

    public Integer getNuOrder() {
        return nuOrder;
    }

    public void setNuOrder(Integer nuOrder) {
        this.nuOrder = nuOrder;
    }

    public String getDisplayCreditValue() {
        return Utilities.getFormattedValue(flCreditValue);
    }

    public String getDisplayRemainderValue() {
        return Utilities.getFormattedValue(flRemainder);
    }

    public String getDisplayInstallmentValue() {
        return Utilities.getFormattedValue(flInstallmentValue);
    }

    public Date getDtNextPayment() {
        return dtNextPayment;
    }

    public Integer getNuInstallments() {
        return nuInstallments;
    }

}
