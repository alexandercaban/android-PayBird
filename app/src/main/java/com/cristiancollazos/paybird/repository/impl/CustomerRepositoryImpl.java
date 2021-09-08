package com.cristiancollazos.paybird.repository.impl;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Parser;
import com.cristiancollazos.paybird.misc.SocketServer;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.CustomerRepository;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {

    private final String TAG = CustomerRepositoryImpl.class.getSimpleName();

    @Override
    public CustomerDTO getCustomerByDocument(String sbDocument) throws AppException {
        Log.i(TAG, "getCustomerByDocument executed");
        Log.i(TAG, "getCustomerByDocument.sbDocument: " + sbDocument);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_USUARIO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += sbDocument;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += "S";

        Log.i(TAG, "getCustomerByDocument :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getCustomerByDocument.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        if (nuStatus == 0) {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            CustomerDTO objCustomer = new CustomerDTO(
                    objParserSepaCampo.nextInt(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextInt(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextInt(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextInt(),
                    Integer.valueOf(objParserSepaCampo.nextString().trim()));

            objCustomer.setSbNeighborhoodName(objParserSepaCampo.nextString());
            objCustomer.setSbCityName(objParserSepaCampo.nextString());

            return objCustomer;
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
    public List<CustomerDTO> getCustomersByRoute(Integer nuRoute) throws AppException {
        Log.i(TAG, "getCustomersByRoute executed");
        Log.i(TAG, "getCustomersByRoute.nuRoute: " + nuRoute);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_USUARIOS_RUTA;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += "S";
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuRoute;

        Log.i(TAG, "getCustomersByRoute :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getCustomersByRoute.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<CustomerDTO> lstCustomers = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                CustomerDTO objCustomer = new CustomerDTO();
                objCustomer.setNuCode(objParserSepaCampo.nextInt());
                objCustomer.setSbDocumentType(objParserSepaCampo.nextString());
                objCustomer.setSbDocument(objParserSepaCampo.nextString());
                objCustomer.setSbName(objParserSepaCampo.nextString());
                objCustomer.setSbLastName(objParserSepaCampo.nextString());

                lstCustomers.add(objCustomer);
            }

            return lstCustomers;
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
    public Boolean setNewCustomer(CustomerDTO objCustomerDTO) throws AppException {
        Log.i(TAG, "setNewCustomer executed");
        Log.i(TAG, "setNewCustomer.sbDocumentType: " + objCustomerDTO.getSbDocumentType());
        Log.i(TAG, "setNewCustomer.sbDocument: " + objCustomerDTO.getSbDocument());
        Log.i(TAG, "setNewCustomer.sbName: " + objCustomerDTO.getSbName());
        Log.i(TAG, "setNewCustomer.sbLastName: " + objCustomerDTO.getSbLastName());
        Log.i(TAG, "setNewCustomer.sbHomeAddress: " + objCustomerDTO.getSbHomeAddress());
        Log.i(TAG, "setNewCustomer.nuHomeCityCode: " + objCustomerDTO.getNuHomeCityCode());
        Log.i(TAG, "setNewCustomer.sbHomePhoneNumber: " + objCustomerDTO.getSbHomePhoneNumber());
        Log.i(TAG, "setNewCustomer.sbMobileNumber: " + objCustomerDTO.getSbMobileNumber());
        Log.i(TAG, "setNewCustomer.sbWorkAddress: " + objCustomerDTO.getSbWorkAddress());
        Log.i(TAG, "setNewCustomer.nuWorkCityCode: " + objCustomerDTO.getNuWorkCityCode());
        Log.i(TAG, "setNewCustomer.sbWorkPhoneNumber: " + objCustomerDTO.getSbWorkPhoneNumber());
        Log.i(TAG, "setNewCustomer.sbCompanyName: " + objCustomerDTO.getSbCompanyName());
        Log.i(TAG, "setNewCustomer.nuRouteCode: " + objCustomerDTO.getNuRouteCode());
        Log.i(TAG, "setNewCustomer.nuNeighborhoodCode: " + objCustomerDTO.getNuNeighborhoodCode());
        Log.i(TAG, "setNewCustomer.nuNeighborhoodCode: " + objCustomerDTO.getNuNeighborhoodCode());

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.REGISTRAR_USUARIO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += "-1";
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbDocumentType();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbDocument();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbName();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbLastName();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbHomeAddress();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuHomeCityCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbHomePhoneNumber();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbMobileNumber();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbWorkAddress();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuWorkCityCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbWorkPhoneNumber();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbCompanyName();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += "S";
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuRouteCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuNeighborhoodCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuNeighborhoodCode();

        Log.i(TAG, "setNewCustomer :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "setNewCustomer.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        String sbStatus = objParserSepaRegistro.nextString();

        if (sbStatus.trim().equals("0")) {
            return true;
        } else {
            throw new AppException(ErrorConstants.E0005);
        }
    }

    @Override
    public Boolean setEditCustomer(CustomerDTO objCustomerDTO) throws AppException {
        Log.i(TAG, "setEditCustomer executed");
        Log.i(TAG, "setEditCustomer.nuCode: " + objCustomerDTO.getNuCode());
        Log.i(TAG, "setEditCustomer.sbDocumentType: " + objCustomerDTO.getSbDocumentType());
        Log.i(TAG, "setEditCustomer.sbDocument: " + objCustomerDTO.getSbDocument());
        Log.i(TAG, "setEditCustomer.sbName: " + objCustomerDTO.getSbName());
        Log.i(TAG, "setEditCustomer.sbLastName: " + objCustomerDTO.getSbLastName());
        Log.i(TAG, "setEditCustomer.sbHomeAddress: " + objCustomerDTO.getSbHomeAddress());
        Log.i(TAG, "setEditCustomer.nuHomeCityCode: " + objCustomerDTO.getNuHomeCityCode());
        Log.i(TAG, "setEditCustomer.sbHomePhoneNumber: " + objCustomerDTO.getSbHomePhoneNumber());
        Log.i(TAG, "setEditCustomer.sbMobileNumber: " + objCustomerDTO.getSbMobileNumber());
        Log.i(TAG, "setEditCustomer.sbWorkAddress: " + objCustomerDTO.getSbWorkAddress());
        Log.i(TAG, "setEditCustomer.nuWorkCityCode: " + objCustomerDTO.getNuWorkCityCode());
        Log.i(TAG, "setEditCustomer.sbWorkPhoneNumber: " + objCustomerDTO.getSbWorkPhoneNumber());
        Log.i(TAG, "setEditCustomer.sbCompanyName: " + objCustomerDTO.getSbCompanyName());
        Log.i(TAG, "setEditCustomer.nuRouteCode: " + objCustomerDTO.getNuRouteCode());
        Log.i(TAG, "setEditCustomer.nuNeighborhoodCode: " + objCustomerDTO.getNuNeighborhoodCode());
        Log.i(TAG, "setEditCustomer.nuNeighborhoodCode: " + objCustomerDTO.getNuNeighborhoodCode());

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();
        sbRequest += Service.ACTUALIZAR_USUARIO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += objCustomerDTO.getNuCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbDocument();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbDocumentType();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbName();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbLastName();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbHomeAddress();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuHomeCityCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbHomePhoneNumber();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbMobileNumber();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbCompanyName();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbWorkAddress();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuWorkCityCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getSbWorkPhoneNumber();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuRouteCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += objCustomerDTO.getNuNeighborhoodCode();
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += "S";

        Log.i(TAG, "setEditCustomer :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "setEditCustomer.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        String sbStatus = objParserSepaRegistro.nextString();

        if (sbStatus.trim().equals("0")) {
            return true;
        } else {
            throw new AppException(ErrorConstants.E0006);
        }
    }

    @Override
    public List<CustomerDTO> getSimpleCustomersByRouteFilter(Integer nuRoute,
                                                             String sbCustomerName) throws AppException {
        Log.i(TAG, "getSimpleCustomersByRoute executed");
        Log.i(TAG, "getSimpleCustomersByRoute.nuRoute: " + nuRoute);
        Log.i(TAG, "getSimpleCustomersByRoute.sbCustomerName: " + sbCustomerName);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_USUARIOS_SENCILLO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += "S";
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuRoute;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += (sbCustomerName == null? "" : sbCustomerName);

        Log.i(TAG, "getSimpleCustomersByRoute :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getSimpleCustomersByRoute.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<CustomerDTO> lstCustomers = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                CustomerDTO objCustomer = new CustomerDTO();
                objCustomer.setNuCode(objParserSepaCampo.nextInt());
                objCustomer.setSbDocumentType(objParserSepaCampo.nextString());
                objCustomer.setSbDocument(objParserSepaCampo.nextString());
                objCustomer.setSbName(objParserSepaCampo.nextString());
                objCustomer.setSbLastName(objParserSepaCampo.nextString());

                lstCustomers.add(objCustomer);
            }

            return lstCustomers;
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
