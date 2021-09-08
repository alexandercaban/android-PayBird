package com.cristiancollazos.paybird.repository.impl;

import android.util.Log;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Parser;
import com.cristiancollazos.paybird.misc.SocketServer;
import com.cristiancollazos.paybird.repository.AuthenticationRepository;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private final String TAG = AuthenticationRepositoryImpl.class.getSimpleName();

    @Override
    public UserDTO doLogin(String sbUser,
                        String sbPassword) throws AppException {
        Log.i(TAG, "doLogin executed");
        Log.i(TAG, "doLogin.sbUser: " + sbUser);
        Log.i(TAG, "doLogin.sbPassword: " + sbPassword);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Services.VALIDAR_USUARIO;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += sbUser;
        sbRequest += Constants.SEPARADOR_CAMPO;
        sbRequest += sbPassword;

        Log.i(TAG, "doLogin :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "doLogin.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        if (nuStatus == 0) {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            UserDTO objUserDTO = new UserDTO(objParserSepaCampo.nextInt(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextInt(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextDate(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString(),
                    objParserSepaCampo.nextString());

            return objUserDTO;
        } else {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuErrorCode = objParserSepaCampo.nextInt();
            String sbMessage = objParserSepaCampo.nextString();
            String sbRecommend = objParserSepaCampo.nextString();

            throw new AppException(nuErrorCode, sbMessage, sbRecommend);
        }
    }

    @Override
    public List<RouteDTO> getRoute(Integer nuUserCode) throws AppException {
        Log.i(TAG, "getRoute executed");
        Log.i(TAG, "getRoute.nuUserCode: " + nuUserCode);

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Services.OBTENER_RUTA;
        sbRequest += Constants.SEPARADOR_REGISTRO;
        sbRequest += nuUserCode;

        Log.i(TAG, "getRoute :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getRoute.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        if (nuStatus == 0) {
            List<RouteDTO> lstRoutesDTO = new ArrayList<>();

            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                RouteDTO objRouteDTO = new RouteDTO(
                        objParserSepaCampo.nextInt(),
                        objParserSepaCampo.nextString());

                lstRoutesDTO.add(objRouteDTO);
            }

            return lstRoutesDTO;
        } else {
            Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                    Constants.SEPARADOR_CAMPO);

            Integer nuErrorCode = objParserSepaCampo.nextInt();
            String sbMessage = objParserSepaCampo.nextString();
            String sbRecommend = objParserSepaCampo.nextString();

            throw new AppException(nuErrorCode, sbMessage, sbRecommend);
        }
    }

}
