package com.cristiancollazos.paybird.print;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class NewCreditPrint {

    private final String TAG = NewCreditPrint.class.getSimpleName();
    private CreditDTO objCreditDTO;
    private String sbPeriod, sbPrint;

    public NewCreditPrint(CreditDTO objCreditDTO) {
        this.objCreditDTO = objCreditDTO;
        buildPrint();
    }

    private void buildPrint() {
        if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_DIARIO) {
            sbPeriod = "Diario";
        } else if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_SEMANAL) {
            sbPeriod = "Semanal";
        } else if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_QUINCENAL) {
            sbPeriod = "Quincenal";
        } else {
            sbPeriod = "Mensual";
        }

        sbPrint = "";
        sbPrint += "Inversionista Financiera \n";
        sbPrint += "Fecha " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n";
        sbPrint += "--------------------------------\n";
        sbPrint += "Comprobante de credito\n";
        sbPrint += "--------------------------------\n";
        sbPrint += "Codigo: " + objCreditDTO.getNuCode() + "\n";

        if (("Cliente: " + objCreditDTO.getSbCustomerName()).length() > 32) {
            sbPrint += ("Cliente: " + objCreditDTO.getSbCustomerName()).substring(0, 32) + "\n";
        } else {
            sbPrint += "Cliente: " + objCreditDTO.getSbCustomerName() + "\n";
        }

        sbPrint += "Fecha del credito: " + new SimpleDateFormat("dd/MM/yyyy")
                .format(objCreditDTO.getDtCreatedDate()) + "\n";
        sbPrint += "\n";
        sbPrint += "Valor del credito: " + Utilities.getFormattedValue(objCreditDTO.getFlValue()) + "\n";
        sbPrint += "Porcentaje interes: " + objCreditDTO.getFlInterest() + "\n";
        sbPrint += "Saldo: " + Utilities.getFormattedValue(objCreditDTO.getFlRemainder()) + "\n";
        sbPrint += "\n";
        sbPrint += "Duracion credito: " + objCreditDTO.getNuLength() + " dias\n";
        sbPrint += "Periodo: " + sbPeriod + "\n";
        sbPrint += "Valor cuota: " + Utilities.getFormattedValue(objCreditDTO.getFlInstallmentValue()) + "\n";
        sbPrint += "Cantidad cuotas: " + objCreditDTO.getNuInstallmentQuantity() + "\n";
        sbPrint += "Proximo pago: " + new SimpleDateFormat("dd/MM/yyyy")
                .format(objCreditDTO.getDtNextPayment()) + "\n";
        sbPrint += "--------------------------------\n";
    }

    public String getPrint() {
        return sbPrint;
    }

}
