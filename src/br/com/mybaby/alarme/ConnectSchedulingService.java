package br.com.mybaby.alarme;

import android.app.IntentService;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import br.com.mybaby.DeviceControlActivity;
import br.com.mybaby.util.Constantes;

public class ConnectSchedulingService extends IntentService{

	public ConnectSchedulingService() {
		super("ConnectSchedulingService");

	}
	
	@Override
	protected void onHandleIntent(Intent intent) {		 
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    	WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
    	wakeLock.acquire();
		
    	final Intent newIntent = new Intent(this, DeviceControlActivity.class);
    	newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	newIntent.putExtra(Constantes.ALARME_EXTRA_VAMOS_ACORDAR, Constantes.ALARME_EXTRA_VAMOS_ACORDAR);
    	startActivity(newIntent);
        
        wakeLock.release();
        SampleAlarmReceiver.completeWakefulIntent(intent);

	}

}
