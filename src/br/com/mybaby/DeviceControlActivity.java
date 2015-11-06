/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.mybaby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import br.com.mybaby.alarme.SampleAlarmReceiver;
import br.com.mybaby.dao.SistemaDAO;
import br.com.mybaby.dialogo.DesconexaoDialogo;
import br.com.mybaby.dialogo.Dialogo;
import br.com.mybaby.preferences.PreferencesActivity;
import br.com.mybaby.sms.SMSDialogo;
import br.com.mybaby.util.Constantes;
import br.com.mybaby.util.Util;
import br.com.mybaby.util.VisibilidadeManager;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final long TIMEOUT_DIALOGO = 1000*30; //1000 = 1 SEGUNDO

    private ImageView imagemStatus;
    private TextView mConnectionExplain;
    private TextView deviceAddress;
    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private boolean isDesconexaoIntencional = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private SistemaDAO sistemaDAO;
    private SharedPreferences sharedPref;
    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
    
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            	mConnected = false;
            	imagemStatus.setImageResource(R.drawable.aguardando);
                updateConnectionState(R.string.aguardando);

                invalidateOptionsMenu();
                
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action) || BluetoothLeService.ACTION_GATT_DISCONNECTED_DIALOGO.equals(action)) {
            	
            	mConnected = false;
            	imagemStatus.setImageResource(R.drawable.aguardando);
                updateConnectionState(R.string.aguardando);
                
                invalidateOptionsMenu();

                if(BluetoothLeService.ACTION_GATT_DISCONNECTED_DIALOGO.equals(action)){
                	mostrarDialogo();
                }
                
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            	mConnected = true;
            	updateConnectionState(R.string.conectado);
            	imagemStatus.setImageResource(R.drawable.conectado);
            	
            	invalidateOptionsMenu();
            	
            	// Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            } else if(BluetoothLeService.ACTION_GATT_DISCONNECTED_INTENCIONAL.equals(action)){
            	mConnected = false;
            	updateConnectionState(R.string.desconectado);
            	imagemStatus.setImageResource(R.drawable.chorando);
            	
            	invalidateOptionsMenu();
            }
        }
    };
      
    public void onOkClickDialogo(){
    	
    	imagemStatus.setImageResource(R.drawable.aguardando);
        updateConnectionState(R.string.aguardando);
    	
        invalidateOptionsMenu();
        
    	//MUDA PARA FALSE A VARIAVEL DE ENVIO DE ALERTAS
    	Log.d(TAG, "UPDATE > DeviceControlManager > onOkClickDialogo");
    	sistemaDAO.update(Constantes.KEEP_ENVIO_ALERTA, Boolean.FALSE.toString());
    	

    	//MANDA O APP PARA BACKGROUND
//    	Intent intentGoBackground = new Intent();
//    	intentGoBackground.setAction(Intent.ACTION_MAIN);
//    	intentGoBackground.addCategory(Intent.CATEGORY_HOME);
//    	this.startActivity(intentGoBackground);
    	
    }
    
    public void onOkClickDialogoSMS(){
    	
    	//LOGA O CLIQUE NO DIALOGO
    	
    	//VOLTA A BOOLEANA  PARA FALSE
    	sistemaDAO.update(Constantes.SMS_ENVIADO, Boolean.FALSE.toString());
    	sistemaDAO.update(Constantes.SMS_ENTREGUE, Boolean.FALSE.toString());
    }
    
    private void mostrarDialogo(){
    	final DialogFragment newFragment = new Dialogo();
        newFragment.show(DeviceControlActivity.this.getFragmentManager(), Dialogo.EXTRAS_DIALOGO);
        
        
//        final Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            public void run() {
//                newFragment.getDialog().dismiss(); // when the task active then close the dialog
//                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//            }
//        }, TIMEOUT_DIALOGO_DOIS_MINUTOS);
    }
    
    private void mostrarDialogoSMS(){
    	final DialogFragment newFragment = new SMSDialogo();
        newFragment.show(DeviceControlActivity.this.getFragmentManager(), SMSDialogo.EXTRAS_DIALOGO_SMS);
    }
    
    private void mostrarDialogoDesconexao(){
    	final DialogFragment newFragment = new DesconexaoDialogo();
        newFragment.show(DeviceControlActivity.this.getFragmentManager(), DesconexaoDialogo.EXTRAS_DESCONEXAO_DIALOGO);
    }
    
    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
    };

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 if (BuildConfig.DEBUG) {
             // Enable strict mode checks when in debug modes
             Util.enableStrictMode();
         }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        
        sistemaDAO = new SistemaDAO(this);
        
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        final Intent intent = getIntent();
        
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        String dispositivoNomeBase = sistemaDAO.getValor(Constantes.DISPOSITIVO_NOME);
        String dispositivoEnderecoBase = sistemaDAO.getValor(Constantes.DISPOSITIVO_ENDERECO);
        
        imagemStatus = (ImageView) findViewById(R.id.imagemStatus);
        deviceAddress = (TextView) findViewById(R.id.device_address);
        deviceAddress.setText(mDeviceAddress);
        //mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        //mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mConnectionExplain = (TextView) findViewById(R.id.connection_explain);
        //mDataField = (TextView) findViewById(R.id.data_value);

        if(mDeviceName != null && mDeviceAddress != null ){
        	//SALVO O NOME E ENDERECO DO DISPOSITIVO PARA TENTAR CONECTAR AUTOMATICO
        	//SALVA O NOME DO DISPOSITIVO
        	if(dispositivoNomeBase != null && !dispositivoNomeBase.equals(mDeviceName)){
        		sistemaDAO.update(Constantes.DISPOSITIVO_NOME, mDeviceName);
        	}else if (dispositivoNomeBase == null) {
        		sistemaDAO.inserir(Constantes.DISPOSITIVO_NOME, mDeviceName);
        	}
        	
        	//SALVA OS DADOS DO ENDERECO DO DISPOSITIVO
        	if(dispositivoEnderecoBase != null && !dispositivoEnderecoBase.equals(mDeviceAddress)){
        		sistemaDAO.update(Constantes.DISPOSITIVO_ENDERECO, mDeviceAddress);
        	}else if (dispositivoEnderecoBase == null){
        		sistemaDAO.inserir(Constantes.DISPOSITIVO_ENDERECO, mDeviceAddress);
        	}
        	
        } else {
        	mDeviceName = dispositivoNomeBase;
        	mDeviceAddress = dispositivoEnderecoBase;
        }
        
        if(sharedPref.getString(Constantes.NOME_MYBABY, "")==""){
        	getActionBar().setTitle("MyBaby!");
        } else{
        	getActionBar().setTitle(sharedPref.getString(Constantes.NOME_MYBABY, ""));
        }
        
        getActionBar().setDisplayHomeAsUpEnabled(false);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        
    }

	private void onOkClick(){
    	//MUDA PARA FALSE A VARIAVEL DE ENVIO DE NOTIFICAÇÕES
		Log.d(TAG, "UPDATE > DeviceControlManager > onOkClick");
    	sistemaDAO.update(Constantes.KEEP_ENVIO_ALERTA, Boolean.FALSE.toString());
    	
    	updateConnectionState(R.string.aguardando);
    	imagemStatus.setImageResource(R.drawable.aguardando);
    	
    	invalidateOptionsMenu();
    }
    
    private boolean isNotificacaoAtivo(){
    	return Boolean.parseBoolean(sistemaDAO.getValor(Constantes.NOTIFICACAO_ATIVO));
    }
    
    private boolean isSMSEnviado(){
    	return Boolean.parseBoolean(sistemaDAO.getValor(Constantes.SMS_ENTREGUE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        if(isNotificacaoAtivo() && !isDesconexaoIntencional() && !isSMSEnviado()){
        	onOkClickDialogo();
        }
        
        if(isSMSEnviado()){
        	mostrarDialogoSMS();
        }
        
        VisibilidadeManager.setMainActivityVisible(Boolean.TRUE);
        
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null  && !isDesconexaoIntencional) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
        
        invalidateOptionsMenu();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);

    	//ALARME
    	if(intent.getStringExtra(Constantes.ALARME_EXTRA_VAMOS_ACORDAR)!=null){
    		setDesconexaoIntencional(Boolean.FALSE);
    		mBluetoothLeService.connect(mDeviceAddress);

    		imagemStatus.setImageResource(R.drawable.aguardando);
    		updateConnectionState(R.string.aguardando);
    	}
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        VisibilidadeManager.setMainActivityVisible(Boolean.FALSE);
        
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        sistemaDAO.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else if(!mConnected && !isDesconexaoIntencional()){
        	menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
            	setDesconexaoIntencional(Boolean.FALSE);
                mBluetoothLeService.connect(mDeviceAddress);
                
                imagemStatus.setImageResource(R.drawable.aguardando);
                updateConnectionState(R.string.aguardando);
                
                invalidateOptionsMenu();
                
                alarm.cancelAlarm(this);
                
                return true;
            case R.id.menu_disconnect:
            	setDesconexaoIntencional(Boolean.TRUE);
            	mostrarDialogoDesconexao();
                
                return true;
            case android.R.id.home:
            	//final Intent intent = new Intent(this, DeviceScanActivity.class);
            	//startActivity(intent);
            	//onBackPressed();
                return true;
            case R.id.menu_configuracao:
            	final Intent intent = new Intent(this, PreferencesActivity.class);
            	startActivity(intent);
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void desconectar(String tempo){
      mBluetoothLeService.disconnect();
      
      alarm.setAlarm(this, Constantes.ALARME_CONNECT, tempo);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	deviceAddress.setText(mDeviceAddress);
            	mConnectionState.setText(resourceId);
            	if(R.string.conectado == resourceId){
            		mConnectionExplain.setText(R.string.conectado_explain);
            	} else if(R.string.aguardando == resourceId){
            		mConnectionExplain.setText(R.string.aguardando_explain);
            	} else if(R.string.desconectado == resourceId){
            		mConnectionExplain.setText(R.string.desconectado_explain);
            	}
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED_DIALOGO);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED_INTENCIONAL);
        
        return intentFilter;
    }

	public boolean isDesconexaoIntencional() {
		return isDesconexaoIntencional;
	}

	public void setDesconexaoIntencional(boolean isDesconexaoIntencional) {
		this.isDesconexaoIntencional = isDesconexaoIntencional;
	}
}
