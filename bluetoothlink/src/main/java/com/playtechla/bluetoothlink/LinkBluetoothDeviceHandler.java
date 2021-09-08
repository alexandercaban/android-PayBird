package com.playtechla.bluetoothlink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class LinkBluetoothDeviceHandler implements OnItemClickListener {
	public static final String TAG = "LinkBluetoothHandler";
	
	public static final String EXTRA_ADDRESS = "BT.Address";
	public static final String EXTRA_DEVICE_NAME = "BT.Name";
	public static final int REQUEST_BLUETOOTH_TURN_ON = 100;
	public static final int REQUEST_PAIR_BLUETOOTH_DEVICE = 110;
	
	private LinkBluetoothDeviceActivity objLinkBTActivity;
	private LinkBluetoothDeviceAdapter objLinkBTAdapter;
	private ArrayList<com.playtechla.bluetoothlink.BluetoothDevice> objDevices = new ArrayList<com.playtechla.bluetoothlink.BluetoothDevice>();
	private ProgressDialog objProgress;
	
	public LinkBluetoothDeviceHandler(LinkBluetoothDeviceActivity objLinkBTActivity){
		this.objLinkBTActivity = objLinkBTActivity;
	}
	
	public void init(){
		try {
			this.objProgress = new ProgressDialog(this.objLinkBTActivity);
			this.objProgress.setTitle("");
			this.objLinkBTAdapter = new LinkBluetoothDeviceAdapter(this.objLinkBTActivity, new ArrayList<com.playtechla.bluetoothlink.BluetoothDevice>());
			
			this.objLinkBTActivity.lstDevices.setOnItemClickListener(this);
			this.objLinkBTActivity.lstDevices.setAdapter(this.objLinkBTAdapter);
			
			this.getDevices(null);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if(requestCode == REQUEST_BLUETOOTH_TURN_ON){
				if(resultCode == Activity.RESULT_OK){
					this.onResultOkBluetoothOn();
				}
				else{
					this.onResultCancelBluetoothOn();
				}
			} else if(requestCode == REQUEST_PAIR_BLUETOOTH_DEVICE){
				if(resultCode == Activity.RESULT_OK){
					this.onResultOkConnectToDevice();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	protected boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		if (itemId == R.id.actBTRefresh) {
			this.doClickActionRefresh(item);
		} else if(itemId == R.id.actBTPowerOn) {
			this.doClickActionPowerOn(item);
		} else if(itemId == R.id.actBTConnectToDevice){
			this.doClickActionConnectToDevice();
		}
		
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String sbMac = this.objDevices.get(position).getAddress();
		String sbName = this.objDevices.get(position).getName();
		
		Log.d(TAG, "Return-> Mac:" + sbMac + ", Name:" + sbName);
		
		Intent iClose = new Intent();
		iClose.putExtra(EXTRA_ADDRESS, sbMac);
		iClose.putExtra(EXTRA_DEVICE_NAME, sbName);
		this.objLinkBTActivity.setResult(Activity.RESULT_OK, iClose);
		this.objLinkBTActivity.finish();		
	}
	
	private void onResultOkBluetoothOn(){
		this.getDevices(null);
	}
	
	private void onResultCancelBluetoothOn(){
		this.objLinkBTAdapter.objItems.clear();
		this.objLinkBTAdapter.notifyDataSetChanged();
	}
	
	private void onResultOkConnectToDevice(){
		this.getDevices(null);
	}
	
	private void doClickActionRefresh(MenuItem item){
		try {
			this.getDevices(item);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	private void doClickActionPowerOn(MenuItem item){
		try {
			Intent iBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			this.objLinkBTActivity.startActivityForResult(iBluetoothOn, REQUEST_BLUETOOTH_TURN_ON);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	private void doClickActionConnectToDevice(){
		try {
			if(this.hasBluetooth()){
				Intent iBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
				this.objLinkBTActivity.startActivityForResult(iBluetooth, REQUEST_PAIR_BLUETOOTH_DEVICE);
			}
			else{
				Toast.makeText(this.objLinkBTActivity, this.objLinkBTActivity.getString(R.string.bluetooth_no_disponible), Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	private void getDevices(MenuItem item){
		try {
			this.objProgress.setMessage(this.objLinkBTActivity.getString(R.string.loading_devices));
			this.objProgress.show();
			
			if(this.hasBluetooth()){
				this.objDevices.clear();
				
				BluetoothAdapter objBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				Set<BluetoothDevice> objBluetoothDevices = objBluetoothAdapter.getBondedDevices();
				
				if(objBluetoothDevices.size() > 0){
					for(BluetoothDevice objDevice : objBluetoothDevices){
						this.objDevices.add(new com.playtechla.bluetoothlink.BluetoothDevice(objDevice.getName(), objDevice.getAddress()));
					}
				}
				
				this.objLinkBTAdapter.objItems = this.objDevices;
				this.objLinkBTAdapter.notifyDataSetChanged();
				
				if(item != null){
					item.setEnabled(true);
				}
				
				if(this.objDevices.size() == 0){
					Toast.makeText(this.objLinkBTActivity, this.objLinkBTActivity.getString(R.string.no_hay_disp_bluetooth), Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(this.objLinkBTActivity, this.objLinkBTActivity.getString(R.string.bluetooth_no_disponible), Toast.LENGTH_SHORT).show();
			}
			
			this.objProgress.dismiss();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	private boolean hasBluetooth(){
		try {
			BluetoothAdapter objBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			return objBluetoothAdapter != null;
		} catch (Exception e) {
			return false;
		}
	}
}