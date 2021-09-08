package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;

import java.util.List;

public interface CustomerRepository {

    interface Service {
        String OBTENER_USUARIO = "5005";
        String OBTENER_USUARIOS_RUTA = "1402";
        String REGISTRAR_USUARIO = "3330";
        String ACTUALIZAR_USUARIO = "4130";
        String OBTENER_USUARIOS_SENCILLO = "5001";
    }

    CustomerDTO getCustomerByDocument(String sbDocument) throws AppException;

    List<CustomerDTO> getCustomersByRoute(Integer nuRoute) throws AppException;

    Boolean setNewCustomer(CustomerDTO objCustomerDTO) throws AppException;

    Boolean setEditCustomer(CustomerDTO objCustomerDTO) throws AppException;

    List<CustomerDTO> getSimpleCustomersByRouteFilter(Integer nuRoute,
                                                      String sbCustomerName) throws AppException;

}
