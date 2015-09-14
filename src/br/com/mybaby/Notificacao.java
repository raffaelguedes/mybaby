package br.com.mybaby;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.mybaby.dao.SistemaDAO;


public class Notificacao extends IntentService {
	private NotificationManager mNotificationManager;
	private String mMessage;
	NotificationCompat.Builder builder;
	SistemaDAO sistemaDAO = new SistemaDAO(this);

	public Notificacao() {
		// The super call is required. The background thread that IntentService
		// starts is labeled with the string argument you pass.
		super("br.com.mybaby");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		String action = intent.getAction();

		if(action.equals(Constantes.NOTIFICACAO_ACTION)) {
			issueNotification(intent, mMessage);
		}else if(action.equals(Constantes.NOTIFICACAO_ACTION_OK)){
			
			sistemaDAO.update(Constantes.NOTIFICACAO_ENVIO, Boolean.TRUE.toString());
			
		} else if (action.equals(Constantes.NOTIFICACAO_CANCEL)){
			
			mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			mNotificationManager.cancel(Constantes.NOTIFICATION_ID);
			
			sistemaDAO.update(Constantes.NOTIFICACAO_ATIVO, Boolean.FALSE.toString());
			
		}
	}

	private void issueNotification(Intent intent, String msg) {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		Intent intentOk = new Intent(this, Notificacao.class);
    	intentOk.setAction(Constantes.NOTIFICACAO_ACTION_OK);
    	PendingIntent pendingIntent = PendingIntent.getService(this, 0, intentOk, 0);

    	 builder = new NotificationCompat.Builder(this)
    	.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    	.setSmallIcon(R.drawable.mybaby)
    	.setContentTitle(getString(R.string.notificacao))
    	.setContentText(getString(R.string.notificacao_mensagem))
    	.setDefaults(Notification.DEFAULT_ALL)// requires VIBRATE permission
    	.addAction (R.drawable.mybaby, "OK", pendingIntent);
    	 
    	 /*
          * Clicking the notification itself displays ResultActivity, which provides
          * UI for snoozing or dismissing the notification.
          * This is available through either the normal view or big view.
          */
          //Intent resultIntent = new Intent(this, DeviceControlActivity.class);
          //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

          // Because clicking the notification opens a new ("special") activity, there's
          // no need to create an artificial back stack.
          //PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

          //builder.setContentIntent(resultPendingIntent);
    	
		startTimer();
	}
	
	public void enviarMensagem(){
		issueNotification(null, null);
	}

	private void issueNotification(NotificationCompat.Builder builder) {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Including the notification ID allows you to update the notification later on.
		mNotificationManager.notify(Constantes.NOTIFICATION_ID, builder.build());
	}
	
	private boolean isContinuarEnviando(){
		return Boolean.valueOf(sistemaDAO.getValor(Constantes.NOTIFICACAO_ENVIO));
	}
	
	public boolean isNotificacaoAtivo(){
		return Boolean.valueOf(sistemaDAO.getValor(Constantes.NOTIFICACAO_ATIVO));
	}
	
	// Starts the timer according to the number of seconds the user specified.
	private void startTimer() {
		Log.d(Constantes.DEBUG_TAG, "Inicio timer notificação");
		//ALTERA O VALOR NA BASE PARA TRUE(NOTIFICACAO ATIVA)
		sistemaDAO.update(Constantes.NOTIFICACAO_ATIVO, Boolean.TRUE.toString());
		try {
			int count = 0;
	    	while (isContinuarEnviando() || count <= 4) {
				issueNotification(builder);
				Thread.sleep(1000*30);
			}

		} catch (InterruptedException e) {
			Log.d(Constantes.DEBUG_TAG, "Erro no timer de notificação");
		}
		
		//ALTERA STATUS NOTIFICAÇÃO ENVIO PARA TRUE
		sistemaDAO.update(Constantes.NOTIFICACAO_ENVIO, Boolean.TRUE.toString());
		Log.d(Constantes.DEBUG_TAG, "Finalizaou o timer de notificação");
	}
}
