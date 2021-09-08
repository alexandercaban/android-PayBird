package com.playtechla.bluetoothlink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class LinkBluetoothDeviceActivity extends Activity {
	public static final String TAG = "LinkBluetoothDeviceActivity";
	
	private LinkBluetoothDeviceHandler objLinkBTHandler;
	public ListView lstDevices;
	private FloatingActionButton btnExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main_link);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		this.prepareView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.link_bluetooth, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return this.objLinkBTHandler.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.objLinkBTHandler.onActivityResult(requestCode, resultCode, data);
	}
	
	private void prepareView(){
		try {
			this.lstDevices = (ListView) this.findViewById(R.id.lstBTDevices);
			this.btnExit = this.findViewById(R.id.btnExit);
			
			this.objLinkBTHandler = new LinkBluetoothDeviceHandler(this);
			this.objLinkBTHandler.init();
			this.btnExit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					finish();
				}
			});
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}
