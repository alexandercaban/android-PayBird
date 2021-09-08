package com.cristiancollazos.paybird.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.view.adapter.interfaces.SpinnerAdaptable;

import java.util.List;

public class SimpleAdapter<T extends SpinnerAdaptable> extends ArrayAdapter<T> {

    private Context objContext;
    private List<T> lstData;
    private Boolean blLightMode;

    public SimpleAdapter(Context objContext, List<T> lstData) {
        super(objContext, R.layout.item_routes_spinner, lstData);
        this.objContext = objContext;
        this.lstData = lstData;
        this.blLightMode = false;
    }

    public SimpleAdapter(Context objContext, List<T> lstData, Boolean blLightMode) {
        super(objContext, R.layout.item_routes_spinner, lstData);
        this.objContext = objContext;
        this.lstData = lstData;
        this.blLightMode = blLightMode;
    }

    @NonNull
    @Override
    public View getView(int nuPosition, @Nullable View objConvertView,
                        @NonNull ViewGroup objParent) {
        LayoutInflater objInflater = LayoutInflater.from(objContext);

        @SuppressLint("ViewHolder")
        View objView = objInflater.inflate(R.layout.item_routes_spinner, objParent, false);
        TextView tvDisplayValue = objView.findViewById(android.R.id.text1);
        tvDisplayValue.setText(lstData.get(nuPosition).getDisplayValue());
        if (blLightMode) {
            tvDisplayValue.setTextColor(objContext.getResources().getColor(R.color.indigoblue_icons));
        }

        return objView;
    }

    @Override
    public View getDropDownView(int nuPosition, @Nullable View objConvertView,
                                @NonNull ViewGroup objParent) {
        LayoutInflater objInflater = LayoutInflater.from(objContext);

        @SuppressLint("ViewHolder")
        View objView = objInflater.inflate(R.layout.item_routes_spinner, objParent, false);
        TextView tvDisplayValue = objView.findViewById(android.R.id.text1);
        tvDisplayValue.setText(lstData.get(nuPosition).getDisplayValue());

        return objView;
    }

    public List<T> getLstData() {
        return lstData;
    }

    public int getPositionByKey(String objKey) {
        for (int i = 0; i < lstData.size(); i++) {
            if (lstData.get(i).getCode().equals(objKey)) {
                return i;
            }
        }
        return 0;
    }

}
