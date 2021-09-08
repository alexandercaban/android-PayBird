package com.cristiancollazos.paybird.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.RouteOrderPresenter;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.RouteOrderAdapter;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnChangeOrderListener;

import java.util.ArrayList;
import java.util.List;

public class RouteOrderFragment extends Fragment
        implements RouteOrderPresenter.View, SwipeRefreshLayout.OnRefreshListener,
        OnChangeOrderListener<RouteItemDTO>{

    private final String TAG = RouteOrderFragment.class.getSimpleName();
    private RecyclerView rvRouteOrder;
    private TextView tvRouteOrder_Subtitle;
    private RouteOrderPresenter objRouteOrderPresenter;
    private List<RouteItemDTO> lstRouteItemDTO;
    private RelativeLayout rlRouteOrder_NO_DATA_FOUND;
    private SwipeRefreshLayout srvRouteOrder;

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(true);
        ((BaseActivity) getActivity()).setDrawerEnabled(true);
        ((BaseActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.routeorder_menu_title));
        ((BaseActivity) getActivity()).setActionBarSubtitle(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater objInflater,
                             @Nullable ViewGroup objContainer,
                             @Nullable Bundle objSavedInstanceState) {
        setBaseEnvironment();

        View objView = objInflater.inflate(R.layout.fragment_routeorder, null);
        rvRouteOrder = objView.findViewById(R.id.rvRouteOrder);
        tvRouteOrder_Subtitle = objView.findViewById(R.id.tvRouteOrder_Subtitle);
        rlRouteOrder_NO_DATA_FOUND = objView.findViewById(R.id.rlRouteOrder_NO_DATA_FOUND);
        srvRouteOrder = objView.findViewById(R.id.srvRouteOrder);

        LinearLayoutManager objLayoutManager = new LinearLayoutManager(getActivity());
        objLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRouteOrder.setLayoutManager(objLayoutManager);
        lstRouteItemDTO = new ArrayList<>();
        RouteOrderAdapter objAdapter = new RouteOrderAdapter(lstRouteItemDTO, this);
        rvRouteOrder.setAdapter(objAdapter);
        srvRouteOrder.setOnRefreshListener(this);

        initializeFragment();
        return objView;
    }

    public void initializeFragment() {
        RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        tvRouteOrder_Subtitle.setText(getResources().getString(R.string.routeorder_menu_subtitle,
                objRouteDTO.getSbName()));
        objRouteOrderPresenter = new RouteOrderPresenter(this);
        objRouteOrderPresenter.getRouteOrderByRoute(objRouteDTO.getNuCode());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!((BaseActivity) getActivity())
                .isFragmentAlreadyCreated(RouteOrderFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.add(RouteOrderFragment.class.getSimpleName());
        }

        ((BaseActivity) getActivity()).setActiveMenuItem(RouteOrderFragment.class.getSimpleName());
    }

    @Override
    public void onRefresh() {
        RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        objRouteOrderPresenter.getRouteOrderByRoute(objRouteDTO.getNuCode());
    }

    public void getRouteOrderAtRouteChange() {
        RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        tvRouteOrder_Subtitle.setText(getResources().getString(R.string.routeorder_menu_subtitle,
                objRouteDTO.getSbName()));
        objRouteOrderPresenter.getRouteOrderByRoute(objRouteDTO.getNuCode());
    }

    @Override
    public void showProgress() {
        ((BaseActivity) getActivity()).showProgressBar();
    }

    @Override
    public void hideProgress() {
        ((BaseActivity) getActivity()).hideProgressBar();
        srvRouteOrder.setRefreshing(false);
    }

    @Override
    public void showErrorMessage(Integer nuCode, String sbError, String sbRecommend) {
        ((BaseActivity) getActivity()).showErrorMessage(sbError, sbRecommend);
    }

    @Override
    public void showSuccessMessage(String sbMessage) {
        Utilities.showSnackBar(getView(), sbMessage);
    }

    @Override
    public void renderRouteOrder(List<RouteItemDTO> lstRouteItemDTO) {
        this.lstRouteItemDTO.clear();
        this.lstRouteItemDTO.addAll(lstRouteItemDTO);
        rlRouteOrder_NO_DATA_FOUND.setVisibility(View.GONE);
        rvRouteOrder.setVisibility(View.VISIBLE);
        rvRouteOrder.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void notifyNoRouteOrderItemsFound() {
        this.lstRouteItemDTO.clear();
        rvRouteOrder.getAdapter().notifyDataSetChanged();
        rlRouteOrder_NO_DATA_FOUND.setVisibility(View.VISIBLE);
        rvRouteOrder.setVisibility(View.GONE);
    }

    @Override
    public void OnChangeOrder(int nuPositionSubject, RouteItemDTO objSubject,
                              int nuPositionTarget, RouteItemDTO objTarget) {
        RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        objRouteOrderPresenter.setChangeRouteOrder(objRouteDTO.getNuCode(),
                objSubject.getNuOrder(), nuPositionSubject, objTarget.getNuOrder(),
                nuPositionTarget);
    }

    @Override
    public void notifyRouteOrderChange(Integer nuPositionFrom,
                                       Integer nuOrderFrom,
                                       Integer nuPositionTo,
                                       Integer nuOrderTo) {
        RouteItemDTO objRouteItemDTOFrom = lstRouteItemDTO.get(nuPositionFrom);
        RouteItemDTO objRouteItemDTOTo = lstRouteItemDTO.get(nuPositionTo);
        objRouteItemDTOFrom.setNuOrder(nuOrderTo);
        objRouteItemDTOTo.setNuOrder(nuOrderFrom);
        lstRouteItemDTO.set(nuPositionFrom, objRouteItemDTOTo);
        lstRouteItemDTO.set(nuPositionTo, objRouteItemDTOFrom);
        rvRouteOrder.getAdapter().notifyItemMoved(nuPositionFrom, nuPositionTo);
        rvRouteOrder.getAdapter().notifyItemChanged(nuPositionFrom);
        rvRouteOrder.getAdapter().notifyItemChanged(nuPositionTo);
    }

}
