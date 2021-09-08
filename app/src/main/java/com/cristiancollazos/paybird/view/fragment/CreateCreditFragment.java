package com.cristiancollazos.paybird.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.ResizeAnimator;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.presenter.CreateCreditPresenter;
import com.cristiancollazos.paybird.print.NewCreditPrint;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.PeriodDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.SimpleAdapter;
import com.cristiancollazos.paybird.view.dialog.ChooseDateDialog;
import com.cristiancollazos.paybird.view.dialog.ConfirmCreditDialog;
import com.cristiancollazos.paybird.view.dialog.CustomerInfoDialog;
import com.cristiancollazos.paybird.view.dialog.QuantityChangeDialog;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnDialogDismissNotifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateCreditFragment extends Fragment implements View.OnClickListener,
        CreateCreditPresenter.View, View.OnFocusChangeListener, AdapterView.OnItemSelectedListener {

    private final String TAG = CreateCreditFragment.class.getSimpleName();
    private CustomerDTO objCustomerDTO;
    private TextView tvCreateCredit_Customer, tvCreateCredit_CreateDate, tvCreateCredit_DueDate,
            tvCreateCredit_InstallmentValue, tvCreateCredit_InstallmentQuantity,
            tvCreateCredit_NextPaymentDate;
    private EditText etCreateCredit_Value, etCreateCredit_Interest, etCreateCredit_Remainder;
    private ImageButton btnCreateCredit_CustomerInfo, btnCreateCredit_PickCreateDate,
            btnCreateCredit_PickNextPaymentDate;
    private ScrollView svCreateCredit_Content;
    private CardView cvCreateCredit_ValuesDiv, cvCreateCredit_InstallmentsDiv;
    private Button btnCreateCredit_PickDuration;
    private Spinner spCreateCredit_Period;
    private CreateCreditPresenter objPresenter;

    private List<PeriodDTO> lstPeriods;

    private SimpleDateFormat objDateFormatter;
    private Date dtCreateDate, dtDueDate, dtNextPaymentDate;
    private Integer nuCreditLength, nuInstallmentQuantity;
    private Float flCreditValue, flInterest, flRemainder, flInstallmentValue;

    private int nuCardHeight;

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(true);
        ((BaseActivity) getActivity()).setDrawerEnabled(false);
        ((BaseActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.createcredit_title));
        ((BaseActivity) getActivity()).setActionBarSubtitle(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater objInflater,
                             @Nullable ViewGroup objViewGroup,
                             @Nullable Bundle objSavedInstanceState) {
        setBaseEnvironment();
        setHasOptionsMenu(true);

        View objView = objInflater.inflate(R.layout.fragment_createcredit, null);
        tvCreateCredit_Customer = objView.findViewById(R.id.tvCreateCredit_Customer);
        tvCreateCredit_CreateDate = objView.findViewById(R.id.tvCreateCredit_CreateDate);
        tvCreateCredit_DueDate = objView.findViewById(R.id.tvCreateCredit_DueDate);
        tvCreateCredit_InstallmentValue = objView.findViewById(R.id.tvCreateCredit_InstallmentValue);
        tvCreateCredit_InstallmentQuantity = objView.findViewById(R.id.tvCreateCredit_InstallmentQuantity);
        tvCreateCredit_NextPaymentDate = objView.findViewById(R.id.tvCreateCredit_NextPaymentDate);
        etCreateCredit_Value = objView.findViewById(R.id.etCreateCredit_Value);
        etCreateCredit_Interest = objView.findViewById(R.id.etCreateCredit_Interest);
        etCreateCredit_Remainder = objView.findViewById(R.id.etCreateCredit_Remainder);
        btnCreateCredit_CustomerInfo = objView.findViewById(R.id.btnCreateCredit_CustomerInfo);
        btnCreateCredit_PickDuration = objView.findViewById(R.id.btnCreateCredit_PickDuration);
        btnCreateCredit_PickCreateDate = objView.findViewById(R.id.btnCreateCredit_PickCreateDate);
        btnCreateCredit_PickNextPaymentDate = objView.findViewById(R.id.btnCreateCredit_PickNextPaymentDate);
        svCreateCredit_Content = objView.findViewById(R.id.svCreateCredit_Content);
        cvCreateCredit_ValuesDiv = objView.findViewById(R.id.cvCreateCredit_ValuesDiv);
        cvCreateCredit_InstallmentsDiv = objView.findViewById(R.id.cvCreateCredit_InstallmentsDiv);
        spCreateCredit_Period = objView.findViewById(R.id.spCreateCredit_Period);

        etCreateCredit_Value.setOnFocusChangeListener(this);
        etCreateCredit_Interest.setOnFocusChangeListener(this);
        etCreateCredit_Remainder.setOnFocusChangeListener(this);

        btnCreateCredit_CustomerInfo.setOnClickListener(this);
        btnCreateCredit_PickCreateDate.setOnClickListener(this);
        btnCreateCredit_PickNextPaymentDate.setOnClickListener(this);
        btnCreateCredit_PickDuration.setOnClickListener(this);

        spCreateCredit_Period.setOnItemSelectedListener(this);

        etCreateCredit_Value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sbCharSequence,
                                          int nuStart,
                                          int nuCount,
                                          int nuAfter) {
            }

            @Override
            public void onTextChanged(CharSequence sbCharSequence,
                                      int nuStart,
                                      int nuBefore,
                                      int nuCount) {

            }

            @Override
            public void afterTextChanged(Editable objEditable) {

            }
        });

        etCreateCredit_Interest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sbCharSequence,
                                          int nuStart,
                                          int nuCount,
                                          int nuAfter) {

            }

            @Override
            public void onTextChanged(CharSequence sbCharSequence,
                                      int nuStart,
                                      int nuBefore,
                                      int nuCount) {
            }

            @Override
            public void afterTextChanged(Editable objEditable) {
                String sbInterest = objEditable.toString();
                String[] rcStrings = sbInterest.split("\\.");
                if (rcStrings.length > 2) {
                    sbInterest = rcStrings[0] + "." + rcStrings[1];
                    etCreateCredit_Interest.setText(sbInterest);
                    etCreateCredit_Interest.setSelection(sbInterest.length());
                } else if (rcStrings.length == 2) {
                    String sbSecondVal = rcStrings[1];
                    if (sbSecondVal.length() > 2) {
                        sbInterest = rcStrings[0] + "." + sbSecondVal.substring(0, 2);
                        etCreateCredit_Interest.setText(sbInterest);
                        etCreateCredit_Interest.setSelection(sbInterest.length());
                    }
                    if (sbInterest.endsWith(".")) {
                        sbInterest = rcStrings[0] + "." + sbSecondVal.substring(0, sbSecondVal.length());
                        etCreateCredit_Interest.setText(sbInterest);
                        etCreateCredit_Interest.setSelection(sbInterest.length());
                    }
                } else {
                    if (sbInterest.endsWith("..")) {
                        sbInterest = sbInterest.substring(0, sbInterest.length() - 1);
                        etCreateCredit_Interest.setText(sbInterest);
                        etCreateCredit_Interest.setSelection(sbInterest.length());
                    }
                }
            }
        });

        etCreateCredit_Remainder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sbCharSequence,
                                          int nuStart,
                                          int nuCount,
                                          int nuAfter) {

            }

            @Override
            public void onTextChanged(CharSequence sbCharSequence,
                                      int nuStart,
                                      int nuBefore,
                                      int nuCount) {

            }

            @Override
            public void afterTextChanged(Editable objEditable) {

            }
        });

        initializeFragment();
        return objView;
    }

    public void initializeFragment() {
        objPresenter = new CreateCreditPresenter(this);

        objDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        dtCreateDate = new Date();
        nuCreditLength = 62;
        dtDueDate = Utilities.getIncrementedDateWithDays(dtCreateDate, nuCreditLength);
        dtNextPaymentDate = Utilities.getIncrementedDateWithDays(dtCreateDate, 1);
        lstPeriods = new ArrayList<>();
        nuInstallmentQuantity = (nuCreditLength / 1);
        flCreditValue = 0F;
        flInterest = 20F;
        flRemainder = 0F;
        flInstallmentValue = 0F;

        tvCreateCredit_CreateDate.setText(objDateFormatter.format(dtCreateDate));
        btnCreateCredit_PickDuration.setText(getResources().getString(R.string.createcredit_duration_value, nuCreditLength));
        tvCreateCredit_DueDate.setText(objDateFormatter.format(dtDueDate));
        tvCreateCredit_NextPaymentDate.setText(objDateFormatter.format(dtNextPaymentDate));
        tvCreateCredit_InstallmentQuantity.setText(String.valueOf(nuInstallmentQuantity));
        etCreateCredit_Value.setText(Utilities.getFormattedValueWoSign(flCreditValue));
        etCreateCredit_Interest.setText(Utilities.getFormattedValueWoSign(flInterest));
        etCreateCredit_Remainder.setText(Utilities.getFormattedValueWoSign(flInstallmentValue));
        tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(flInstallmentValue));

        objPresenter.getPeriods();
    }

    public void setCustomerData(CustomerDTO objCustomerDTO) {
        this.objCustomerDTO = objCustomerDTO;
    }

    @Override
    public void onCreateOptionsMenu(Menu objMenu, MenuInflater objMenuInflater) {
        objMenuInflater.inflate(R.menu.menu_createcredit, objMenu);
        super.onCreateOptionsMenu(objMenu, objMenuInflater);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!((BaseActivity) getActivity())
                .isFragmentAlreadyCreated(CreateCreditFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.add(CreateCreditFragment.class.getSimpleName());
        }

        tvCreateCredit_Customer.setText(objCustomerDTO.getSbName() + " "
                + objCustomerDTO.getSbLastName());
    }

    @Override
    public void onClick(View objView) {
        ChooseDateDialog objChooseDateDialog;

        switch (objView.getId()) {
            case R.id.btnCreateCredit_CustomerInfo:
                etCreateCredit_Value.clearFocus();
                etCreateCredit_Interest.clearFocus();
                etCreateCredit_Remainder.clearFocus();

                objPresenter.getCustomerByDocument(objCustomerDTO.getSbDocument());
                break;

            case R.id.btnCreateCredit_PickCreateDate:
                etCreateCredit_Value.clearFocus();
                etCreateCredit_Interest.clearFocus();
                etCreateCredit_Remainder.clearFocus();

                objChooseDateDialog = new ChooseDateDialog();

                objChooseDateDialog.setupDialog(new ConfirmDialogListener() {
                    @Override
                    public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                        dtCreateDate = (Date) rcDialogData[0];
                        dtDueDate = Utilities.getIncrementedDateWithDays(dtCreateDate, nuCreditLength);
                        PeriodDTO objSelectPeriodDTO = (PeriodDTO) spCreateCredit_Period.getSelectedItem();

                        if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_DIARIO) {
                            dtNextPaymentDate = Utilities
                                    .getIncrementedDateWithDays(dtCreateDate, 1);
                        } else if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_SEMANAL) {
                            dtNextPaymentDate = Utilities
                                    .getIncrementedDateWithDays(dtCreateDate, 7);
                        } else if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_QUINCENAL) {
                            dtNextPaymentDate = Utilities
                                    .getIncrementedDateWithDays(dtCreateDate, 14);
                        } else {
                            dtNextPaymentDate = Utilities
                                    .getIncrementedDateWithMonths(dtCreateDate, 1);
                        }

                        tvCreateCredit_CreateDate.setText(objDateFormatter.format(dtCreateDate));
                        tvCreateCredit_DueDate.setText(objDateFormatter.format(dtDueDate));
                        tvCreateCredit_NextPaymentDate.setText(objDateFormatter.format(dtNextPaymentDate));

                        objAlertDialog.dismiss();
                    }

                    @Override
                    public void onCancel(AlertDialog objAlertDialog) {
                        objAlertDialog.dismiss();
                    }
                }, dtCreateDate, new Date(), null);

                objChooseDateDialog.show(getFragmentManager(), "ChooseDateDialog");
                break;

            case R.id.btnCreateCredit_PickNextPaymentDate:
                etCreateCredit_Value.clearFocus();
                etCreateCredit_Interest.clearFocus();
                etCreateCredit_Remainder.clearFocus();

                objChooseDateDialog = new ChooseDateDialog();

                objChooseDateDialog.setupDialog(new ConfirmDialogListener() {
                    @Override
                    public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                        dtNextPaymentDate = (Date) rcDialogData[0];
                        tvCreateCredit_NextPaymentDate.setText(objDateFormatter.format(dtNextPaymentDate));
                        objAlertDialog.dismiss();
                    }

                    @Override
                    public void onCancel(AlertDialog objAlertDialog) {
                        objAlertDialog.dismiss();
                    }
                }, dtNextPaymentDate, dtCreateDate, null);

                objChooseDateDialog.show(getFragmentManager(), "ChooseDateDialog");
                break;

            case R.id.btnCreateCredit_PickDuration:
                etCreateCredit_Value.clearFocus();
                etCreateCredit_Interest.clearFocus();
                etCreateCredit_Remainder.clearFocus();

                QuantityChangeDialog objQuantityChangeDialog = new QuantityChangeDialog();
                objQuantityChangeDialog.setupDialog(new ConfirmDialogListener() {
                    @Override
                    public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                        nuCreditLength = (Integer) rcDialogData[0];
                        dtDueDate = Utilities.getIncrementedDateWithDays(dtCreateDate, nuCreditLength);

                        PeriodDTO objSelectPeriodDTO = (PeriodDTO) spCreateCredit_Period.getSelectedItem();
                        if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_DIARIO) {
                            nuInstallmentQuantity = nuCreditLength / 1;
                        } else if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_SEMANAL) {
                            nuInstallmentQuantity = nuCreditLength / 7;
                        } else if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_QUINCENAL) {
                            nuInstallmentQuantity = nuCreditLength / 14;
                        } else {
                            nuInstallmentQuantity = nuCreditLength / 30;
                        }

                        tvCreateCredit_DueDate.setText(objDateFormatter.format(dtDueDate));
                        tvCreateCredit_InstallmentQuantity.setText(String.valueOf(nuInstallmentQuantity));
                        btnCreateCredit_PickDuration.setText(
                                getResources().getString(R.string.createcredit_duration_value, nuCreditLength));

                        calculateValues();

                        objAlertDialog.dismiss();
                    }

                    @Override
                    public void onCancel(AlertDialog objAlertDialog) {
                        objAlertDialog.dismiss();
                    }
                }, nuCreditLength);
                objQuantityChangeDialog.show(getFragmentManager(), "QuantityChangeDialog");
                break;
        }
    }

    @Override
    public void onFocusChange(final View objView, final boolean blBoolean) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (objView.getId()) {
                    case R.id.etCreateCredit_Value:
                        if (blBoolean) {
                            nuCardHeight = cvCreateCredit_InstallmentsDiv.getHeight();
                            svCreateCredit_Content
                                    .scrollTo(0, (etCreateCredit_Value.getBottom() + nuCardHeight));
                        }

                        calculateValues();
                        break;
                    case R.id.etCreateCredit_Interest:
                        if (blBoolean) {
                            nuCardHeight = cvCreateCredit_InstallmentsDiv.getHeight();
                            svCreateCredit_Content
                                    .scrollTo(0, (etCreateCredit_Interest.getBottom() + nuCardHeight));
                        }

                        calculateValues();
                        break;
                    case R.id.etCreateCredit_Remainder:
                        if (blBoolean) {
                            nuCardHeight = cvCreateCredit_InstallmentsDiv.getHeight();
                            svCreateCredit_Content
                                    .scrollTo(0, (etCreateCredit_Remainder.getBottom() + nuCardHeight));
                        } else {
                            calculateValuesFromRemainder();
                        }
                        break;
                }
            }
        }, 500);
    }

    @Override
    public void onItemSelected(AdapterView<?> objAdapterView, View objView, int nuPosition, long lgVar) {
        etCreateCredit_Value.clearFocus();
        etCreateCredit_Interest.clearFocus();
        etCreateCredit_Remainder.clearFocus();

        switch (objAdapterView.getId()) {
            case R.id.spCreateCredit_Period:
                PeriodDTO objSelectPeriodDTO = lstPeriods.get(nuPosition);
                if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_DIARIO) {
                    dtNextPaymentDate = Utilities.getIncrementedDateWithDays(dtCreateDate, 1);
                    nuInstallmentQuantity = nuCreditLength / 1;
                } else if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_SEMANAL) {
                    dtNextPaymentDate = Utilities.getIncrementedDateWithDays(dtCreateDate, 7);
                    nuInstallmentQuantity = nuCreditLength / 7;
                } else if (objSelectPeriodDTO.getNuCode() == Constants.TIEMPOPAGO_QUINCENAL) {
                    dtNextPaymentDate = Utilities.getIncrementedDateWithDays(dtCreateDate, 14);
                    nuInstallmentQuantity = nuCreditLength / 14;
                } else {
                    dtNextPaymentDate = Utilities.getIncrementedDateWithMonths(dtCreateDate, 1);
                    nuInstallmentQuantity = nuCreditLength / 30;
                }

                tvCreateCredit_NextPaymentDate.setText(objDateFormatter.format(dtNextPaymentDate));
                tvCreateCredit_InstallmentQuantity.setText(String.valueOf(nuInstallmentQuantity));
                break;
        }

        calculateValues();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        etCreateCredit_Value.clearFocus();
        etCreateCredit_Interest.clearFocus();
        etCreateCredit_Remainder.clearFocus();
    }

    public void calculateValues() {
        if (!etCreateCredit_Value.getText().toString().isEmpty()) {
            String sbValue = etCreateCredit_Value.getText().toString();
            Float flValue = Float.valueOf(sbValue);

            if (flValue > 0) {
                if (!etCreateCredit_Interest.getText().toString().isEmpty()) {
                    String sbInterest = etCreateCredit_Interest.getText().toString();
                    Float flInterest = Float.valueOf(sbInterest);

                    if (flInterest > 0) {
                        this.flCreditValue = flValue;
                        this.flInterest = flInterest;
                        this.flRemainder = flValue + ((flValue / 100F) * flInterest);
                        this.flInstallmentValue = this.flRemainder / nuInstallmentQuantity;

                        etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
                        tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(this.flInstallmentValue));
                    } else {
                        this.flRemainder = flValue + ((flValue / 100F) * 0F);
                        this.flInstallmentValue = this.flRemainder / nuInstallmentQuantity;

                        if (flInstallmentValue < 0) {
                            flInstallmentValue = 0F;
                        }

                        etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
                        tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(this.flInstallmentValue));
                    }
                } else {
                    this.flRemainder = flValue;
                    this.flInstallmentValue = this.flRemainder / nuInstallmentQuantity;

                    if (flInstallmentValue < 0) {
                        flInstallmentValue = 0F;
                    }

                    etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
                    tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(this.flInstallmentValue));
                }
            } else {
                etCreateCredit_Remainder.setText("0");
                tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(0F));
            }
        } else {
            etCreateCredit_Remainder.setText("0");
            tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(0F));
        }
    }

    public void calculateValuesFromRemainder() {
        if (!etCreateCredit_Remainder.getText().toString().isEmpty()) {
            String sbRemainderValue = etCreateCredit_Remainder.getText().toString();
            Float flRemainderValue = Float.valueOf(sbRemainderValue);

            if (flRemainderValue > 0) {
                if (!etCreateCredit_Value.getText().toString().isEmpty()) {
                    String sbCreditValue = etCreateCredit_Value.getText().toString();
                    Float flCreditValue = Float.valueOf(sbCreditValue);

                    if (flCreditValue > flRemainderValue) {
                        etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
                        tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(
                                this.flInstallmentValue));
                        showErrorMessage(ErrorConstants.E0013.def(), ErrorConstants.E0013.recommend());
                    } else {
                        if (flCreditValue > 0) {
                            this.flCreditValue = flCreditValue;
                            this.flRemainder = flRemainderValue;

                            this.flInterest = (((this.flRemainder - this.flCreditValue) * 100) /
                                    this.flCreditValue);
                            this.flInstallmentValue = this.flRemainder / nuInstallmentQuantity;

                            etCreateCredit_Interest.setText(String.valueOf(this.flInterest));
                        } else {
                            etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
                            tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(
                                    this.flInstallmentValue));
                        }
                    }
                } else {
                    etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
                }
            } else {
                etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
            }
        } else {
            etCreateCredit_Remainder.setText(String.valueOf(this.flRemainder));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem objMenuItem) {
        switch (objMenuItem.getItemId()) {
            case R.id.abAction_CreateCredit_CleanForm:
                cleanForm();
                break;
            case R.id.abAction_CreateCredit_DoCreate:
                if (lstPeriods.size() == 0) {
                    showErrorMessage(ErrorConstants.E0014.def(), ErrorConstants.E0014.recommend());
                    break;
                }

                if (flCreditValue <= 0) {
                    showErrorMessage(ErrorConstants.E0015.def(), ErrorConstants.E0015.recommend());
                    break;
                }

                CreditDTO objCreditDTO = new CreditDTO();
                objCreditDTO.setDtCreatedDate(dtCreateDate);
                objCreditDTO.setFlValue(flCreditValue);
                objCreditDTO.setFlInterest(flInterest);
                objCreditDTO.setFlRemainder(flRemainder);
                objCreditDTO.setDtLastPayment(dtCreateDate);
                objCreditDTO.setNuCustomerCode(objCustomerDTO.getNuCode());
                objCreditDTO.setNuLength(nuCreditLength);
                objCreditDTO.setNuPeriod(((PeriodDTO) spCreateCredit_Period.getSelectedItem()).getNuCode());
                objCreditDTO.setNuCoOwnerCode(749);
                objCreditDTO.setDtNextPayment(dtNextPaymentDate);
                objCreditDTO.setFlInstallmentValue(flInstallmentValue);
                objCreditDTO.setNuInstallmentQuantity(nuInstallmentQuantity);

                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                objPresenter.getRouteOrder(objRouteDTO.getNuCode(), objCreditDTO);
                break;
        }
        return true;
    }

    public void cleanForm() {
        dtCreateDate = new Date();
        nuCreditLength = 62;
        dtDueDate = Utilities.getIncrementedDateWithDays(dtCreateDate, nuCreditLength);
        dtNextPaymentDate = Utilities.getIncrementedDateWithDays(dtCreateDate, 1);
        nuInstallmentQuantity = (nuCreditLength / 1);
        flCreditValue = 0F;
        flInterest = 20F;
        flRemainder = 0F;
        flInstallmentValue = 0F;

        tvCreateCredit_CreateDate.setText(objDateFormatter.format(dtCreateDate));
        btnCreateCredit_PickDuration.setText(getResources().getString(R.string.createcredit_duration_value, nuCreditLength));
        tvCreateCredit_DueDate.setText(objDateFormatter.format(dtDueDate));
        tvCreateCredit_NextPaymentDate.setText(objDateFormatter.format(dtNextPaymentDate));
        tvCreateCredit_InstallmentQuantity.setText(String.valueOf(nuInstallmentQuantity));
        etCreateCredit_Value.setText(Utilities.getFormattedValueWoSign(flCreditValue));
        etCreateCredit_Interest.setText(Utilities.getFormattedValueWoSign(flInterest));
        etCreateCredit_Remainder.setText(Utilities.getFormattedValueWoSign(flInstallmentValue));
        tvCreateCredit_InstallmentValue.setText(Utilities.getFormattedValue(flInstallmentValue));
        spCreateCredit_Period.setSelection(0);
    }

    @Override
    public void showProgress() {
        ((BaseActivity) getActivity()).showProgressBar();
    }

    @Override
    public void hideProgress() {
        ((BaseActivity) getActivity()).hideProgressBar();
    }

    @Override
    public void showErrorMessage(String sbError, String sbRecommend) {
        try {
            ((BaseActivity) getActivity()).showErrorMessage(sbError, sbRecommend);
        } catch (Exception objException) {
            Log.e(TAG, "El mensaje de error no fue desplegado porque no existia el fragmento.");
        }
    }

    @Override
    public void showSuccessMessage(String sbMessage) {
        Utilities.showSnackBar(getView(), sbMessage);
        cleanForm();
    }

    @Override
    public void renderCustomerData(CustomerDTO objCustomerDTO) {
        CustomerInfoDialog objCustomerInfoDialog = new CustomerInfoDialog();
        objCustomerInfoDialog.setupDialog(objCustomerDTO);
        objCustomerInfoDialog.show(getFragmentManager(), "CustomerInfoDialog");
    }

    @Override
    public void renderPeriods(List<PeriodDTO> lsPeriodDTO) {
        this.lstPeriods.clear();
        this.lstPeriods.addAll(lsPeriodDTO);
        SimpleAdapter<PeriodDTO> objAdapter = new SimpleAdapter<>(getActivity(), this.lstPeriods);
        spCreateCredit_Period.setAdapter(objAdapter);
    }

    @Override
    public void renderRouteOrder(final List<RouteItemDTO> lstRouteItemDTO,
                                 final CreditDTO objCreditDTO,
                                 final Integer nuUserCode) {
        nuCardHeight = cvCreateCredit_InstallmentsDiv.getHeight();
        ResizeAnimator objAnimator = new ResizeAnimator(cvCreateCredit_InstallmentsDiv,
                (nuCardHeight * -1), nuCardHeight);
        objAnimator.setDuration(300);
        cvCreateCredit_InstallmentsDiv.startAnimation(objAnimator);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConfirmCreditDialog objConfirmCreditDialog = new ConfirmCreditDialog();
                objConfirmCreditDialog.setupDialog(objCustomerDTO, objCreditDTO, dtDueDate,
                        new ConfirmDialogListener() {
                            @Override
                            public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                                Integer nuRouteOrderPosition = (Integer) rcDialogData[0];
                                RouteDTO objRouteDTO = ((BaseActivity) getActivity())
                                        .getCurrentSelectedRoute();

                                objPresenter.setNewCredit(objCreditDTO, objRouteDTO.getNuCode(),
                                        nuRouteOrderPosition, nuUserCode);
                            }

                            @Override
                            public void onCancel(AlertDialog objAlertDialog) {
                                objAlertDialog.dismiss();
                            }
                        }, lstRouteItemDTO,
                        new OnDialogDismissNotifier() {
                            @Override
                            public void OnDialogDismiss() {
                                ResizeAnimator objAnimator = new ResizeAnimator(cvCreateCredit_InstallmentsDiv,
                                        nuCardHeight, 0);
                                objAnimator.setDuration(300);
                                cvCreateCredit_InstallmentsDiv.startAnimation(objAnimator);
                            }
                        });
                objConfirmCreditDialog.show(getFragmentManager(), "ConfirmCreditDialog");
            }
        }, 300);
    }

    @Override
    public void showProgressScreen(String sbLoadingMessage) {
        ((BaseActivity) getActivity()).showProgressScreen(sbLoadingMessage);
    }

    @Override
    public void hideProgressScreen() {
        ((BaseActivity) getActivity()).hideProgressScreen();
    }

    @Override
    public void promptPrintOption(final CreditDTO objRecentlyCreatedCredit) {
        Utilities.showConfirmationDialog(getActivity(),
                getResources().getString(R.string.confirmcredit_donetoprint), new ConfirmDialogListener() {
            @Override
            public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                NewCreditPrint objNewCreditPrint = new NewCreditPrint(objRecentlyCreatedCredit);
                String sbDataToPrint = objNewCreditPrint.getPrint();
                Utilities.doPrint(getActivity(), sbDataToPrint);

                ConfirmCreditDialog objConfirmCreditDialog = (ConfirmCreditDialog)
                        getFragmentManager().findFragmentByTag("ConfirmCreditDialog");
                objConfirmCreditDialog.dismiss();

                showSuccessMessage("Credito creado exitosamente");

                objAlertDialog.dismiss();
            }

            @Override
            public void onCancel(AlertDialog objAlertDialog) {
                ConfirmCreditDialog objConfirmCreditDialog = (ConfirmCreditDialog)
                        getFragmentManager().findFragmentByTag("ConfirmCreditDialog");
                objConfirmCreditDialog.dismiss();

                showSuccessMessage("Credito creado exitosamente");

                objAlertDialog.dismiss();
            }
        }, R.drawable.ic_done_black_24dp);
    }

}
