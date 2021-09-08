package com.cristiancollazos.paybird.repository.dto;

import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.misc.enums.MovementTypes;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MovementDTO {

    @SerializedName("code")
    private Integer nuCode;

    @SerializedName("date")
    private Date dtMovementDate;

    @SerializedName("hour")
    private String sbMovementHour;

    @SerializedName("customer_name")
    private String sbCustomerName;

    @SerializedName("credit_code")
    private Integer nuCreditCode;

    @SerializedName("value")
    private Float flValue;

    @SerializedName("type")
    private String sbType;

    @SerializedName("type_description")
    private String sbTypeDescription;

    @SerializedName("status")
    private String sbStatus;

    public MovementDTO() {
    }

    public MovementDTO(Integer nuCode, Date dtMovementDate, String sbMovementHour,
                       String sbCustomerName, Integer nuCreditCode, Float flValue, String sbType,
                       String sbStatus) {
        this.nuCode = nuCode;
        this.dtMovementDate = dtMovementDate;
        this.sbMovementHour = sbMovementHour;
        this.sbCustomerName = sbCustomerName;
        this.nuCreditCode = nuCreditCode;
        this.flValue = flValue;
        this.sbType = sbType;
        this.sbStatus = sbStatus;

        if (sbType.equals(MovementTypes.ABONO.getSbId())) {
            this.sbTypeDescription = MovementTypes.ABONO.getSbDescription();
        } else if (sbType.equals(MovementTypes.DESEMBOLSO.getSbId())) {
            this.sbTypeDescription = MovementTypes.DESEMBOLSO.getSbDescription();
        } else if (sbType.equals(MovementTypes.GASTO.getSbId())) {
            this.sbTypeDescription = MovementTypes.GASTO.getSbDescription();
        } else if (sbType.equals(MovementTypes.ENTREGA_ADMINISTRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.ENTREGA_ADMINISTRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.RECIBE_ADMINISTRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.ENTREGA_ADMINISTRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.ENTREGA_SOCIO.getSbId())) {
            this.sbTypeDescription = MovementTypes.ENTREGA_SOCIO.getSbDescription();
        } else if (sbType.equals(MovementTypes.VALE_COBRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.VALE_COBRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.VALE_AYUDANTE.getSbId())) {
            this.sbTypeDescription = MovementTypes.VALE_AYUDANTE.getSbDescription();
        } else if (sbType.equals(MovementTypes.PAGO_COBRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.PAGO_COBRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.PAGO_AYUDANTE.getSbId())) {
            this.sbTypeDescription = MovementTypes.PAGO_AYUDANTE.getSbDescription();
        } else if (sbType.equals(MovementTypes.PAGO_SUELDO.getSbId())) {
            this.sbTypeDescription = MovementTypes.PAGO_SUELDO.getSbDescription();
        } else if (sbType.equals(MovementTypes.SOBRANTE.getSbId())) {
            this.sbTypeDescription = MovementTypes.SOBRANTE.getSbDescription();
        } else if (sbType.equals(MovementTypes.ABONO_CREDITOSIMPLE.getSbId())) {
            this.sbTypeDescription = MovementTypes.ABONO_CREDITOSIMPLE.getSbDescription();
        } else {
            this.sbTypeDescription = "Movimiento";
        }
    }

    public Integer getNuCode() {
        return nuCode;
    }

    public void setNuCode(Integer nuCode) {
        this.nuCode = nuCode;
    }

    public Date getDtMovementDate() {
        return dtMovementDate;
    }

    public void setDtMovementDate(Date dtMovementDate) {
        this.dtMovementDate = dtMovementDate;
    }

    public String getSbMovementHour() {
        return sbMovementHour;
    }

    public void setSbMovementHour(String sbMovementHour) {
        this.sbMovementHour = sbMovementHour;
    }

    public String getSbCustomerName() {
        return sbCustomerName;
    }

    public void setSbCustomerName(String sbCustomerName) {
        this.sbCustomerName = sbCustomerName;
    }

    public Integer getNuCreditCode() {
        return nuCreditCode;
    }

    public void setNuCreditCode(Integer nuCreditCode) {
        this.nuCreditCode = nuCreditCode;
    }

    public Float getFlValue() {
        return flValue;
    }

    public void setFlValue(Float flValue) {
        this.flValue = flValue;
    }

    public String getSbType() {
        return sbType;
    }

    public void setSbType(String sbType) {
        this.sbType = sbType;

        if (sbType.equals(MovementTypes.ABONO.getSbId())) {
            this.sbTypeDescription = MovementTypes.ABONO.getSbDescription();
        } else if (sbType.equals(MovementTypes.DESEMBOLSO.getSbId())) {
            this.sbTypeDescription = MovementTypes.DESEMBOLSO.getSbDescription();
        } else if (sbType.equals(MovementTypes.GASTO.getSbId())) {
            this.sbTypeDescription = MovementTypes.GASTO.getSbDescription();
        } else if (sbType.equals(MovementTypes.ENTREGA_ADMINISTRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.ENTREGA_ADMINISTRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.RECIBE_ADMINISTRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.ENTREGA_ADMINISTRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.ENTREGA_SOCIO.getSbId())) {
            this.sbTypeDescription = MovementTypes.ENTREGA_SOCIO.getSbDescription();
        } else if (sbType.equals(MovementTypes.VALE_COBRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.VALE_COBRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.VALE_AYUDANTE.getSbId())) {
            this.sbTypeDescription = MovementTypes.VALE_AYUDANTE.getSbDescription();
        } else if (sbType.equals(MovementTypes.PAGO_COBRADOR.getSbId())) {
            this.sbTypeDescription = MovementTypes.PAGO_COBRADOR.getSbDescription();
        } else if (sbType.equals(MovementTypes.PAGO_AYUDANTE.getSbId())) {
            this.sbTypeDescription = MovementTypes.PAGO_AYUDANTE.getSbDescription();
        } else if (sbType.equals(MovementTypes.PAGO_SUELDO.getSbId())) {
            this.sbTypeDescription = MovementTypes.PAGO_SUELDO.getSbDescription();
        } else if (sbType.equals(MovementTypes.SOBRANTE.getSbId())) {
            this.sbTypeDescription = MovementTypes.SOBRANTE.getSbDescription();
        } else if (sbType.equals(MovementTypes.ABONO_CREDITOSIMPLE.getSbId())) {
            this.sbTypeDescription = MovementTypes.ABONO_CREDITOSIMPLE.getSbDescription();
        } else {
            this.sbTypeDescription = "Movimiento";
        }
    }

    public String getSbTypeDescription() {
        return sbTypeDescription;
    }

    public String getDisplayValue() {
        return Utilities.getFormattedValue(flValue);
    }

    public String getSbStatus() {
        return sbStatus;
    }

    public void setSbStatus(String sbStatus) {
        this.sbStatus = sbStatus;
    }

}
