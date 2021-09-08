package com.cristiancollazos.paybird.view.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.interfaces.BluetoothPrinterNotifier;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.view.adapter.SimpleAdapter;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.BasePresenter;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.view.fragment.CreditManagementFragment;
import com.cristiancollazos.paybird.view.fragment.CustomerFragment;
import com.cristiancollazos.paybird.view.fragment.LoginFragment;
import com.cristiancollazos.paybird.view.fragment.PendingCreditsFragment;
import com.cristiancollazos.paybird.view.fragment.RouteOrderFragment;
import com.cristiancollazos.paybird.view.fragment.TodayPaymentsFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import service.BluetoothService;
import service.ConectThread;
import service.RunServicePrinter;

public class BaseActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, BasePresenter.View, AdapterView.OnItemSelectedListener {

    private static String TAG = BaseActivity.class.getSimpleName();
    private ActionBarDrawerToggle objToggle;
    private DrawerLayout objDrawer;
    private Toolbar objToolbar;
    private NavigationView objNavigationView;
    private BasePresenter objPresenter;
    private MaterialProgressBar objProgressbar;
    private RelativeLayout objProgressScreen;
    private TextView tvLoggedUserName, tvLoggedUserEmail, tvLoggedUserDocument,
            tvLoadingScreenText;
    private Spinner spBaseUser_Route;
    private RunServicePrinter objRunServicePrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        objToolbar = findViewById(R.id.toolBr_base);
        objDrawer = findViewById(R.id.drwLyt_base);
        objNavigationView = findViewById(R.id.navViw_base);
        objProgressbar = findViewById(R.id.pgsBar_base);
        objProgressScreen = findViewById(R.id.pgsScreen_base);
        tvLoadingScreenText = findViewById(R.id.pgsScreen_base_loadingText);

        View objHeaderView = objNavigationView.getHeaderView(0);
        Menu objMenuView = objNavigationView.getMenu();
        tvLoggedUserName = objHeaderView.findViewById(R.id.tvBaseUser_FullName);
        tvLoggedUserEmail = objHeaderView.findViewById(R.id.tvBaseUser_Email);
        tvLoggedUserDocument = objHeaderView.findViewById(R.id.tvBaseUser_Document);
        spBaseUser_Route = (Spinner) objMenuView.findItem(R.id.menItm_drwLyt_routes).getActionView();

        setSupportActionBar(objToolbar);

        objToggle = new ActionBarDrawerToggle(this, objDrawer, objToolbar,
                R.string.gen_drawer_open, R.string.gen_drawer_close);
        objDrawer.addDrawerListener(objToggle);
        objToggle.syncState();

        objNavigationView.setNavigationItemSelectedListener(this);
        spBaseUser_Route.setOnItemSelectedListener(this);

        objPresenter = new BasePresenter(this);

        setToolbarVisible(false);
        setDrawerEnabled(false);

        objPresenter.initializeApplication();
        objPresenter.checkIfAlarmsCreatingNeeded();
    }

    @Override
    public void onBackPressed() {
        if (objDrawer.isDrawerOpen(GravityCompat.START)) {
            objDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (Constants.FRAGMENTS_CREATED.size() > 0) {
                Constants.FRAGMENTS_CREATED.remove(Constants.FRAGMENTS_CREATED.size() - 1);
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem objMenuItem) {
        objDrawer.closeDrawer(GravityCompat.START);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (objMenuItem.getItemId()) {
                    case R.id.menItm_drwLyt_logout:
                        Utilities.showConfirmationDialog(BaseActivity.this,
                                getResources().getString(R.string.gen_logout_confirm),
                                new ConfirmDialogListener() {
                                    @Override
                                    public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                                        showProgressScreen("Cerrando sesión");
                                        objAlertDialog.dismiss();
                                        getBackToTodayFragment();
                                        objPresenter.doLogout();
                                    }

                                    @Override
                                    public void onCancel(AlertDialog objAlertDialog) {
                                        objAlertDialog.dismiss();
                                    }

                                },
                                R.drawable.ic_cancel_black_24dp);
                        break;

                    case R.id.menItm_drwLyt_customers:
                        if (!isFragmentDisplayed(CustomerFragment.class.getSimpleName())) {
                            getBackToTodayFragment();
                            CustomerFragment objCustomerFragment = new CustomerFragment();
                            replaceFragment(objCustomerFragment, true);
                        }
                        break;

                    case R.id.menItm_drwLyt_today:
                        if (!isFragmentDisplayed(TodayPaymentsFragment.class.getSimpleName())) {
                            if (isFragmentAlreadyCreated(TodayPaymentsFragment.class.getSimpleName())) {
                                onBackPressed();
                                int nuRequieredOnBack = getOnBackRequiredCount(
                                        TodayPaymentsFragment.class.getSimpleName());

                                while (nuRequieredOnBack > 0) {
                                    onBackPressed();
                                    nuRequieredOnBack--;
                                }
                            }
                        }
                        break;

                    case R.id.menItm_drwLyt_pending:
                        if (!isFragmentDisplayed(PendingCreditsFragment.class.getSimpleName())) {
                            getBackToTodayFragment();
                            PendingCreditsFragment objFragment = new PendingCreditsFragment();
                            replaceFragment(objFragment, true);
                        }
                        break;

                    case R.id.menItm_drwLyt_order:
                        if (!isFragmentDisplayed(RouteOrderFragment.class.getSimpleName())) {
                            getBackToTodayFragment();
                            RouteOrderFragment objFragment = new RouteOrderFragment();
                            replaceFragment(objFragment, true);
                        }
                        break;

                    case R.id.menItm_drwLyt_credits:
                        if (!isFragmentDisplayed(CreditManagementFragment.class.getSimpleName())) {
                            getBackToTodayFragment();
                            CreditManagementFragment objFragment = new CreditManagementFragment();
                            replaceFragment(objFragment, true);
                        }
                        break;
                }
            }
        }, 500);

        return true;
    }

    private void getBackToTodayFragment() {
        while (Constants.FRAGMENTS_CREATED.size() > 1) {
            onBackPressed();
        }
    }

    @Override
    public void onClick(View objView) {
        switch (objView.getId()) {
            case -1:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    public void replaceFragment(Fragment objFragment,
                                Boolean blAddToBacktack) {
        FragmentTransaction objTransaction = getSupportFragmentManager().beginTransaction();
        objTransaction.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_bottom,
                R.anim.enter_from_bottom, R.anim.exit_to_up);
        objTransaction.replace(R.id.frmLyt_base, objFragment);

        if (blAddToBacktack) {
            objTransaction.addToBackStack(null);
        }

        objTransaction.commit();
    }

    public void setToolbarVisible(Boolean blVisible) {
        if (blVisible) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
    }

    public void setDrawerEnabled(Boolean blEnabled) {
        if (blEnabled) {
            objDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            objToggle = new ActionBarDrawerToggle(this, objDrawer, objToolbar,
                    R.string.gen_drawer_open, R.string.gen_drawer_close);
            objDrawer.addDrawerListener(objToggle);
            objToggle.syncState();
        } else {
            if (objDrawer.isDrawerOpen(GravityCompat.START)) {
                objDrawer.closeDrawer(GravityCompat.START);
            }

            objDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            objToolbar.setNavigationOnClickListener(this);
        }
    }

    @Override
    public void showProgressBar() {
        objProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        objProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressScreen(String sbLoadingMessage) {
        tvLoadingScreenText.setText(sbLoadingMessage);
        objProgressScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressScreen() {
        objProgressScreen.setVisibility(View.GONE);
    }

    @Override
    public void redirectToLoginFragment() {
        if (isFragmentDisplayed(TodayPaymentsFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.remove(0);
        }
        LoginFragment objFragment = new LoginFragment();
        replaceFragment(objFragment, false);
    }

    @Override
    public void redirectToTodayFragment() {
        if (isFragmentDisplayed(LoginFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.remove(0);
        }
        TodayPaymentsFragment objFragment = new TodayPaymentsFragment();
        replaceFragment(objFragment, false);
    }

    public Boolean isFragmentAlreadyCreated(String sbFragmentName) {
        if (Constants.FRAGMENTS_CREATED.size() > 0) {
            int nuCounter = Constants.FRAGMENTS_CREATED.size();

            while (nuCounter > 0) {
                if (Constants.FRAGMENTS_CREATED.get(nuCounter - 1).equals(sbFragmentName)) {
                    return true;
                }

                nuCounter--;
            }
        }
        return false;
    }

    public int getOnBackRequiredCount(String sbFragmentName) {
        if (Constants.FRAGMENTS_CREATED.size() > 0) {
            int nuCounter = Constants.FRAGMENTS_CREATED.size();

            while (nuCounter > 0) {
                if (Constants.FRAGMENTS_CREATED.get(nuCounter - 1).equals(sbFragmentName)) {
                    break;
                }

                nuCounter--;
            }

            return Constants.FRAGMENTS_CREATED.size() - nuCounter;
        } else {
            return 0;
        }
    }

    public void setActionBarTitle(String sbTitle) {
        if (sbTitle != null) {
            objToolbar.setTitle(sbTitle);
        } else {
            objToolbar.setTitle(getResources().getString(R.string.gen_app_name));
        }
    }

    public void setActionBarSubtitle(String sbSubtitle) {
        if (sbSubtitle != null) {
            objToolbar.setSubtitle(sbSubtitle);
        } else {
            objToolbar.setSubtitle(null);
        }
    }

    public Boolean isFragmentDisplayed(String sbFragmentName) {
        if (Constants.FRAGMENTS_CREATED.size() > 0) {
            return Constants.FRAGMENTS_CREATED.get(Constants.FRAGMENTS_CREATED.size() - 1)
                    .equals(sbFragmentName);
        } else {
            return false;
        }
    }

    @Override
    public void setLoggedUserOnUi(String sbFullname, String sbEmail,
                                  String sbDocument, List<RouteDTO> lstRoutesDTO) {
        tvLoggedUserName.setText((sbFullname != null) ? sbFullname : "Sin definir");
        tvLoggedUserEmail.setText((sbEmail != null) ? sbEmail : "Sin definir");
        tvLoggedUserDocument.setText((sbDocument != null) ? sbDocument : "Sin definir");

        SimpleAdapter adpSimple = new SimpleAdapter<>(this, lstRoutesDTO);
        spBaseUser_Route.setAdapter(adpSimple);
    }

    @Override
    public void showErrorMessage(String sbError, String sbRecommend) {
        String sbMessageToShow = "<b>" + sbError + "</b><br/>" + (sbRecommend != null ? sbRecommend : "");
        Utilities.showErrorMessageDialog(this, Html.fromHtml(sbMessageToShow));
    }

    public RouteDTO getCurrentSelectedRoute() {
        return (RouteDTO) spBaseUser_Route.getSelectedItem();
    }

    public void setActiveMenuItem(String sbActiveMenuItem) {
        Menu objMenuView = objNavigationView.getMenu();

        if (sbActiveMenuItem.equals(TodayPaymentsFragment.class.getSimpleName())) {
            objMenuView.findItem(R.id.menItm_drwLyt_today).setChecked(true);
            objMenuView.findItem(R.id.menItm_drwLyt_customers).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_logout).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_pending).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_order).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_credits).setChecked(false);
        } else if (sbActiveMenuItem.equals(CustomerFragment.class.getSimpleName())) {
            objMenuView.findItem(R.id.menItm_drwLyt_customers).setChecked(true);
            objMenuView.findItem(R.id.menItm_drwLyt_today).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_logout).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_pending).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_order).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_credits).setChecked(false);
        } else if (sbActiveMenuItem.equals(PendingCreditsFragment.class.getSimpleName())) {
            objMenuView.findItem(R.id.menItm_drwLyt_pending).setChecked(true);
            objMenuView.findItem(R.id.menItm_drwLyt_today).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_logout).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_customers).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_order).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_credits).setChecked(false);
        } else if (sbActiveMenuItem.equals(RouteOrderFragment.class.getSimpleName())) {
            objMenuView.findItem(R.id.menItm_drwLyt_order).setChecked(true);
            objMenuView.findItem(R.id.menItm_drwLyt_pending).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_today).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_logout).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_customers).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_credits).setChecked(false);
        } else if (sbActiveMenuItem.equals(CreditManagementFragment.class.getSimpleName())) {
            objMenuView.findItem(R.id.menItm_drwLyt_credits).setChecked(true);
            objMenuView.findItem(R.id.menItm_drwLyt_order).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_pending).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_today).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_logout).setChecked(false);
            objMenuView.findItem(R.id.menItm_drwLyt_customers).setChecked(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> objAdapterView, View objView, int nuPosition, long l) {
        switch (objAdapterView.getId()) {
            case R.id.spBaseUser_Route:
                if (isFragmentDisplayed(TodayPaymentsFragment.class.getSimpleName())) {
                    TodayPaymentsFragment objTodayPaymentsFragment
                            = (TodayPaymentsFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.frmLyt_base);

                    objTodayPaymentsFragment.getPendingPayments();
                } else if (isFragmentDisplayed(PendingCreditsFragment.class.getSimpleName())) {
                    PendingCreditsFragment objPendingCreditsFragment
                            = (PendingCreditsFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.frmLyt_base);

                    objPendingCreditsFragment.getPendingCreditsAtRouteChange();
                } else if (isFragmentDisplayed(RouteOrderFragment.class.getSimpleName())) {
                    RouteOrderFragment objRouteOrderFragment
                            = (RouteOrderFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.frmLyt_base);

                    objRouteOrderFragment.getRouteOrderAtRouteChange();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void showExpiredSesion() {
        Utilities.showIndefinitedSnackBar(findViewById(R.id.frmLyt_base), getResources().getString(R.string.gen_sesion_expired));
    }

    public void getPrinterMacAndConnect(BluetoothPrinterNotifier objNotifier) {
        objPresenter.getPrinterMacAndConnect(objNotifier);
    }

    @Override
    public void connectToPrinter(String sbPrinterMac, BluetoothPrinterNotifier objNotifier) {
        objNotifier.onDisconnected();
        new BluetoothPrinterConnectTask().execute(sbPrinterMac, objNotifier);
    }

    public void disconectFromPrinter() {
        new BluetoothPrinterDisconnectTask().execute();
    }

    @Override
    public void notifyNoPrinterFound() {
        Utilities.showIndefinitedSnackBar(findViewById(R.id.frmLyt_base),
                getResources().getString(R.string.gen_printer_notfound));
    }

    class BluetoothPrinterConnectTask extends AsyncTask<Object, Void, Object> {

        BluetoothPrinterNotifier objNotifier;

        @Override
        protected Object doInBackground(Object... rcObjects) {
            try {
                objNotifier = (BluetoothPrinterNotifier) rcObjects[1];
                objNotifier.onConnecting();

                Log.i(TAG, "Connecting Bluetooth");
                if (!BluetoothService.isRunning || !ConectThread.conected) {
                    objRunServicePrinter = new RunServicePrinter(BaseActivity.this, (String) rcObjects[0]);
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
                    objRunServicePrinter.start();
                    sendBroadcast(new Intent().setAction(BluetoothService.ACTION_RESTART_CONECT_THREAD));
                }

                Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
                mapResponse.put(1, true);
                return mapResponse;
            } catch (Exception objException) {
                Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
                mapResponse.put(1, false);
                mapResponse.put(2, objException);
                return mapResponse;
            }
        }

        @Override
        protected void onPostExecute(Object objResponse) {
            Map<Integer, Object> mapResponse = (Map<Integer, Object>) objResponse;
            if ((Boolean) mapResponse.get(1)) {
                objNotifier.onConnected();
                Utilities.showSnackBar(findViewById(R.id.frmLyt_base),
                        getResources().getString(R.string.gen_printer_found));
            } else {
                objNotifier.onDisconnected();
            }
        }
    }

    class BluetoothPrinterDisconnectTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... rcSbParams) {
            try {
                Log.i(TAG, "Disconnecting Bluetooth");
                if ((BluetoothService.isRunning || ConectThread.conected)
                        && objRunServicePrinter != null) {
                    sendBroadcast(new Intent().setAction(BluetoothService.ACTION_THREAD_CLOSE));
                    objRunServicePrinter.stop();
                }

                Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
                mapResponse.put(1, true);
                return mapResponse;
            } catch (Exception objException) {
                Map<Integer, Object> mapResponse = new HashMap<Integer, Object>();
                mapResponse.put(1, false);
                mapResponse.put(2, objException);
                return mapResponse;
            }
        }

        @Override
        protected void onPostExecute(Object objResponse) {
            Map<Integer, Object> mapResponse = (Map<Integer, Object>) objResponse;
            if (!(Boolean) mapResponse.get(1)) {
                Exception objException = (Exception) mapResponse.get(2);
                showErrorMessage(getResources().getString(R.string.gen_printer_notfound),
                        objException.getMessage());
            }
        }
    }

    @Override
    public void createAlarms() {

    }

}
