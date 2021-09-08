package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cristiancollazos.paybird.interactor.CreditsInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.CreditsRepository;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PeriodDTO;
import com.cristiancollazos.paybird.repository.impl.CreditsRepositoryImpl;

import java.util.List;

public class CreditsInteractorImpl implements CreditsInteractor {

    private CreditsRepository objCreditsRepository;

    public CreditsInteractorImpl() {
        objCreditsRepository = new CreditsRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getCreditsByCustomer(final ResponseCallback<List<CreditDTO>> objResponseCallback,
                                     final Integer nuCustomerCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... rcVoids) {
                try {
                    return objCreditsRepository.getCreditsByCustomer(nuCustomerCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<CreditDTO>) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getMovementsByCredit(final ResponseCallback<List<MovementDTO>> objResponseCallback,
                                     final Integer nuCreditCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... rcVoids) {
                try {
                    return objCreditsRepository.getMovementsByCredit(nuCreditCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<MovementDTO>) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getPeriods(final ResponseCallback<List<PeriodDTO>> objResponseCallback) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... rcVoids) {
                try {
                    return objCreditsRepository.getPeriods();
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<PeriodDTO>) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void setNewCredit(final ResponseCallback<Boolean> objResponseCallback,
                             final CreditDTO objCreditDTO,
                             final Integer nuRouteCode,
                             final Integer nuRouteOrderPosition,
                             final Integer nuUserCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... rcVoids) {
                try {
                    return objCreditsRepository.setNewCredit(objCreditDTO,
                            nuRouteCode, nuRouteOrderPosition, nuUserCode);
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
    public void getRecentlyCreatedCredit(final ResponseCallback<CreditDTO> objResponseCallback,
                                         final Integer nuCustomerCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... rcVoids) {
                try {
                    return objCreditsRepository.getRecentlyCreatedCredit(nuCustomerCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((CreditDTO) objResponse);
                }
            }
        }.execute();
    }

}
