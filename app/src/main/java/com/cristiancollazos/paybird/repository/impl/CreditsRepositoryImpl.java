package com.cristiancollazos.paybird.repository.impl;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Parser;
import com.cristiancollazos.paybird.misc.SocketServer;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.CreditsRepository;
import com.cristiancollazos.paybird.repository.CustomerRepository;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PeriodDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CreditsRepositoryImpl implements CreditsRepository {

    private final String TAG = CreditsRepositoryImpl.class.getSimpleName();

    @Override
    public List<CreditDTO> getCreditsByCustomer(Integer nuCustomerCode) throws AppException {
        Log.i(TAG, "getCreditsByCustomer executed");
        Log.i(TAG, "getCreditsByCustomer.nuCustomerCode: " + nuCustomerCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_CREDITOS_CLIENTE;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuCustomerCode;

        Log.i(TAG, "getCreditsByCustomer :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getCreditsByCustomer.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<CreditDTO> lstCreditDTO = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                CreditDTO objCreditDTO = new CreditDTO();
                objCreditDTO.setNuCode(objParserSepaCampo.nextInt());
                objCreditDTO.setDtCreatedDate(objParserSepaCampo.nextDate());
                objCreditDTO.setFlValue(objParserSepaCampo.nextFloat());
                objCreditDTO.setFlInterest(objParserSepaCampo.nextFloat());
                objCreditDTO.setSbStatus(objParserSepaCampo.nextString());
                objCreditDTO.setFlRemainder(objParserSepaCampo.nextFloat());
                objCreditDTO.setDtLastPayment(objParserSepaCampo.nextDate());
                objParserSepaCampo.nextString();
                objCreditDTO.setNuLength(objParserSepaCampo.nextInt());
                objCreditDTO.setNuPeriod(objParserSepaCampo.nextInt());
                objParserSepaCampo.nextString();
                objParserSepaCampo.nextString();
                objParserSepaCampo.nextString();
                objCreditDTO.setNuInstallmentQuantity(objParserSepaCampo.nextInt());
                objCreditDTO.setSbCoOwner(objParserSepaCampo.nextString());

                lstCreditDTO.add(objCreditDTO);
            }

            return lstCreditDTO;
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
    public List<MovementDTO> getMovementsByCredit(Integer nuCreditCode) throws AppException {
        Log.i(TAG, "getMovementsByCredit executed");
        Log.i(TAG, "getMovementsByCredit.nuCreditCode: " + nuCreditCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_MOVIMIENTOS_CREDITOS;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuCreditCode;

        Log.i(TAG, "getMovementsByCredit :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getMovementsByCredit.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<MovementDTO> lstMovementDTO = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                MovementDTO objMovementDTO = new MovementDTO();
                objParserSepaCampo.nextString();
                objMovementDTO.setDtMovementDate(objParserSepaCampo.nextDate());
                objMovementDTO.setSbMovementHour(objParserSepaCampo.nextString());
                objParserSepaCampo.nextString();
                objParserSepaCampo.nextString();
                objMovementDTO.setFlValue(objParserSepaCampo.nextFloat());
                objParserSepaCampo.nextString();
                objParserSepaCampo.nextString();
                objMovementDTO.setSbStatus(objParserSepaCampo.nextString());

                lstMovementDTO.add(objMovementDTO);
            }

            return lstMovementDTO;
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
    public List<PeriodDTO> getPeriods() throws AppException {
        Log.i(TAG, "getPeriods executed");

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_PERIODOS;
        sbRequest += Constants.SEPARADOR_REGISTRO;

        Log.i(TAG, "getPeriods :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getPeriods.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<PeriodDTO> lstPeriodDTO = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                PeriodDTO objPeriodDTO = new PeriodDTO();
                objPeriodDTO.setNuCode(objParserSepaCampo.nextInt());
                objPeriodDTO.setSbDefinition(objParserSepaCampo.nextString());

                lstPeriodDTO.add(objPeriodDTO);
            }

            return lstPeriodDTO;
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
    public Boolean setNewCredit(CreditDTO objCreditDTO,
                                Integer nuRouteCode,
                                Integer nuRouteOrderPosition,
                                Integer nuUserCode) throws AppException {
        SimpleDateFormat objFormatter = new SimpleDateFormat("yyyy-MM-dd");

        Log.i(TAG, "setNewCredit executed");
        Log.i(TAG, "setNewCredit.nuUserCode: " + nuUserCode);
        Log.i(TAG, "setNewCredit.objCreditDTO.nuCustomerCode: "
                + objCreditDTO.getNuCustomerCode());
        Log.i(TAG, "setNewCredit.objCreditDTO.flValue: " + objCreditDTO.getFlValue());
        Log.i(TAG, "setNewCredit.objCreditDTO.flInterest: " + objCreditDTO.getFlInterest());
        Log.i(TAG, "setNewCredit.objCreditDTO.flRemainder: " + objCreditDTO.getFlRemainder());
        Log.i(TAG, "setNewCredit.nuRouteOrderPosition: " + nuRouteOrderPosition);
        Log.i(TAG, "setNewCredit.nuRouteCode: " + nuRouteCode);
        Log.i(TAG, "setNewCredit.objCreditDTO.dtCreatedDate: "
                + objFormatter.format(objCreditDTO.getDtCreatedDate()));
        Log.i(TAG, "setNewCredit.objCreditDTO.nuLength: " + objCreditDTO.getNuLength());
        Log.i(TAG, "setNewCredit.objCreditDTO.nuPeriod: " + objCreditDTO.getNuPeriod());
        Log.i(TAG, "setNewCredit.objCreditDTO.nuCoOwnerCode: " + objCreditDTO.getNuCoOwnerCode());
        Log.i(TAG, "setNewCredit.objCreditDTO.dtNextPayment: "
                + objFormatter.format(objCreditDTO.getDtNextPayment()));
        Log.i(TAG, "setNewCredit.objCreditDTO.flInstallmentValue: "
                + objCreditDTO.getFlInstallmentValue());
        Log.i(TAG, "setNewCredit.objCreditDTO.nuInstallmentQuantity: "
                + objCreditDTO.getNuInstallmentQuantity());

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.REGISTRAR_CREDITO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuUserCode;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getNuCustomerCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getFlValue();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getFlInterest();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getFlRemainder();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuRouteOrderPosition;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuRouteCode;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objFormatter.format(objCreditDTO.getDtCreatedDate());
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getNuLength();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getNuPeriod();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getNuCoOwnerCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objFormatter.format(objCreditDTO.getDtNextPayment());
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getFlInstallmentValue();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCreditDTO.getNuInstallmentQuantity();

        Log.i(TAG, "setNewCredit :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "setNewCredit.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        String sbStatus = objParserSepaRegistro.nextString();

        if (sbStatus.trim().equals("0")) {
            return true;
        } else {
            throw new AppException(ErrorConstants.E0017);
        }
    }

    @Override
    public CreditDTO getRecentlyCreatedCredit(Integer nuCustomerCode) throws AppException {
        Log.i(TAG, "getRecentlyCreatedCredit executed");
        Log.i(TAG, "getRecentlyCreatedCredit.nuCustomerCode: " + nuCustomerCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_CREDITO_RECIEN_CREADO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuCustomerCode;

        Log.i(TAG, "getRecentlyCreatedCredit :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getRecentlyCreatedCredit.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        if (nuStatus == 0) {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            CreditDTO objCreditDTO = new CreditDTO();
            objCreditDTO.setNuCode(objParserSepaCampo.nextInt());
            objCreditDTO.setDtCreatedDate(objParserSepaCampo.nextDate());
            objCreditDTO.setFlValue(objParserSepaCampo.nextFloat());
            objCreditDTO.setFlInterest(objParserSepaCampo.nextFloat());
            objCreditDTO.setFlRemainder(objParserSepaCampo.nextFloat());
            objCreditDTO.setSbCustomerName(objParserSepaCampo.nextString());
            objCreditDTO.setNuLength(objParserSepaCampo.nextInt());
            objCreditDTO.setNuPeriod(objParserSepaCampo.nextInt());
            objCreditDTO.setDtNextPayment(objParserSepaCampo.nextDate());
            objCreditDTO.setFlInstallmentValue(objParserSepaCampo.nextFloat());
            objCreditDTO.setNuInstallmentQuantity(objParserSepaCampo.nextInt());

            return objCreditDTO;
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
