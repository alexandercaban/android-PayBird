package com.cristiancollazos.paybird.repository.impl;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Parser;
import com.cristiancollazos.paybird.misc.SocketServer;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.RouteOrderRepository;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;

import java.util.ArrayList;
import java.util.List;

public class RouteOrderRepositoryImpl implements RouteOrderRepository {

    private final String TAG = RouteOrderRepositoryImpl.class.getSimpleName();

    @Override
    public List<RouteItemDTO> getRouteOrderByRoute(Integer nuRouteCode) throws AppException {
        Log.i(TAG, "getRouteOrderByRoute executed");
        Log.i(TAG, "getRouteOrderByRoute.nuRouteCode: " + nuRouteCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_ORDEN_RUTA;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuRouteCode;

        Log.i(TAG, "getRouteOrderByRoute :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getRouteOrderByRoute.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<RouteItemDTO> lstRouteOrderDTO = new ArrayList<>();

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                RouteItemDTO objRouteItemDTO = new RouteItemDTO();
                objRouteItemDTO.setNuOrder(objParserSepaCampo.nextInt());
                objRouteItemDTO.setSbCustomerName(objParserSepaCampo.nextString());
                String sbAddress = objParserSepaCampo.nextString();
                sbAddress = (!sbAddress.equals("null") ? sbAddress : Constants.APP_NAME);
                objRouteItemDTO.setSbAddress(sbAddress);

                lstRouteOrderDTO.add(objRouteItemDTO);
            }

            return lstRouteOrderDTO;
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
    public Boolean setReorderRoute(Integer nuRouteCode,
                                   Integer nuRouteOrderFrom,
                                   Integer nuRouteOrderTo) throws AppException {
        Log.i(TAG, "setReorderRoute executed");
        Log.i(TAG, "setReorderRoute.nuRouteCode: " + nuRouteCode);
        Log.i(TAG, "setReorderRoute.nuRouteOrderFrom: " + nuRouteOrderFrom);
        Log.i(TAG, "setReorderRoute.nuRouteOrderTo: " + nuRouteOrderTo);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.CAMBIAR_ORDEN_RUTA;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuRouteCode;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuRouteOrderFrom;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += nuRouteOrderTo;

        Log.i(TAG, "setReorderRoute :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "setReorderRoute.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        String sbStatus = objParserSepaRegistro.nextString();

        if (sbStatus.trim().equals("0")) {
            return true;
        } else {
            throw new AppException(ErrorConstants.E0011);
        }
    }

    @Override
    public List<RouteItemDTO> getRouteOrderByRouteWithOffset(Integer nuRouteCode)
            throws AppException {
        Log.i(TAG, "getRouteOrderByRouteWithOffset executed");
        Log.i(TAG, "getRouteOrderByRouteWithOffset.nuRouteCode: " + nuRouteCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_ORDEN_RUTA;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuRouteCode;

        Log.i(TAG, "getRouteOrderByRouteWithOffset :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getRouteOrderByRouteWithOffset.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        List<RouteItemDTO> lstRouteOrderDTO = new ArrayList<>();
        lstRouteOrderDTO.add(new RouteItemDTO(0, "PRIMERO", null));

        if (nuStatus == 0) {
            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                RouteItemDTO objRouteItemDTO = new RouteItemDTO();
                objRouteItemDTO.setNuOrder(objParserSepaCampo.nextInt());
                objRouteItemDTO.setSbCustomerName(objParserSepaCampo.nextString());
                String sbAddress = objParserSepaCampo.nextString();
                sbAddress = (!sbAddress.equals("null") ? sbAddress : Constants.APP_NAME);
                objRouteItemDTO.setSbAddress(sbAddress);

                lstRouteOrderDTO.add(objRouteItemDTO);
            }
        }

        return lstRouteOrderDTO;

        /*else {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuErrorCode = nuStatus;
            objParserSepaCampo.nextInt();
            String sbMessage = objParserSepaCampo.nextString();
            String sbRecommend = objParserSepaCampo.nextString();

            throw new AppException(nuErrorCode, sbMessage, sbRecommend);
        }*/
    }
}
