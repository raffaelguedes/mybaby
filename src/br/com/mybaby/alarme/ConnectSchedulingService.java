package br.com.mybaby.alarme;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import br.com.mybaby.BluetoothLeService;
import br.com.mybaby.BluetoothLeService.LocalBinder;
import br.com.mybaby.DeviceControlActivity;
import br.com.mybaby.dao.SistemaDAO;
import br.com.mybaby.util.Constantes;

public class ConnectSchedulingService extends IntentService{

	private BluetoothLeService bluetoothLeService;
	private SistemaDAO sistemaDAO;
	
	public ConnectSchedulingService() {
		super("ConnectSchedulingService");
		
		
		sistemaDAO = new SistemaDAO(this);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {		 
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    	WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
    	wakeLock.acquire();
		
//		Intent newIntent = new Intent(this, BluetoothLeService.class);
//        bindService(newIntent, mServiceConnection, BIND_AUTO_CREATE);
    	
    	final Intent newIntent = new Intent(this, DeviceControlActivity.class);
    	newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	newIntent.putExtra(Constantes.ALARME_EXTRA_VAMOS_ACORDAR, Constantes.ALARME_EXTRA_VAMOS_ACORDAR);
    	startActivity(newIntent);
        
        wakeLock.release();
        SampleAlarmReceiver.completeWakefulIntent(intent);

	}
	
	// Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
        	LocalBinder binder = (LocalBinder) service;
            bluetoothLeService = binder.getService();
            
    		if(bluetoothLeService.initialize()){
    			bluetoothLeService.connect(sistemaDAO.getValor(Constantes.DISPOSITIVO_ENDERECO));
    		}

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        	bluetoothLeService = null;
        }
    };

	
}
