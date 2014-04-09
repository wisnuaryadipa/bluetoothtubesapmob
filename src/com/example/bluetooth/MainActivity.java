package com.example.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;
	 
    ListView listDeviceBT;
    Button btnScan;
    TextView statusAktifBT;
    BluetoothAdapter BTadapter;
    ArrayAdapter<String> btArrayAdapter;
 
 /** deklarasi fungsi xml */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnScan = (Button)findViewById(R.id.scandevice);
        
        statusAktifBT = (TextView)findViewById(R.id.statusBT);
        BTadapter = BluetoothAdapter.getDefaultAdapter();
        
        listDeviceBT = (ListView)findViewById(R.id.BTaktif);
        btArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        listDeviceBT.setAdapter(btArrayAdapter);
        
        CheckBlueToothState();
        
        btnScan.setOnClickListener(btnScanDeviceOnClickListener);

        registerReceiver(ActionFoundReceiver, 
          new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }
    
    /** Cek Bluetooth aktif apa enggak, Kalau enggak muncul window requestenable bluetooth */
 private void CheckBlueToothState(){
     if (BTadapter == null){
         statusAktifBT.setText("Bluetooth NOT support");
        }else{
         if (BTadapter.isEnabled()){
          if(BTadapter.isDiscovering()){
           statusAktifBT.setText("Bluetooth is currently in device discovery process.");
          }else{
           statusAktifBT.setText("Bluetooth is Enabled.");
           btnScan.setEnabled(true);
          }
         }else{
          statusAktifBT.setText("Bluetooth is NOT Enabled!");
          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
         }
        }
    }
    
    private Button.OnClickListener btnScanDeviceOnClickListener
    = new Button.OnClickListener(){

  @Override
  public void onClick(View arg0) {
   // TODO Auto-generated method stub
   btArrayAdapter.clear();
   BTadapter.startDiscovery();
  }};

 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  // TODO Auto-generated method stub
  if(requestCode == REQUEST_ENABLE_BT){
   CheckBlueToothState();
  }
 }
    
 private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

  @Override
  public void onReceive(Context context, Intent intent) {
   // TODO Auto-generated method stub
   String action = intent.getAction();
   if(BluetoothDevice.ACTION_FOUND.equals(action)) {
             BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
             btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
             btArrayAdapter.notifyDataSetChanged();
         }
  }};
    
}
