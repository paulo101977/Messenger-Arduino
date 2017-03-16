package br.com.galdino.paulo.messengerarduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter baAdapter;
    private ListView bListView;
    private Toast toast;
    private final int REQUEST_ENABLE_BT = 9876; //the request to bluetooth
    private final String TAG = this.getClass().getCanonicalName();//tag for log messages

    //register the android receiver to broadcast devices
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView

                String name = device.getName();
                String mac = device.getAddress();
                //Log.d(TAG,name);
                updateAdapter(
                    name + ":" + mac
                );


            }
        }
    };

    //the list input
    //private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private ArrayList<String> deviceNames;
    private DeviceNameBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //render the current layout
        setContentView(R.layout.activity_main);

        //retrieve the list view component from the current layout
        bListView = (ListView)findViewById(R.id.bluetoothList);

        //the device names list
        deviceNames = new ArrayList<>();


        //start new adapter
        adapter = new DeviceNameBaseAdapter(this,deviceNames);

        //set the adapter
        bListView.setAdapter(adapter);

        //register the receiver
        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //registerReceiver(mReceiver, filter);


        //retrieve the respective adapter to connect other devices
        baAdapter = BluetoothAdapter.getDefaultAdapter();
        //baAdapter.startDiscovery();
        retrieveBlueToothList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //register the receiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        //if(deviceNames != null) deviceNames.clear();
    }

    @Override
    protected void onDestroy() {

        //unregister the receiver
        if(mReceiver != null) unregisterReceiver(mReceiver);

        mReceiver = null;

        super.onDestroy();
    }



    private void retrieveBlueToothList(){
        if (baAdapter == null) {
            // Device does not support Bluetooth
            showMessage("Bluetooth error, dont compatible device");
        }

        //fill the list with the bluetooth
        else {
            showMessage("Bluetooth found!");

            //try enable bluetooth
            if (!baAdapter.isEnabled()) {
                Log.d(TAG,"Bluetooth is not enabled");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                return;
            }


            //send message to operational system and receive info into Broadcast
            //deviceNames.clear(); //clear list
            if(!baAdapter.startDiscovery()){
                showMessage("Bluetooth error, dont could discovery devices!");
            }

        }
    }

    /* show message to user */
    private void showMessage(String text) {
        Toast
            .makeText(this, text, Toast.LENGTH_LONG)
            .show();
    }


    /*
        UPDATE the adapter and refresh the list view
     */
    private void updateAdapter(String deviceName){
        //update the base list internal data
        if(!adapter.getData().contains(deviceName)) {
            adapter.getData().add(deviceName);

            // notify the change to list view
            adapter.notifyDataSetChanged();
        }
    }

    /* after send request to active the bluetooh */
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options){

        //check the request origin
        if(REQUEST_ENABLE_BT == requestCode){
            Log.e(TAG,"The request return ok");

            //send message to operational system and receive info into Broadcast
            //this retrieve all device with bluetooth
            deviceNames.clear(); //clear data
            if(!baAdapter.startDiscovery()){
                showMessage("Bluetooth error, dont could discovery devices!");
            }
        }
    }


}
