package com.cristiancollazos.paybird.interactor.callback;

import com.cristiancollazos.paybird.misc.exceptions.AppException;

public interface ResponseCallback<T> {

    void success(T objResponse);

    void failure(AppException objException);

}
