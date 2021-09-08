package com.cristiancollazos.paybird.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;

public class CustomerInfoDialog extends DialogFragment {

    private View objView;
    private TextView tvCustomerInfo_FullName, tvCustomerInfo_Document, tvCustomerInfo_Address,
            tvCustomerInfo_Neighborhood, tvCustomerInfo_City, tvCustomerInfo_Contact;
    private CustomerDTO objCustomerDTO;

    public void setupDialog(CustomerDTO objCustomerDTO) {
        this.objCustomerDTO = objCustomerDTO;
    }

    @Override
    public void onCreate(Bundle objSavedInstanceState) {
        super.onCreate(objSavedInstanceState);
        LayoutInflater objInflater = LayoutInflater.from(getActivity());
        objView = objInflater.inflate(R.layout.dialog_customerinfo, null);

        tvCustomerInfo_FullName = objView.findViewById(R.id.tvCustomerInfo_FullName);
        tvCustomerInfo_Document = objView.findViewById(R.id.tvCustomerInfo_Document);
        tvCustomerInfo_Address = objView.findViewById(R.id.tvCustomerInfo_Address);
        tvCustomerInfo_Neighborhood = objView.findViewById(R.id.tvCustomerInfo_Neighborhood);
        tvCustomerInfo_City = objView.findViewById(R.id.tvCustomerInfo_City);
        tvCustomerInfo_Contact = objView.findViewById(R.id.tvCustomerInfo_Contact);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(getActivity());
        objBuilder.setView(objView);
        objBuilder.setPositiveButton(R.string.gen_dialog_ok_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int nuAction) {
                        dismiss();
                    }
                });

        return objBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        tvCustomerInfo_FullName.setText(objCustomerDTO.getSbName() + " "
                + objCustomerDTO.getSbLastName());
        tvCustomerInfo_Document.setText(objCustomerDTO.getSbDocumentType() + " "
                + objCustomerDTO.getSbDocument());
        tvCustomerInfo_Address.setText(objCustomerDTO.getSbHomeAddress());
        tvCustomerInfo_Neighborhood.setText(objCustomerDTO.getSbNeighborhoodName());
        tvCustomerInfo_City.setText(objCustomerDTO.getSbCityName());
        tvCustomerInfo_Contact.setText(objCustomerDTO.getSbHomePhoneNumber() + " - "
                + objCustomerDTO.getSbMobileNumber());
    }
}
