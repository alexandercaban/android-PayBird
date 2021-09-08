package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PeriodDTO;

import java.util.List;

public interface CreditsRepository {

    interface Service {

        String OBTENER_CREDITOS_CLIENTE = "1432";

        String OBTENER_MOVIMIENTOS_CREDITOS = "2910";

        String OBTENER_PERIODOS = "4120";

        String REGISTRAR_CREDITO = "2920";

        String OBTENER_CREDITO_RECIEN_CREADO = "5006";

    }

    List<CreditDTO> getCreditsByCustomer(Integer nuCustomerCode) throws AppException;

    List<MovementDTO> getMovementsByCredit(Integer nuCreditCode) throws AppException;

    List<PeriodDTO> getPeriods() throws AppException;

    Boolean setNewCredit(CreditDTO objCreditDTO,
                         Integer nuRouteCode,
                         Integer nuRouteOrderPosition,
                         Integer nuUserCode) throws AppException;

    CreditDTO getRecentlyCreatedCredit(Integer nuCustomerCode) throws AppException;

}
