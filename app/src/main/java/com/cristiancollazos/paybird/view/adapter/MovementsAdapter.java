package com.cristiancollazos.paybird.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;

import java.text.SimpleDateFormat;
import java.util.List;

public class MovementsAdapter extends RecyclerView.Adapter<MovementsAdapter.MovementViewHolder> {

    private List<MovementDTO> lstMovementDTO;

    public MovementsAdapter(List<MovementDTO> lstMovementDTO) {
        this.lstMovementDTO = lstMovementDTO;
    }

    class MovementViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovement_Type, tvMovement_DateHour, tvMovement_Customer, tvMovement_CreditCode,
                tvMovement_Value;

        MovementViewHolder(View objItemView) {
            super(objItemView);
            tvMovement_Type = objItemView.findViewById(R.id.tvMovement_Type);
            tvMovement_DateHour = objItemView.findViewById(R.id.tvMovement_DateHour);
            tvMovement_Customer = objItemView.findViewById(R.id.tvMovement_Customer);
            tvMovement_CreditCode = objItemView.findViewById(R.id.tvMovement_CreditCode);
            tvMovement_Value = objItemView.findViewById(R.id.tvMovement_Value);
        }

    }

    @Override
    public MovementViewHolder onCreateViewHolder(ViewGroup objParent, int nuViewType) {
        LayoutInflater objLayoutInflater = LayoutInflater.from(objParent.getContext());
        View objView = objLayoutInflater.inflate(R.layout.adapteritem_movements, objParent, false);
        return new MovementViewHolder(objView);
    }

    @Override
    public void onBindViewHolder(MovementViewHolder objHolder, int nuPosition) {
        MovementDTO obMovementDTO = lstMovementDTO.get(nuPosition);

        SimpleDateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String sbDateHour = objFormatter.format(obMovementDTO.getDtMovementDate()) + " "
                + obMovementDTO.getSbMovementHour();

        objHolder.tvMovement_DateHour.setText(sbDateHour);
        objHolder.tvMovement_CreditCode.setText("" + obMovementDTO.getNuCreditCode());
        objHolder.tvMovement_Customer.setText(obMovementDTO.getSbCustomerName());
        objHolder.tvMovement_Type.setText(obMovementDTO.getSbTypeDescription());
        objHolder.tvMovement_Value.setText(obMovementDTO.getDisplayValue());
    }

    @Override
    public int getItemCount() {
        return lstMovementDTO.size();
    }

}
