package com.cristiancollazos.paybird.misc.exceptions;

import com.cristiancollazos.paybird.misc.enums.ErrorConstants;

public class AppException extends Exception {

    private static final long serialVersionUID = -5213793666303258030L;

    private int nuCodeError;
    private String sbErrorMessage;
    private String sbErrorRecommend;

    public AppException(int nuNewCodeError, String sbNewErrorMessage) {
        nuCodeError = nuNewCodeError;
        sbErrorMessage = sbNewErrorMessage;
    }

    public AppException(ErrorConstants sbNewCodeError) {
        nuCodeError = sbNewCodeError.code();
        sbErrorMessage = sbNewCodeError.def();
        sbErrorRecommend = sbNewCodeError.recommend();
    }

    public AppException(int nuCodeError, String sbErrorMessage, String sbErrorRecommend) {
        this.nuCodeError = nuCodeError;
        this.sbErrorMessage = sbErrorMessage;
        this.sbErrorRecommend = sbErrorRecommend;
    }

    @Override
    public String getMessage() {
        return sbErrorMessage;
    }

    public int getErrorCode() {
        return nuCodeError;
    }

    public String getRecommend() {
        return sbErrorRecommend;
    }

}

