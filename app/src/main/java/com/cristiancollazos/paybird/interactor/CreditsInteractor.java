package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PeriodDTO;

import java.util.List;

public interface CreditsInteractor {

    void getCreditsByCustomer(ResponseCallback<List<CreditDTO>> objResponseCallback,
                              Integer nuCustomerCode);

    void getMovementsByCredit(ResponseCallback<List<MovementDTO>> objResponseCallback,
                              Integer nuCreditCode);

    void getPeriods(ResponseCallback<List<PeriodDTO>> objResponseCallback);

    void setNewCredit(ResponseCallback<Boolean> objResponseCallback,
                      CreditDTO objCreditDTO,
                      Integer nuRouteCode,
                      Integer nuRouteOrderPosition,
                      Integer nuUserCode);

    void getRecentlyCreatedCredit(ResponseCallback<CreditDTO> objResponseCallback,
                                  Integer nuCustomerCode);

}
