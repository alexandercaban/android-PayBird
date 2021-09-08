package com.cristiancollazos.paybird.print;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class PaymentPrint {

    private final String TAG = PaymentPrint.class.getSimpleName();
    private PendingPaymentDTO objPendingPaymentDTO;
    private String sbPrint;
    private Float flCreditInterestValue, flCurrentPayment, flPaymentValue, flRealPaymentValue;
    private String sbPeriod;
    private Integer nuCreditLength, nuCurrentPayment;
    private Date dtNextPayment;

    public PaymentPrint(PendingPaymentDTO objPendingPaymentDTO, Float flRealPaymentValue,
                        Date dtNextPayment) {
        this.objPendingPaymentDTO = objPendingPaymentDTO;
        this.flRealPaymentValue = flRealPaymentValue;
        this.dtNextPayment = dtNextPayment;
        buildPrint();
    }

    public void buildPrint() {
        NumberFormat objNumberFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        objNumberFormatter.setCurrency(Currency.getInstance("COP"));

        flCreditInterestValue = objPendingPaymentDTO.getFlCreditValue() +
                ((objPendingPaymentDTO.getFlCreditValue() * objPendingPaymentDTO.getFlInterest()) / 100);

        if (objPendingPaymentDTO.getNuPeriod() == Constants.TIEMPOPAGO_DIARIO) {
            nuCreditLength = objPendingPaymentDTO.getNuCreditLength();
            sbPeriod = "Diario";
            flCurrentPayment = ((flCreditInterestValue - objPendingPaymentDTO.getFlRemainder()) /
                    (flCreditInterestValue / nuCreditLength));
        } else if (objPendingPaymentDTO.getNuPeriod() == Constants.TIEMPOPAGO_SEMANAL) {
            nuCreditLength = objPendingPaymentDTO.getNuCreditLength() / 7;
            sbPeriod = "Semanal";
            flCurrentPayment = ((flCreditInterestValue - objPendingPaymentDTO.getFlRemainder()) /
                    (flCreditInterestValue / nuCreditLength));
        } else if (objPendingPaymentDTO.getNuPeriod() == Constants.TIEMPOPAGO_QUINCENAL) {
            nuCreditLength = objPendingPaymentDTO.getNuCreditLength() / 14;
            sbPeriod = "Quincenal";
            flCurrentPayment = ((flCreditInterestValue - objPendingPaymentDTO.getFlRemainder()) /
                    (flCreditInterestValue / nuCreditLength));
        } else {
            nuCreditLength = objPendingPaymentDTO.getNuCreditLength() / 30;
            sbPeriod = "Mensual";
            flCurrentPayment = ((flCreditInterestValue - objPendingPaymentDTO.getFlRemainder()) /
                    (flCreditInterestValue / nuCreditLength));
        }

        nuCurrentPayment = Math.round(flCurrentPayment) + 1;
        //flPaymentValue = (objPendingPaymentDTO.getFlCreditValue() + flCreditInterestValue) / nuCreditLength;
        flPaymentValue = objPendingPaymentDTO.getFlInstallmentValue();

        sbPrint = "";
        sbPrint += "Inversionista Financiera \n";
        sbPrint += "Fecha " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n";
        sbPrint += "--------------------------------\n";
        sbPrint += "Numero de Recibo: " + nuCurrentPayment + "\n";
        sbPrint += "--------------------------------\n";
        sbPrint += "Codigo: " + objPendingPaymentDTO.getNuCode() + "\n";
        sbPrint += "Documento: " + objPendingPaymentDTO.getSbCustomerDocument() + "\n";

        if (("Cliente: " + objPendingPaymentDTO.getSbCustomerName()).length() > 32) {
            sbPrint += ("Cliente: " + objPendingPaymentDTO.getSbCustomerName()).substring(0, 32) + "\n";
        } else {
            sbPrint += "Cliente: " + objPendingPaymentDTO.getSbCustomerName() + "\n";
        }

        if (("Direccion: " + objPendingPaymentDTO.getSbHomeAddress()).length() > 32) {
            sbPrint += ("Direccion: " + objPendingPaymentDTO.getSbHomeAddress()).substring(0, 32) + "\n";
        } else {
            sbPrint += "Direccion: " + objPendingPaymentDTO.getSbHomeAddress() + "\n";
        }

        sbPrint += "Telefonos: " + objPendingPaymentDTO.getSbContactInfo() + "\n";
        sbPrint += "Fecha del Credito: " + new SimpleDateFormat("dd/MM/yyyy")
                .format(objPendingPaymentDTO.getDtCreditDate()) + "\n";
        sbPrint += "Saldo: " + objNumberFormatter.format(objPendingPaymentDTO.getFlRemainder()
                - flRealPaymentValue).substring(3) + "\n";
        sbPrint += "Fecha Cuota: " + new SimpleDateFormat("dd/MM/yyyy")
                .format(objPendingPaymentDTO.getDtNextPayment()) + "\n";
        sbPrint += "Ultimo Abono: " + new SimpleDateFormat("dd/MM/yyyy")
                .format(objPendingPaymentDTO.getDtLastPayment()) + "\n";
        //sbPrint += "Plazo: " + objPendingPaymentDTO.getNuCreditLength() + "\n";
        sbPrint += "Proximo Abono: " + new SimpleDateFormat("dd/MM/yyyy")
                .format(dtNextPayment) + "\n";
        sbPrint += "Periodo: " + sbPeriod + "\n";
        sbPrint += "Valor Cuota: " + objNumberFormatter.format(flPaymentValue)
                .substring(3) + "\n";
        sbPrint += "Total cuotas a Pagar: " + nuCreditLength + "\n";
        sbPrint += "--------------------------------\n";
    }

    public String getPrint() {
        return sbPrint;
    }

}
