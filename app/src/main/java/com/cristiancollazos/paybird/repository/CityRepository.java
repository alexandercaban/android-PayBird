package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.CityDTO;

import java.util.List;

public interface CityRepository {

    interface Service {
        String OBTENER_CIUDADES = "420";
    }

    List<CityDTO> getCities() throws AppException;

}
