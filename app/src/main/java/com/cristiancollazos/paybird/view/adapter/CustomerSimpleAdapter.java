package com.cristiancollazos.paybird.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;

import java.util.List;

public class CustomerSimpleAdapter
        extends RecyclerView.Adapter<CustomerSimpleAdapter.SimpleRecyclerViewHolder> {

    private List<CustomerDTO> lstCustomersDTO;
    private OnRecyclerItemActionListener<CustomerDTO> objListener;

    public CustomerSimpleAdapter(List<CustomerDTO> lstCustomersDTO,
                                 OnRecyclerItemActionListener objListener) {
        this.lstCustomersDTO = lstCustomersDTO;
        this.objListener = objListener;
    }

    class SimpleRecyclerViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llItemCustomer;
        private TextView tvItemSimpleDisplayValue;

        public SimpleRecyclerViewHolder(View objItemView) {
            super(objItemView);
            llItemCustomer = objItemView.findViewById(R.id.llItemCustomer);
            tvItemSimpleDisplayValue = objItemView.findViewById(R.id.tvItemSimpleDisplayValue);
        }
    }

    @Override
    public SimpleRecyclerViewHolder onCreateViewHolder(ViewGroup objParent, int nuViewType) {
        View objView = LayoutInflater.from(objParent.getContext())
                .inflate(R.layout.adapteritem_customer_simple, objParent, false);
        return new SimpleRecyclerViewHolder(objView);
    }

    @Override
    public void onBindViewHolder(SimpleRecyclerViewHolder objHolder, final int nuPosition) {
        final CustomerDTO objData = lstCustomersDTO.get(nuPosition);
        objHolder.tvItemSimpleDisplayValue.setText(objData.getRecyclerItemDisplayValue());
        objHolder.llItemCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View objView) {
                objListener.OnAction(objData, nuPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCustomersDTO.size();
    }

}
