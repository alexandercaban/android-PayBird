package com.cristiancollazos.paybird.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.CreditViewHolder> {

    private List<CreditDTO> lstCreditDTO;
    private Context objContext;
    private OnRecyclerItemActionListener<CreditDTO> objListener;

    public CreditAdapter(Context objContext, List<CreditDTO> lstCreditDTO,
                         OnRecyclerItemActionListener<CreditDTO> objListener) {
        this.objContext = objContext;
        this.lstCreditDTO = lstCreditDTO;
        this.objListener = objListener;
    }

    public class CreditViewHolder extends RecyclerView.ViewHolder {

        TextView tvCreditInfo_CreatedDate, tvCreditInfo_Code, tvCreditInfo_RemainderLabel,
                tvCreditInfo_Remainder, tvCreditInfo_CreditValue, tvCreditInfo_Interest,
                tvCreditInfo_Period, tvCreditInfo_LastPayment, tvCreditInfo_InstallmentTracker;

        RelativeLayout rlCreditItem;

        public CreditViewHolder(View objItemView) {
            super(objItemView);

            rlCreditItem = objItemView.findViewById(R.id.rlCreditItem);
            tvCreditInfo_CreatedDate = objItemView.findViewById(R.id.tvCreditInfo_CreatedDate);
            tvCreditInfo_Code = objItemView.findViewById(R.id.tvCreditInfo_Code);
            tvCreditInfo_RemainderLabel = objItemView.findViewById(R.id.tvCreditInfo_RemainderLabel);
            tvCreditInfo_Remainder = objItemView.findViewById(R.id.tvCreditInfo_Remainder);
            tvCreditInfo_CreditValue = objItemView.findViewById(R.id.tvCreditInfo_CreditValue);
            tvCreditInfo_Interest = objItemView.findViewById(R.id.tvCreditInfo_Interest);
            tvCreditInfo_Period = objItemView.findViewById(R.id.tvCreditInfo_Period);
            tvCreditInfo_LastPayment = objItemView.findViewById(R.id.tvCreditInfo_LastPayment);
            tvCreditInfo_InstallmentTracker = objItemView.findViewById(R.id.tvCreditInfo_InstallmentTracker);
        }

    }

    @Override
    public CreditViewHolder onCreateViewHolder(ViewGroup objParent, int nuViewType) {
        LayoutInflater objLayoutInflater = LayoutInflater.from(objParent.getContext());
        View objView = objLayoutInflater.inflate(R.layout.adapteritem_credit, objParent, false);
        return new CreditViewHolder(objView);
    }

    @Override
    public void onBindViewHolder(CreditViewHolder objHolder, final int nuPosition) {
        SimpleDateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");
        final CreditDTO objCreditDTO = lstCreditDTO.get(nuPosition);

        objHolder.tvCreditInfo_CreatedDate.setText(objFormatter.format(objCreditDTO.getDtCreatedDate()));
        objHolder.tvCreditInfo_Code.setText(String.valueOf(objCreditDTO.getNuCode()));

        if (objCreditDTO.getSbStatus().equals("A")) {
            objHolder.tvCreditInfo_CreatedDate.setTextColor(objContext.getResources().getColor(R.color.indigoblue_accent));
            objHolder.tvCreditInfo_Code.setTextColor(objContext.getResources().getColor(R.color.indigoblue_accent));
            objHolder.tvCreditInfo_Remainder.setTextColor(objContext.getResources().getColor(R.color.indigoblue_primary_text));
            objHolder.tvCreditInfo_CreditValue.setTextColor(objContext.getResources().getColor(R.color.indigoblue_primary_text));
            objHolder.tvCreditInfo_Interest.setTextColor(objContext.getResources().getColor(R.color.indigoblue_primary_text));
            objHolder.tvCreditInfo_Period.setTextColor(objContext.getResources().getColor(R.color.indigoblue_primary_text));
            objHolder.tvCreditInfo_LastPayment.setTextColor(objContext.getResources().getColor(R.color.indigoblue_primary_text));
            objHolder.tvCreditInfo_InstallmentTracker.setTextColor(objContext.getResources().getColor(R.color.indigoblue_primary_text));

            objHolder.tvCreditInfo_RemainderLabel.setVisibility(View.VISIBLE);
            objHolder.tvCreditInfo_Remainder.setText(Utilities.getFormattedValue(objCreditDTO.getFlRemainder()));
        } else {
            objHolder.tvCreditInfo_CreatedDate.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));
            objHolder.tvCreditInfo_Code.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));
            objHolder.tvCreditInfo_Remainder.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));
            objHolder.tvCreditInfo_CreditValue.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));
            objHolder.tvCreditInfo_Interest.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));
            objHolder.tvCreditInfo_Period.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));
            objHolder.tvCreditInfo_LastPayment.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));
            objHolder.tvCreditInfo_InstallmentTracker.setTextColor(objContext.getResources().getColor(R.color.indigoblue_secondary_text));

            objHolder.tvCreditInfo_RemainderLabel.setVisibility(View.GONE);
            objHolder.tvCreditInfo_Remainder.setText(objContext.getResources().getString(R.string.creditinfo_finished));
        }

        objHolder.tvCreditInfo_CreditValue.setText(Utilities.getFormattedValue(objCreditDTO.getFlValue()));
        objHolder.tvCreditInfo_Interest.setText(String.valueOf(objCreditDTO.getFlInterest()));
        objHolder.tvCreditInfo_LastPayment.setText(objFormatter.format(objCreditDTO.getDtLastPayment()));

        String sbPeriod;
        if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_DIARIO) {
            sbPeriod = "Diario";
        } else if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_SEMANAL) {
            sbPeriod = "Semanal";
        } else if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_QUINCENAL) {
            sbPeriod = "Quincenal";
        } else {
            sbPeriod = "Mensual";
        }

        objHolder.tvCreditInfo_Period.setText(sbPeriod);

        Float flTotalCreditValue = objCreditDTO.getFlValue()
                + ((objCreditDTO.getFlValue() / 100) * objCreditDTO.getFlInterest());
        Float flInstallmentCounter = (flTotalCreditValue - objCreditDTO.getFlRemainder()) /
                (flTotalCreditValue / objCreditDTO.getNuInstallmentQuantity());
        Integer nuInstallmentCounter = flInstallmentCounter.intValue() + 1;
        objHolder.tvCreditInfo_InstallmentTracker.setText(String.valueOf(nuInstallmentCounter));

        objHolder.rlCreditItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View objView) {
                objListener.OnAction(objCreditDTO, nuPosition);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCreditDTO.size();
    }

}
