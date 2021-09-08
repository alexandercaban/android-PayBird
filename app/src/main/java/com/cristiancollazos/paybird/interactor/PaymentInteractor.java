package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;

import java.util.Date;
import java.util.List;

public interface PaymentInteractor {

    void getPendingPaymentsByDate(ResponseCallback<List<PendingPaymentDTO>> objResponseCallback,
                                  Integer nuRouteCode,
                                  Date dtFilterDate,
                                  String sbCustomerName,
                                  Integer nuCreditCode);

    void doPayPendingPayment(ResponseCallback<Boolean> obResponseCallback,
                             Integer nuRouteCode,
                             Integer nuOrder,
                             Integer nuValueToPay,
                             Date dtNextPaymentDate);

    void getMovementsByRoute(ResponseCallback<List<MovementDTO>> obResponseCallback,
                             Integer nuRouteCode);

    void getValidatePaymentPerRefund(ResponseCallback<Boolean> objResponseCallback,
                                     Integer nuMovementCode);

    void doNullMovement(ResponseCallback<Boolean> objResponseCallback,
                        Integer nuMovementCode);

    void getPendingCreditsByFilters(ResponseCallback<List<PendingPaymentDTO>> objResponseCallback,
                                    Integer nuRouteCode,
                                    String sbCustomerName,
                                    Integer nuCreditCode);

}
