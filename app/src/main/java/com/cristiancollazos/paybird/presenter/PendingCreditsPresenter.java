package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.PaymentInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.interactor.impl.PaymentInteractorImpl;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;

import java.util.Date;
import java.util.List;

public class PendingCreditsPresenter {

    public interface View {

        void showProgress();

        void hideProgress();

        void showErrorMessage(Integer nuCode, String sbError, String sbRecommend);

        void showSuccessMessage(String sbMessage);

        void renderPendingCredits(List<PendingPaymentDTO> lstPendingPaymentDTO);

        void notifyPendingNotFound();

        void printPayment(Integer nuPosition, Integer nuValueToPay, Date dtNextPayment);

        void notifyFiltersUsed(int nuFilterUsed);
    }

    private View objView;
    private PaymentInteractor objPaymentInteractor;

    public static final int NO_FILTER = 0;
    public static final int CREDITCODE_FILTER = 1;
    public static final int CUSTOMER_FILTER = 2;

    public PendingCreditsPresenter(View objView) {
        this.objView = objView;
        objPaymentInteractor = new PaymentInteractorImpl();
    }

    public void getPendingCreditsByFilters(Integer nuRouteCode,
                                           final String sbCustomerName,
                                           final Integer nuCreditCode) {
        objView.showProgress();
        objPaymentInteractor.getPendingCreditsByFilters(new ResponseCallback<List<PendingPaymentDTO>>() {
            @Override
            public void success(List<PendingPaymentDTO> objResponse) {
                objView.renderPendingCredits(objResponse);

                if (sbCustomerName != null) {
                    objView.notifyFiltersUsed(CUSTOMER_FILTER);
                } else if (nuCreditCode != null) {
                    objView.notifyFiltersUsed(CREDITCODE_FILTER);
                } else {
                    objView.notifyFiltersUsed(NO_FILTER);
                }
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                    objView.notifyPendingNotFound();
                } else {
                    objView.showErrorMessage(objException.getErrorCode(),
                            objException.getMessage(), objException.getRecommend());
                }

                if (sbCustomerName != null) {
                    objView.notifyFiltersUsed(CUSTOMER_FILTER);
                } else if (nuCreditCode != null) {
                    objView.notifyFiltersUsed(CREDITCODE_FILTER);
                } else {
                    objView.notifyFiltersUsed(NO_FILTER);
                }
            }
        }, nuRouteCode, sbCustomerName, nuCreditCode);
    }

    public void doPayPendingPayment(final Integer nuRouteCode,
                                    Integer nuOrder,
                                    final Integer nuValueToPay,
                                    final Date dtNextPayment,
                                    final Integer nuPosition) {
        objView.showProgress();
        objPaymentInteractor.doPayPendingPayment(new ResponseCallback<Boolean>() {
            @Override
            public void success(Boolean objResponse) {
                objView.printPayment(nuPosition, nuValueToPay, dtNextPayment);
                objView.showSuccessMessage("Abono registrado");
                objView.hideProgress();
                getPendingCreditsByFilters(nuRouteCode, null, null);
            }

            @Override
            public void failure(AppException objException) {
                objView.showErrorMessage(objException.getErrorCode(), objException.getMessage(),
                        objException.getRecommend());
                objView.hideProgress();
            }
        }, nuRouteCode, nuOrder, nuValueToPay, dtNextPayment);
    }


}
