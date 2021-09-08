package com.cristiancollazos.paybird.repository.impl;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Parser;
import com.cristiancollazos.paybird.misc.SocketServer;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.CityRepository;
import com.cristiancollazos.paybird.repository.dto.CityDTO;

import java.util.ArrayList;
import java.util.List;

public class CityRepositoryImpl implements CityRepository {

    private final String TAG = CityRepositoryImpl.class.getSimpleName();

    @Override
    public List<CityDTO> getCities() throws AppException{
        Log.i(TAG, "getCities executed");

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_CIUDADES;

        Log.i(TAG, "getCities :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getCities.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        if (nuStatus == 0) {
            List<CityDTO> lstCitiesDTO = new ArrayList<>();

            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                CityDTO objCityDTO = new CityDTO(
                        objParserSepaCampo.nextInt(),
                        objParserSepaCampo.nextString());

                lstCitiesDTO.add(objCityDTO);
            }

            return lstCitiesDTO;
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
