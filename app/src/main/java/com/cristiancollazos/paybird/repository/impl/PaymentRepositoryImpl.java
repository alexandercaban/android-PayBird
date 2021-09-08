package com.cristiancollazos.paybird.repository.impl;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Parser;
import com.cristiancollazos.paybird.misc.SocketServer;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.PaymentRepository;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {

    private final String TAG = PaymentRepositoryImpl.class.getSimpleName();

    @Override
    public List<PendingPaymentDTO> getPendingPaymentsByDateAndFilters(String sbFormattedDate,
                                                                      Integer nuRouteCode,
                                                                      String sbCustomerName,
                                                                      Integer nuCreditCode) throws AppException {
        Log.i(TAG, "getPendingPaymentsByDateAndFilters executed");
        Log.i(TAG, "getPendingPaymentsByDateAndFilters.sbFormattedDate: " + sbFormattedDate);
        Log.i(TAG, "getPendingPaymentsByDateAndFilters.nuRouteCode: " + nuRouteCode);
        Log.i(TAG, "getPendingPaymentsByDateAndFilters.sbCustomerName: " + sbCustomerName);
        Log.i(TAG, "getPendingPaymentsByDateAndFilters.nuCreditCode: " + nuCreditCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_ABONOS_FECHA_FILTROS;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += sbFormattedDate;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuRouteCode;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += (sbCustomerName == null? "" : sbCustomerName);
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += (nuCreditCode == null? "" : nuCreditCode);

        Log.i(TAG, "getPendingPaymentsByDateAndFilters :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getPendingPaymentsByDateAndFilters.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<PendingPaymentDTO> lstPendingPaymentsDTO = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                PendingPaymentDTO objPendingPaymentDTO = new PendingPaymentDTO();
                objPendingPaymentDTO.setNuCode(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setSbCustomerDocument(objParserSepaCampo.nextString());
                objPendingPaymentDTO.setSbCustomerName(objParserSepaCampo.nextString());
                String sbHomeAddress = objParserSepaCampo.nextString();
                sbHomeAddress = (!sbHomeAddress.equals("null")? sbHomeAddress : Constants.APP_NAME);
                objPendingPaymentDTO.setSbHomeAddress(sbHomeAddress);
                objPendingPaymentDTO.setDtCreditDate(objParserSepaCampo.nextDate());
                objPendingPaymentDTO.setFlCreditValue(objParserSepaCampo.nextFloat());
                objPendingPaymentDTO.setFlInterest(objParserSepaCampo.nextFloat());
                objPendingPaymentDTO.setFlRemainder(objParserSepaCampo.nextFloat());
                objPendingPaymentDTO.setDtLastPayment(objParserSepaCampo.nextDate());
                objPendingPaymentDTO.setNuCreditLength(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setNuPeriod(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setDtNextPayment(objParserSepaCampo.nextDate());
                objPendingPaymentDTO.setFlInstallmentValue(objParserSepaCampo.nextFloat());
                objPendingPaymentDTO.setNuInstallments(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setSbContactInfo(objParserSepaCampo.nextString());
                objPendingPaymentDTO.setNuOrder(Integer.valueOf(objParserSepaCampo.nextString().trim()));

                lstPendingPaymentsDTO.add(objPendingPaymentDTO);
            }

            return lstPendingPaymentsDTO;
        } else {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuErrorCode = nuStatus;
            objParserSepaCampo.nextInt();
            String sbMessage = objParserSepaCampo.nextString();
            String sbRecommend = objParserSepaCampo.nextString();

            throw new AppException(nuErrorCode, sbMessage, sbRecommend);
        }
    }

    @Override
    public Boolean doPayPendingPayment(Integer nuRouteCode,
                                       Integer nuOrder,
                                       Integer nuValueToPay,
                                       String sbNextPaymentDay) throws AppException {
        Log.i(TAG, "doPayPendingPayment executed");
        Log.i(TAG, "doPayPendingPayment.nuRouteCode: " + nuRouteCode);
        Log.i(TAG, "doPayPendingPayment.nuOrder: " + nuOrder);
        Log.i(TAG, "doPayPendingPayment.nuValueToPay: " + nuValueToPay);
        Log.i(TAG, "doPayPendingPayment.sbNextPaymentDay: " + sbNextPaymentDay);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.ABONAR;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuRouteCode;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuOrder;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuValueToPay;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += sbNextPaymentDay;

        Log.i(TAG, "doPayPendingPayment :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "doPayPendingPayment.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        String sbStatus = objParserSepaRegistro.nextString();

        if (sbStatus.trim().equals("0")) {
            return true;
        } else {
            throw new AppException(ErrorConstants.E0007);
        }
    }

    @Override
    public List<MovementDTO> getMovementsByRoute(Integer nuRouteCode) throws AppException {
        Log.i(TAG, "getMovementsByRoute executed");
        Log.i(TAG, "getMovementsByRoute.nuRouteCode: " + nuRouteCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_MOVIMIENTOS;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuRouteCode;

        Log.i(TAG, "getMovementsByRoute :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getMovementsByRoute.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<MovementDTO> lstMovementsDTO = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                MovementDTO objMovementDTO = new MovementDTO();
                objMovementDTO.setNuCode(objParserSepaCampo.nextInt());
                objMovementDTO.setDtMovementDate(objParserSepaCampo.nextDate());
                objMovementDTO.setSbMovementHour(objParserSepaCampo.nextString());
                objMovementDTO.setSbCustomerName(objParserSepaCampo.nextString());
                objMovementDTO.setNuCreditCode(objParserSepaCampo.nextInt());
                objMovementDTO.setFlValue(objParserSepaCampo.nextFloat());
                objMovementDTO.setSbType(objParserSepaCampo.nextString());

                lstMovementsDTO.add(objMovementDTO);
            }

            return lstMovementsDTO;
        } else {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuErrorCode = nuStatus;
            objParserSepaCampo.nextInt();
            String sbMessage = objParserSepaCampo.nextString();
            String sbRecommend = objParserSepaCampo.nextString();

            throw new AppException(nuErrorCode, sbMessage, sbRecommend);
        }
    }

    @Override
    public Boolean getValidatePaymentPerRefund(Integer nuMovementCode) throws AppException {
        Log.i(TAG, "getValidatePaymentPerRefund executed");
        Log.i(TAG, "getValidatePaymentPerRefund.nuMovementCode: " + nuMovementCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.CONSULTAR_ABONOS_DESEMBOLSO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuMovementCode;

        Log.i(TAG, "getValidatePaymentPerRefund :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getValidatePaymentPerRefund.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        if (nuStatus == 0) {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuValidation = objParserSepaCampo.nextInt();

            if (nuValidation == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuErrorCode = nuStatus;
            objParserSepaCampo.nextInt();
            String sbMessage = objParserSepaCampo.nextString();
            String sbRecommend = objParserSepaCampo.nextString();

            throw new AppException(nuErrorCode, sbMessage, sbRecommend);
        }
    }

    @Override
    public Boolean doNullMovement(Integer nuMovementCode) throws AppException {
        Log.i(TAG, "doNullMovement executed");
        Log.i(TAG, "doNullMovement.nuMovementCode: " + nuMovementCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.ANULAR_MOVIMIENTO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuMovementCode;

        Log.i(TAG, "doNullMovement :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "doNullMovement.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        String sbStatus = objParserSepaRegistro.nextString();

        if (sbStatus.trim().equals("0")) {
            return true;
        } else {
            throw new AppException(ErrorConstants.E0008);
        }
    }

    @Override
    public List<PendingPaymentDTO> getPendingCreditsByFilters(Integer nuRouteCode,
                                                              String sbCustomerName,
                                                              Integer nuCreditCode) throws AppException {
        Log.i(TAG, "getPendingCreditsByFilters executed");
        Log.i(TAG, "getPendingCreditsByFilters.nuRouteCode: " + nuRouteCode);
        Log.i(TAG, "getPendingCreditsByFilters.sbCustomerName: " + sbCustomerName);
        Log.i(TAG, "getPendingCreditsByFilters.nuCreditCode: " + nuCreditCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_CREDITOS_PENDIENTES_FILTROS;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuRouteCode;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += (sbCustomerName == null? "" : sbCustomerName);
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += (nuCreditCode == null? "" : nuCreditCode);

        Log.i(TAG, "getPendingCreditsByFilters :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getPendingCreditsByFilters.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<PendingPaymentDTO> lstPendingPaymentsDTO = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                PendingPaymentDTO objPendingPaymentDTO = new PendingPaymentDTO();
                objPendingPaymentDTO.setNuOrder(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setSbCustomerName(objParserSepaCampo.nextString());
                objPendingPaymentDTO.setNuCode(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setDtCreditDate(objParserSepaCampo.nextDate());
                objPendingPaymentDTO.setFlCreditValue(objParserSepaCampo.nextFloat());
                objPendingPaymentDTO.setFlRemainder(objParserSepaCampo.nextFloat());
                objPendingPaymentDTO.setDtLastPayment(objParserSepaCampo.nextDate());
                objPendingPaymentDTO.setNuCreditLength(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setNuPeriod(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setDtNextPayment(objParserSepaCampo.nextDate());
                objPendingPaymentDTO.setFlInstallmentValue(objParserSepaCampo.nextFloat());
                objPendingPaymentDTO.setNuInstallments(objParserSepaCampo.nextInt());
                objPendingPaymentDTO.setSbCustomerDocument(objParserSepaCampo.nextString());
                objPendingPaymentDTO.setSbContactInfo(objParserSepaCampo.nextString());
                String sbHomeAddress = objParserSepaCampo.nextString();
                sbHomeAddress = (!sbHomeAddress.equals("null")? sbHomeAddress : Constants.APP_NAME);
                objPendingPaymentDTO.setSbHomeAddress(sbHomeAddress);
                objPendingPaymentDTO.setFlInterest(objParserSepaCampo.nextFloat());

                lstPendingPaymentsDTO.add(objPendingPaymentDTO);
            }

            return lstPendingPaymentsDTO;
        } else {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuErrorCode = nuStatus;
            objParserSepaCampo.nextInt();
            String sbMessage = objParserSepaCampo.nextString();
            String sbRecommend = objParserSepaCampo.nextString();

            throw new AppException(nuErrorCode, sbMessage, sbRecommend);
        }
    }

}
