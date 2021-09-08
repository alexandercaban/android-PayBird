package com.playtechla.bluetoothlink;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LinkBluetoothDeviceAdapter extends BaseAdapter {
	public static final String TAG = "LinkBluetoothDeviceAdapter";

	protected Activity objActivity;
	protected ArrayList<BluetoothDevice> objItems;
	
	public LinkBluetoothDeviceAdapter(Activity objActivity, ArrayList<BluetoothDevice> objItems) {
		this.objActivity = objActivity;
		this.objItems = objItems;
	}

	@Override
	public int getCount() {
		return this.objItems.size();
	}

	@Override
	public Object getItem(int position) {
		return this.objItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null){
			view = LayoutInflater.from(this.objActivity).inflate(R.layout.item_device, null);
		}
		
		BluetoothDevice objDevice = (BluetoothDevice) this.getItem(position);
		TextView lblName = (TextView) view.findViewById(R.id.lblName);
		TextView lblAddress = (TextView) view.findViewById(R.id.lblAddress);
		
		lblName.setText(objDevice.getName());
		lblAddress.setText(objDevice.getAddress());
		
		view.setTag(objDevice);
		
		return view;
	}

}
