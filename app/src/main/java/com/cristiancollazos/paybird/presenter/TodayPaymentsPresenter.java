package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.CustomerInteractor;
import com.cristiancollazos.paybird.interactor.PaymentInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.interactor.impl.CustomerInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.PaymentInteractorImpl;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;

import java.util.Date;
import java.util.List;

public class TodayPaymentsPresenter {

    public interface View {
        void showProgress();

        void hideProgress();

        void showErrorMessage(Integer nuCode, String sbError, String sbRecommend);

        void showSuccessMessage(String sbMessage);

        void renderPendingPayments(List<PendingPaymentDTO> lstPendingPaymentDTO);

        void renderMovements(List<MovementDTO> lstMovementDTO);

        void notifyPendingNotFound();

        void notifyDoneNotFound();

        void removePendingItemAndNotify(Integer nuPosition);

        void displayUiAsPending();

        void displayUiAsDone();

        void revertMovementItem(Integer nuPosition);

        void removeMovementItem(Integer nuPosition);

        void printPayment(Integer nuPosition, Integer nuValueToPay, Date dtNextPayment);

        void notifyFiltersUsed(int nuFilterUsed);
    }

    private final String TAG = TodayPaymentsPresenter.class.getSimpleName();
    private View objView;
    private PaymentInteractor objPaymentInteractor;
    private CustomerInteractor objCustomerInteractor;

    public static final int NO_FILTER = 0;
    public static final int CREDITCODE_FILTER = 1;
    public static final int CUSTOMER_FILTER = 2;

    public TodayPaymentsPresenter(View objView) {
        this.objView = objView;
        this.objPaymentInteractor = new PaymentInteractorImpl();
        this.objCustomerInteractor = new CustomerInteractorImpl();
    }

    public void getPendingPaymentsByDate(Integer nuRouteCode,
                                         Date dtFilterDate,
                                         final String sbCustomerName,
                                         final Integer nuCreditCode) {
        objView.showProgress();
        objPaymentInteractor.getPendingPaymentsByDate(
                new ResponseCallback<List<PendingPaymentDTO>>() {
                    @Override
                    public void success(List<PendingPaymentDTO> objResponse) {
                        objView.displayUiAsPending();
                        objView.renderPendingPayments(objResponse);
                        objView.hideProgress();

                        if (sbCustomerName != null) {
                            objView.notifyFiltersUsed(CUSTOMER_FILTER);
                        } else if (nuCreditCode != null) {
                            objView.notifyFiltersUsed(CREDITCODE_FILTER);
                        } else {
                            objView.notifyFiltersUsed(NO_FILTER);
                        }
                    }

                    @Override
                    public void failure(AppException objException) {
                        objView.hideProgress();
                        if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                            objView.notifyPendingNotFound();
                            objView.displayUiAsPending();
                        } else {
                            objView.displayUiAsPending();
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
                }, nuRouteCode, dtFilterDate, sbCustomerName, nuCreditCode);
    }

    public void doPayPendingPayment(Integer nuRouteCode,
                                    Integer nuOrder,
                                    final Integer nuValueToPay,
                                    final Date dtNextPayment,
                                    final Integer nuPosition) {
        objView.showProgress();
        objPaymentInteractor.doPayPendingPayment(new ResponseCallback<Boolean>() {
            @Override
            public void success(Boolean objResponse) {
                objView.printPayment(nuPosition, nuValueToPay, dtNextPayment);
                objView.removePendingItemAndNotify(nuPosition);
                objView.showSuccessMessage("Abono registrado");
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.showErrorMessage(objException.getErrorCode(), objException.getMessage(),
                        objException.getRecommend());
                objView.hideProgress();
            }
        }, nuRouteCode, nuOrder, nuValueToPay, dtNextPayment);
    }

    public void getMovementsByRoute(Integer nuRouteCode) {
        objView.showProgress();
        objPaymentInteractor.getMovementsByRoute(new ResponseCallback<List<MovementDTO>>() {
            @Override
            public void success(List<MovementDTO> objResponse) {
                objView.displayUiAsDone();
                objView.renderMovements(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                    objView.notifyDoneNotFound();
                    objView.displayUiAsDone();
                } else {
                    objView.displayUiAsDone();
                    objView.showErrorMessage(objException.getErrorCode(),
                            objException.getMessage(), objException.getRecommend());
                }
            }
        }, nuRouteCode);
    }

    public void doNullMovement(final Integer nuMovementCode, final Integer nuPosition) {
        objView.showProgress();
        objPaymentInteractor.getValidatePaymentPerRefund(new ResponseCallback<Boolean>() {
            @Override
            public void success(Boolean objResponse) {
                if (objResponse) {
                    objPaymentInteractor.doNullMovement(new ResponseCallback<Boolean>() {
                        @Override
                        public void success(Boolean objResponse) {
                            objView.showSuccessMessage("Movimiento anulado");
                            objView.removeMovementItem(nuPosition);
                            objView.hideProgress();
                        }

                        @Override
                        public void failure(AppException objException) {
                            objView.hideProgress();
                            objView.revertMovementItem(nuPosition);
                            objView.showErrorMessage(objException.getErrorCode(),
                                    objException.getMessage(), objException.getRecommend());
                        }
                    }, nuMovementCode);
                } else {
                    objView.hideProgress();
                    objView.revertMovementItem(nuPosition);
                    objView.showErrorMessage(ErrorConstants.E0009.code(), ErrorConstants.E0009.def(),
                            ErrorConstants.E0009.recommend());
                }
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                objView.revertMovementItem(nuPosition);
                objView.showErrorMessage(objException.getErrorCode(), objException.getMessage(),
                        objException.getRecommend());
            }
        }, nuMovementCode);
    }

}
