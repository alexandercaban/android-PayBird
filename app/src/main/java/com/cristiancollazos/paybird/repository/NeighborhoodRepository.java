package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.NeighborhoodDTO;

import java.util.List;

public interface NeighborhoodRepository {

    interface Service {
        String OBTENER_BARRIO = "430";
    }

    List<NeighborhoodDTO> getNeighborhoods() throws AppException;

}
