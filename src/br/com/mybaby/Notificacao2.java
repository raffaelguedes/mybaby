package br.com.mybaby;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import br.com.mybaby.dao.SistemaDAO;

public class Notificacao2 {
	private Context context;
	private SistemaDAO sistemaDAO;
	private NotificationCompat.Builder builder;
	private boolean houveRespostaDaNotificacaoEnviada;
	
	private final long DOIS_MINUTOS = 60000*2;
	private final long TRINTA_SEGUNDOS = 1000*30;
	private final long QUINZE_SEGUNDOS = 1000*15;
	private final long CINCO_SEGUNDOS = 1000*5;
	private final int MAX_ENVIO_QUATRO = 4;
	private final int MAX_ENVIO_DOIS = 2;
	private final int MAX_ENVIO_OITO = 8;
	
	public Notificacao2(Context context) {
		this.context = context;
		sistemaDAO = new SistemaDAO(context);
	}
	
	public void cancelarNotificacao(int idNotificacao){
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(idNotificacao);

	}

	public boolean enviarNotificacao(){
		builder = new NotificationCompat.Builder(context)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setSmallIcon(R.drawable.mybaby)
			.setContentTitle(context.getString(R.string.notificacao))
			.setContentText(context.getString(R.string.notificacao_mensagem))
			.setDefaults(Notification.DEFAULT_ALL);// requires VIBRATE permission
		
			// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, NotificacaoActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(NotificacaoActivity.class);
		
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		
		startTimer();
		
		return houveRespostaDaNotificacaoEnviada;
	}
	
	// Starts the timer according to the number of seconds the user specified.
	private void startTimer() {
		Log.d(Constantes.DEBUG_TAG, "Inicio timer notificação");
		
		//ALTERA O VALOR NA BASE PARA TRUE(NOTIFICACAO ATIVA)
		sistemaDAO.update(Constantes.NOTIFICACAO_ATIVO, Boolean.TRUE.toString());
		try {
			int count = 1;
			houveRespostaDaNotificacaoEnviada = true;
			while (isContinuarEnviando() && count <= MAX_ENVIO_OITO) {
				issueNotification(builder);
				
				if(count == (MAX_ENVIO_OITO/2)){
					Thread.sleep(DOIS_MINUTOS);
				}else{
					Thread.sleep(TRINTA_SEGUNDOS);
				}
				count ++;
			}
			
			if(isContinuarEnviando() || count >= MAX_ENVIO_OITO){
				houveRespostaDaNotificacaoEnviada = false;
				//ESGOTOU OS ENVIOS E NÃO TEVE RESPOSTA
				//RETORNAR....
				
			}

		} catch (InterruptedException e) {
			Log.d(Constantes.DEBUG_TAG, "Erro no timer de notificação");
		}

		//ALTERA STATUS NOTIFICAÇÃO ENVIO PARA TRUE
		sistemaDAO.update(Constantes.NOTIFICACAO_ENVIO, Boolean.TRUE.toString());
		Log.d(Constantes.DEBUG_TAG, "Finalizaou o timer de notificação");
	}
	
	private void issueNotification(NotificationCompat.Builder builder) {
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(Constantes.NOTIFICATION_ID, builder.build());
	}
	
	private boolean isContinuarEnviando(){
		return Boolean.valueOf(sistemaDAO.getValor(Constantes.NOTIFICACAO_ENVIO));
	}
}
