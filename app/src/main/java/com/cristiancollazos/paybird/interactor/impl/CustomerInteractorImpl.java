package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.cristiancollazos.paybird.interactor.CustomerInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.CustomerRepository;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.impl.CustomerRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class CustomerInteractorImpl implements CustomerInteractor {

    private CustomerRepository objCustomerRepository;

    public CustomerInteractorImpl() {
        objCustomerRepository = new CustomerRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getCustomerByDocument(final ResponseCallback<CustomerDTO> objResponseCallback,
                                      final String sbDocument) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objCustomerRepository.getCustomerByDocument(sbDocument);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((CustomerDTO) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getCustomersByRoute(final ResponseCallback<List<CustomerDTO>> objResponseCallback,
                                    final Integer nuRoute) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objCustomerRepository.getCustomersByRoute(nuRoute);
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
                        objResponseCallback.success(new ArrayList<CustomerDTO>());
                    } else {
                        objResponseCallback.success((List<CustomerDTO>) objResponse);
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void setNewCustomer(final ResponseCallback<Boolean> objResponseCallback,
                               final CustomerDTO objCustomerDTO) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objCustomerRepository.setNewCustomer(objCustomerDTO);
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
    public void setEditCustomer(final ResponseCallback<Boolean> objResponseCallback,
                                final CustomerDTO objCustomerDTO) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objCustomerRepository.setEditCustomer(objCustomerDTO);
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
    public void getSimpleCustomersByRouteFilter(final ResponseCallback<List<CustomerDTO>> objResponseCallback,
                                                final Integer nuRoute,
                                                final String sbCustomerName) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objCustomerRepository
                            .getSimpleCustomersByRouteFilter(nuRoute, sbCustomerName);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<CustomerDTO>) objResponse);
                }
            }
        }.execute();
    }

}
