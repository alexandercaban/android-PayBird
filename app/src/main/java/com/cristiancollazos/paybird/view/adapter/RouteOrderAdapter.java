package com.cristiancollazos.paybird.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnChangeOrderListener;

import java.util.List;

public class RouteOrderAdapter extends RecyclerView.Adapter<RouteOrderAdapter.RouteOrderViewHolder> {

    private List<RouteItemDTO> lstRouteItemDTO;
    private OnChangeOrderListener<RouteItemDTO> objListener;

    public RouteOrderAdapter(List<RouteItemDTO> lstRouteItemDTO,
                             OnChangeOrderListener<RouteItemDTO> objListener) {
        this.lstRouteItemDTO = lstRouteItemDTO;
        this.objListener = objListener;
    }

    class RouteOrderViewHolder extends RecyclerView.ViewHolder {

        public TextView tvRouteOrderItem_Address, tvRouteOrderItem_CustomerName;
        public FrameLayout btnRouteOrder_Down, btnRouteOrder_Up;

        public RouteOrderViewHolder(View objItemView) {
            super(objItemView);
            tvRouteOrderItem_Address = objItemView.findViewById(R.id.tvRouteOrderItem_Address);
            tvRouteOrderItem_CustomerName = objItemView.findViewById(R.id.tvRouteOrderItem_CustomerName);
            btnRouteOrder_Up = objItemView.findViewById(R.id.btnRouteOrder_Up);
            btnRouteOrder_Down = objItemView.findViewById(R.id.btnRouteOrder_Down);
        }

    }

    @Override
    public RouteOrderViewHolder onCreateViewHolder(ViewGroup objParent, int nuViewType) {
        LayoutInflater objLayoutInflater = LayoutInflater.from(objParent.getContext());
        View objView = objLayoutInflater.inflate(R.layout.adapteritem_routeorderitem,
                objParent, false);
        return new RouteOrderViewHolder(objView);
    }

    @Override
    public void onBindViewHolder(RouteOrderViewHolder nuHolder, final int nuPosition) {
        final RouteItemDTO objRouteItemDTO = lstRouteItemDTO.get(nuPosition);
        nuHolder.tvRouteOrderItem_CustomerName.setText(objRouteItemDTO.getSbCustomerName());
        nuHolder.tvRouteOrderItem_Address.setText(objRouteItemDTO.getSbAddress());

        if (lstRouteItemDTO.size() > 1) {
            if (nuPosition == 0) {
                nuHolder.btnRouteOrder_Up.setVisibility(View.INVISIBLE);
                nuHolder.btnRouteOrder_Down.setVisibility(View.VISIBLE);

                nuHolder.btnRouteOrder_Down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View objView) {
                        RouteItemDTO objRouteItemDTO2 = lstRouteItemDTO.get(nuPosition + 1);
                        objListener.OnChangeOrder(nuPosition, objRouteItemDTO,
                                nuPosition + 1, objRouteItemDTO2);
                    }
                });
            } else if (nuPosition == (lstRouteItemDTO.size() - 1)) {
                nuHolder.btnRouteOrder_Up.setVisibility(View.VISIBLE);
                nuHolder.btnRouteOrder_Down.setVisibility(View.INVISIBLE);

                nuHolder.btnRouteOrder_Up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View objView) {
                        RouteItemDTO objRouteItemDTO2 = lstRouteItemDTO.get(nuPosition - 1);
                        objListener.OnChangeOrder(nuPosition, objRouteItemDTO,
                                nuPosition - 1, objRouteItemDTO2);
                    }
                });
            } else {
                nuHolder.btnRouteOrder_Up.setVisibility(View.VISIBLE);
                nuHolder.btnRouteOrder_Down.setVisibility(View.VISIBLE);

                nuHolder.btnRouteOrder_Up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View objView) {
                        RouteItemDTO objRouteItemDTO2 = lstRouteItemDTO.get(nuPosition - 1);
                        objListener.OnChangeOrder(nuPosition, objRouteItemDTO,
                                nuPosition - 1, objRouteItemDTO2);
                    }
                });

                nuHolder.btnRouteOrder_Down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View objView) {
                        RouteItemDTO objRouteItemDTO2 = lstRouteItemDTO.get(nuPosition + 1);
                        objListener.OnChangeOrder(nuPosition, objRouteItemDTO,
                                nuPosition + 1, objRouteItemDTO2);
                    }
                });
            }
        } else {
            nuHolder.btnRouteOrder_Up.setVisibility(View.INVISIBLE);
            nuHolder.btnRouteOrder_Down.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lstRouteItemDTO.size();
    }

}
