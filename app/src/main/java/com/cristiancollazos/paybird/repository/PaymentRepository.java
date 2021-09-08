package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;

import java.util.List;

public interface PaymentRepository {

    interface Service {

        String OBTENER_ABONOS_FECHA_FILTROS = "5004";

        String ABONAR = "3410";

        String OBTENER_MOVIMIENTOS = "3420";

        String CONSULTAR_ABONOS_DESEMBOLSO = "4250";

        String ANULAR_MOVIMIENTO = "4050";

        String OBTENER_CREDITOS_PENDIENTES_FILTROS = "5002";
    }

    List<PendingPaymentDTO> getPendingPaymentsByDateAndFilters(String sbFormattedDate,
                                                               Integer nuRouteCode,
                                                               String sbCustomerName,
                                                               Integer nuCreditCode) throws AppException;

    Boolean doPayPendingPayment(Integer nuRouteCode,
                                Integer nuOrder,
                                Integer nuValueToPay,
                                String sbNextPaymentDay) throws AppException;

    List<MovementDTO> getMovementsByRoute(Integer nuRouteCode) throws AppException;

    Boolean getValidatePaymentPerRefund(Integer nuMovementCode) throws AppException;

    Boolean doNullMovement(Integer nuMovementCode) throws AppException;

    List<PendingPaymentDTO> getPendingCreditsByFilters(Integer nuRouteCode,
                                                       String sbCustomerName,
                                                       Integer nuCreditCode) throws AppException;

}
