package com.cristiancollazos.paybird.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class PendingPaymentAdapter extends RecyclerView.Adapter<PendingPaymentAdapter.TodayPaymentViewHolder> {

    private List<PendingPaymentDTO> lstPendingPaymentDTO;
    private OnRecyclerItemActionListener<PendingPaymentDTO> objLongClickListener;

    public PendingPaymentAdapter(List<PendingPaymentDTO> lstPendingPaymentDTO,
                                 OnRecyclerItemActionListener<PendingPaymentDTO> objLongClickListener) {
        this.lstPendingPaymentDTO = lstPendingPaymentDTO;
        this.objLongClickListener = objLongClickListener;
    }

    class TodayPaymentViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTodayPayment_Customer, tvTodayPayment_CreditCode, tvTodayPayment_Address,
                tvTodayPayment_Remainder, tvTodayPayment_LastPayment, tvTodayPayment_CreditValue,
                tvTodayPayment_PaymentValue, tvTodayPayment_InstallmentTracker;
        public CardView cvTodayPaymentItem;

        public TodayPaymentViewHolder(View objItemView) {
            super(objItemView);

            cvTodayPaymentItem = objItemView.findViewById(R.id.cvTodayPaymentItem);
            tvTodayPayment_Address = objItemView.findViewById(R.id.tvTodayPayment_Address);
            tvTodayPayment_CreditCode = objItemView.findViewById(R.id.tvTodayPayment_CreditCode);
            tvTodayPayment_Customer = objItemView.findViewById(R.id.tvTodayPayment_Customer);
            tvTodayPayment_LastPayment = objItemView.findViewById(R.id.tvTodayPayment_LastPayment);
            tvTodayPayment_CreditValue = objItemView.findViewById(R.id.tvTodayPayment_CreditValue);
            tvTodayPayment_Remainder = objItemView.findViewById(R.id.tvTodayPayment_Remainder);
            tvTodayPayment_PaymentValue = objItemView.findViewById(R.id.tvTodayPayment_PaymentValue);
            tvTodayPayment_InstallmentTracker = objItemView.findViewById(R.id.tvTodayPayment_InstallmentTracker);
        }
    }

    @Override
    public TodayPaymentViewHolder onCreateViewHolder(ViewGroup objParent, int nuViewType) {
        LayoutInflater objLayoutInflater = LayoutInflater.from(objParent.getContext());
        View objView = objLayoutInflater.inflate(R.layout.adapteritem_pendingpayments, objParent, false);
        return new TodayPaymentViewHolder(objView);
    }

    @Override
    public void onBindViewHolder(TodayPaymentViewHolder objHolder, final int nuPosition) {
        final PendingPaymentDTO objPendiPaymentDTO = lstPendingPaymentDTO.get(nuPosition);

        objHolder.tvTodayPayment_Address.setText(objPendiPaymentDTO.getSbHomeAddress());
        objHolder.tvTodayPayment_Customer.setText(objPendiPaymentDTO.getSbCustomerDocument()
                + " - " + objPendiPaymentDTO.getSbCustomerName());
        objHolder.tvTodayPayment_CreditCode.setText("# " + objPendiPaymentDTO.getNuCode().toString());
        objHolder.tvTodayPayment_LastPayment.setText("Último pago realizado: "
                + new SimpleDateFormat("dd/MM/yyyy").format(objPendiPaymentDTO.getDtLastPayment()));
        objHolder.tvTodayPayment_Remainder.setText(objPendiPaymentDTO.getDisplayRemainderValue());
        objHolder.tvTodayPayment_CreditValue.setText(objPendiPaymentDTO.getDisplayCreditValue());
        objHolder.tvTodayPayment_PaymentValue.setText(objPendiPaymentDTO.getDisplayInstallmentValue());

        Float flTotalCreditValue = objPendiPaymentDTO.getFlCreditValue()
                + ((objPendiPaymentDTO.getFlCreditValue() / 100) * objPendiPaymentDTO.getFlInterest());
        Float flInstallmentCounter = (flTotalCreditValue - objPendiPaymentDTO.getFlRemainder()) /
                (flTotalCreditValue / objPendiPaymentDTO.getNuInstallments());
        Integer nuInstallmentCounter = flInstallmentCounter.intValue() + 1;
        objHolder.tvTodayPayment_InstallmentTracker.setText(String.valueOf(nuInstallmentCounter));

        objHolder.cvTodayPaymentItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                objLongClickListener.OnAction(objPendiPaymentDTO, nuPosition);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstPendingPaymentDTO.size();
    }

}