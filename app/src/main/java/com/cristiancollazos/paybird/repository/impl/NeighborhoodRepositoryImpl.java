package com.cristiancollazos.paybird.repository.impl;

import android.util.Log;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Parser;
import com.cristiancollazos.paybird.misc.SocketServer;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.NeighborhoodRepository;
import com.cristiancollazos.paybird.repository.dto.NeighborhoodDTO;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodRepositoryImpl implements NeighborhoodRepository {

    private final String TAG = NeighborhoodRepositoryImpl.class.getSimpleName();

    @Override
    public List<NeighborhoodDTO> getNeighborhoods() throws AppException {
        Log.i(TAG, "getNeighborhoods executed");

        String sbRequest = "";
        SocketServer objSocketSever = new SocketServer();

        sbRequest += Service.OBTENER_BARRIO;

        Log.i(TAG, "getNeighborhoods :: Ejecución");
        String sbResponse = objSocketSever.send(sbRequest);
        Log.i(TAG, "getNeighborhoods.sbResponse: " + sbResponse);

        Parser objParserSepaRegistro = new Parser(sbResponse, Constants.SEPARADOR_REGISTRO);
        Integer nuStatus = objParserSepaRegistro.nextInt();

        if (nuStatus == 0) {
            List<NeighborhoodDTO> lstNeighborhoodsDTO = new ArrayList<>();

            while (objParserSepaRegistro.hasMoreTokens()) {
                Parser objParserSepaCampo = new Parser(objParserSepaRegistro.nextString(),
                        Constants.SEPARADOR_CAMPO);

                NeighborhoodDTO objNeighborhoodDTO = new NeighborhoodDTO(
                        objParserSepaCampo.nextInt(),
                        objParserSepaCampo.nextString());

                lstNeighborhoodsDTO.add(objNeighborhoodDTO);
            }

            return lstNeighborhoodsDTO;
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
