package br.com.mybaby.alarme;

import br.com.mybaby.email.EmailService;
import br.com.mybaby.notificacao.Notificacao;
import br.com.mybaby.sms.SMS;
import android.app.IntentService;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;


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
    	
    	EmailService emailService = new EmailService("raffa3lgu3d3s@gmail.com", "HardCore1983");
    	try {
			emailService.sendMail("Subject", "Body", "raffa3lgu3d3s@gmail.com", "raffa3lgu3d3s@gmail.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//O PROCESSO DE NOTIFICAÇÃO ATÉ O SMS ESTA LEVANDO EM MÉDIA 6 MINUTOS
    	//ENVIA AS NOTIFICAÇÕES
    	if(!notificacao.enviarNotificacao()){
    		//AGORA FUDEU...NÃO TEVE RESPOSTA EM NENHUMA DAS NOTIFICAÇÕES
    		//TENTATIVA DE RECONECTAR AO DISPOSITIVO
    		Log.i(TAG, "Sem resposta aos envios de Notificação.");
    		Log.i(TAG, "Os SMS serão enviados.");

    		if(!sms.enviarSMS()){
    			//AGORA FUDEU EM DOBRO...NÃO TEVE RESPOSTA EM NENHUMA DAS NOTIFICAÇÕES E DOS SMS´S
    			Log.i(TAG, "Sem resposta aos envios de SMS.");
    		}
    	}
        
		wakeLock.release();
        // Release the wake lock provided by the BroadcastReceiver.
        SampleAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }
}
