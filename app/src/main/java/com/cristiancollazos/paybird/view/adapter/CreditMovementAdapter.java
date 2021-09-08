package com.cristiancollazos.paybird.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;

import java.text.SimpleDateFormat;
import java.util.List;

public class CreditMovementAdapter
        extends RecyclerView.Adapter<CreditMovementAdapter.CreditMovementViewHolder> {

    private List<MovementDTO> lstMovementDTO;

    public CreditMovementAdapter(List<MovementDTO> lstMovementDTO) {
        this.lstMovementDTO = lstMovementDTO;
    }

    public class CreditMovementViewHolder extends RecyclerView.ViewHolder {

        TextView tvCreditMovement_DateHour, tvCreditMovement_Status, tvCreditMovement_Value;

        public CreditMovementViewHolder(View objItemView) {
            super(objItemView);
            tvCreditMovement_DateHour = objItemView.findViewById(R.id.tvCreditMovement_DateHour);
            tvCreditMovement_Status = objItemView.findViewById(R.id.tvCreditMovement_Status);
            tvCreditMovement_Value = objItemView.findViewById(R.id.tvCreditMovement_Value);
        }

    }

    @Override
    public CreditMovementViewHolder onCreateViewHolder(ViewGroup objParent,
                                                       int nuViewType) {
        LayoutInflater objLayoutInflater = LayoutInflater.from(objParent.getContext());
        View objView = objLayoutInflater.inflate(R.layout.adapteritem_creditmovement,
                objParent, false);
        return new CreditMovementViewHolder(objView);
    }

    @Override
    public void onBindViewHolder(CreditMovementViewHolder objHolder,
                                 int nuPosition) {
        SimpleDateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");

        MovementDTO objMovementDTO = lstMovementDTO.get(nuPosition);
        objHolder.tvCreditMovement_DateHour.setText(
                objFormatter.format(objMovementDTO.getDtMovementDate()) + " " + objMovementDTO.getSbMovementHour());
        objHolder.tvCreditMovement_Value.setText(Utilities.getFormattedValue(objMovementDTO.getFlValue()));
        objHolder.tvCreditMovement_Status.setText(objMovementDTO.getSbStatus());
    }

    @Override
    public int getItemCount() {
        return lstMovementDTO.size();
    }

}
