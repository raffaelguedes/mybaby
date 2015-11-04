package br.com.mybaby.alarme;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import br.com.mybaby.email.EmailService;
import br.com.mybaby.notificacao.Notificacao;
import br.com.mybaby.sms.SMS;


/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService {
    public SampleSchedulingService() {
        super("SchedulingService");
        
        notificacao = new Notificacao(this);
        sms = new SMS(this);
    }
    
    private final static String TAG = SampleSchedulingService.class.getSimpleName();
    
    private Notificacao notificacao;
    private SMS sms;
    
    @Override
    protected void onHandleIntent(Intent intent) {
    	
    	PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    	WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
    	wakeLock.acquire();
    	
    	//O PROCESSO DE NOTIFICA��O AT� O SMS ESTA LEVANDO EM M�DIA 6 MINUTOS
    	//ENVIA AS NOTIFICA��ES
    	if(!notificacao.enviarNotificacao()){
    		//AGORA FUDEU...N�O TEVE RESPOSTA EM NENHUMA DAS NOTIFICA��ES
    		//TENTATIVA DE RECONECTAR AO DISPOSITIVO
    		Log.i(TAG, "Sem resposta aos envios de Notifica��o.");
    		Log.i(TAG, "Os SMS ser�o enviados.");

    		if(!sms.enviarSMS()){
    			//AGORA FUDEU EM DOBRO...N�O TEVE RESPOSTA EM NENHUMA DAS NOTIFICA��ES E DOS SMS�S
    			Log.i(TAG, "Sem resposta aos envios de SMS.");
    			
    			//ENVIANDO OS EMAILS
    	    	if(isOnline()){
    	    		EmailService emailService = new EmailService("raffa3lgu3d3s@gmail.com", "HardCore1983", this);
    	    		try {
    	    			emailService.sendMail();
    	    		} catch (Exception e) {
    	    			Log.i(TAG, "Erro ao enviar email." + e);
    	    		}
    	    	}
    		}
    	}
        
		wakeLock.release();
        // Release the wake lock provided by the BroadcastReceiver.
        SampleAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }
    
    public boolean isOnline() {
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo netInfo = cm.getActiveNetworkInfo();
    	return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
