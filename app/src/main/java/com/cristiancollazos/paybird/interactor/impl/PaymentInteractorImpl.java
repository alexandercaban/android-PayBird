package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cristiancollazos.paybird.interactor.PaymentInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.PaymentRepository;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;
import com.cristiancollazos.paybird.repository.impl.PaymentRepositoryImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentInteractorImpl implements PaymentInteractor {

    private final String TAG = PaymentInteractorImpl.class.getSimpleName();
    private PaymentRepository objPaymentRepository;

    public PaymentInteractorImpl() {
        objPaymentRepository = new PaymentRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getPendingPaymentsByDate(
            final ResponseCallback<List<PendingPaymentDTO>> objResponseCallback,
            final Integer nuRouteCode,
            final Date dtFilterDate,
            final String sbCustomerName,
            final Integer nuCreditCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... rcVoids) {
                try {
                    String sbDate = new SimpleDateFormat("yyyy-MM-dd").format(dtFilterDate);
                    return objPaymentRepository.getPendingPaymentsByDateAndFilters(sbDate,
                            nuRouteCode, sbCustomerName, nuCreditCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    if (objResponse instanceof NullPointerException) {
                        objResponseCallback.success(new ArrayList<PendingPaymentDTO>());
                    } else {
                        objResponseCallback.success((List<PendingPaymentDTO>) objResponse);
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void doPayPendingPayment(final ResponseCallback<Boolean> obResponseCallback,
                                    final Integer nuRouteCode,
                                    final Integer nuOrder,
                                    final Integer nuValueToPay,
                                    final Date dtNextPaymentDate) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    String sbPaymentDate = new SimpleDateFormat("yyyy-MM-dd")
                            .format(dtNextPaymentDate);
                    return objPaymentRepository.doPayPendingPayment(nuRouteCode, nuOrder,
                            nuValueToPay, sbPaymentDate);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    obResponseCallback.failure((AppException) objResponse);
                } else {
                    obResponseCallback.success((Boolean) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getMovementsByRoute(final ResponseCallback<List<MovementDTO>> objResponseCallback,
                                    final Integer nuRouteCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... rcVoids) {
                try {
                    return objPaymentRepository.getMovementsByRoute(nuRouteCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    if (objResponse != null) {
                        objResponseCallback.success((List<MovementDTO>) objResponse);
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getValidatePaymentPerRefund(final ResponseCallback<Boolean> objResponseCallback,
                                            final Integer nuMovementCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objPaymentRepository.getValidatePaymentPerRefund(nuMovementCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((Boolean) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void doNullMovement(final ResponseCallback<Boolean> objResponseCallback,
                               final Integer nuMovementCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objPaymentRepository.doNullMovement(nuMovementCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((Boolean) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getPendingCreditsByFilters(final ResponseCallback<List<PendingPaymentDTO>> objResponseCallback,
                                           final Integer nuRouteCode,
                                           final String sbCustomerName,
                                           final Integer nuCreditCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objPaymentRepository.getPendingCreditsByFilters(nuRouteCode,
                            sbCustomerName, nuCreditCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    if (objResponse instanceof NullPointerException) {
                        objResponseCallback.success(new ArrayList<PendingPaymentDTO>());
                    } else {
                        objResponseCallback.success((List<PendingPaymentDTO>) objResponse);
                    }
                }
            }
        }.execute();
    }

}
