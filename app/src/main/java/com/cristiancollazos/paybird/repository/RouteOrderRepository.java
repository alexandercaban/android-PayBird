package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;

import java.util.List;

public interface RouteOrderRepository {

    interface Service {

        String OBTENER_ORDEN_RUTA = "5003";

        String CAMBIAR_ORDEN_RUTA = "4060";

    }

    List<RouteItemDTO> getRouteOrderByRoute(Integer nuRouteCode) throws AppException;

    Boolean setReorderRoute(Integer nuRouteCode,
                            Integer nuRouteOrderFrom,
                            Integer nuRouteOrderTo) throws AppException;

    List<RouteItemDTO> getRouteOrderByRouteWithOffset(Integer nuRouteCode) throws AppException;

}
