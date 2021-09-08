package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;

import java.util.List;

public interface AuthenticationRepository {

    interface Services {
        String VALIDAR_USUARIO = "11";
        String OBTENER_RUTA = "4080";
    }

    UserDTO doLogin(String sbUser, String sbPassword) throws AppException;

    List<RouteDTO> getRoute(Integer nuUserCode) throws AppException;

}
