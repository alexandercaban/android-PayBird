package com.cristiancollazos.paybird.view.adapter.interfaces;

public interface OnChangeOrderListener<T> {

    void OnChangeOrder(int nuPositionSubject, T objSubject, int nuPositionTarget, T objTarget);

}
