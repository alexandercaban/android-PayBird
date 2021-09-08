package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cristiancollazos.paybird.interactor.LocalInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.DocumentTypeDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

import java.util.List;

public class LocalInteractorImpl implements LocalInteractor {

    private LocalRepository objLocalRepository;

    public LocalInteractorImpl() {
        objLocalRepository = new LocalRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getDocumentTypes(final ResponseCallback<List<DocumentTypeDTO>> objResponseCallback) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objLocalRepository.getDocumentTypes();
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<DocumentTypeDTO>) objResponse);
                }
            }
        }.execute();
    }

}
