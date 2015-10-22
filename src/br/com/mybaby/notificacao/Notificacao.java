package br.com.mybaby.notificacao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.mybaby.DeviceControlActivity;
import br.com.mybaby.R;
import br.com.mybaby.R.drawable;
import br.com.mybaby.R.string;
import br.com.mybaby.dao.SistemaDAO;
import br.com.mybaby.util.Constantes;
import br.com.mybaby.util.Util;

public class Notificacao {
	private final static String TAG = Notificacao.class.getSimpleName();
	private Context context;
	private SistemaDAO sistemaDAO;
	private NotificationCompat.Builder builder;
	private boolean houveRespostaDaNotificacaoEnviada;
	
	private final long DOIS_MINUTOS = 60000*2;
	private final long HUM_MINUTO = 60000;
	private final long TRINTA_SEGUNDOS = 1000*30;
	private final long QUINZE_SEGUNDOS = 1000*15;
	private final long CINCO_SEGUNDOS = 1000*5;
	private final int MAX_ENVIO = 8;
	
	private String[] mensagens = {
			"Alerta do MyBaby! Tudo Certo!?",
		    "Tirou o baby! Ufa!",
		    "Hello. Tem alguém aí!!!",
		    "Alerta do MyBaby! Tudo Certo!?",
		    "Será que estou sozinho aqui!?",
		    "Alerta do MyBaby! Tudo Certo!?",
		    "Estou preocupado!!! Ei!!!",
		    "Buááááá!!! Enviando SMS!!!"
	};
	
	public Notificacao(Context context) {
		this.context = context;
		sistemaDAO = new SistemaDAO(context);
	}
	
	public void cancelarNotificacao(int idNotificacao){
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(idNotificacao);
		
		Log.d(TAG, "UPDATE > Notificacao > cancelarNotificacao");
		sistemaDAO.update(Constantes.NOTIFICACAO_ATIVO, Boolean.FALSE.toString());

	}

	public boolean enviarNotificacao(){
		
		builder = new NotificationCompat.Builder(context)
		.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
		.setSmallIcon(R.drawable.mybaby)
		.setContentTitle(context.getString(R.string.notificacao))
		.setDefaults(Notification.DEFAULT_ALL);// requires VIBRATE permission
		
		// Creates an Intent for the Activity
		Intent notifyIntent = new Intent(context, DeviceControlActivity.class);
		
		notifyIntent.putExtra(Constantes.NOTIFICACAO_ACTION_OK, Constantes.NOTIFICACAO_ACTION_OK);
		
		// Sets the Activity to start in a new, empty task
		//notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Creates the PendingIntent
		PendingIntent notifyPendingIntent =
		        PendingIntent.getActivity(
		        context,
		        0,
		        notifyIntent,
		        PendingIntent.FLAG_UPDATE_CURRENT
		);
		
		// Puts the PendingIntent into the notification builder
		builder.setContentIntent(notifyPendingIntent);
		
		startTimer();
		
		return houveRespostaDaNotificacaoEnviada;
	}
	
	// Starts the timer according to the number of seconds the user specified.
	private void startTimer() {
		Log.d(TAG, "Inicio timer notificação");
		
		//ALTERA O VALOR NA BASE PARA TRUE(NOTIFICACAO ATIVA)
		Log.d(TAG, "UPDATE > Notificacao > startTimer");
		sistemaDAO.update(Constantes.NOTIFICACAO_ATIVO, Boolean.TRUE.toString());
		
		try {
			int count = 1;
			houveRespostaDaNotificacaoEnviada = true;
			while (isContinuarEnviando() && count <= MAX_ENVIO && !isDispositivoConectadoSincronizado()) {
				builder.setContentText(mensagens[count-1]);
				issueNotification(builder);
				Log.d(TAG, "Notificação de número: " + count +" | "+ Util.getDataAtual());
				
				if(count == (MAX_ENVIO/2)){
					Thread.sleep(HUM_MINUTO);
				}else{
					Thread.sleep(TRINTA_SEGUNDOS);
				}
				count ++;
			}
			
			if((isContinuarEnviando() || count >= MAX_ENVIO) && !isDispositivoConectadoSincronizado()){
				//ESGOTOU OS ENVIOS E NÃO TEVE RESPOSTA
				houveRespostaDaNotificacaoEnviada = false;
				
			}
			
			if(!isContinuarEnviando() || isDispositivoConectadoSincronizado()){
				cancelarNotificacao(Constantes.NOTIFICATION_ID);
				
			}

		} catch (InterruptedException e) {
			Log.d(TAG, "Erro no timer de notificação");
		}

		Log.d(TAG, "Finalizaou o timer de notificação");
	}
	
	private void issueNotification(NotificationCompat.Builder builder) {
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(Constantes.NOTIFICATION_ID, builder.build());
	}
	
	private boolean isContinuarEnviando(){
		return Boolean.valueOf(sistemaDAO.getValor(Constantes.KEEP_ENVIO_ALERTA));
	}
	
	private boolean isDispositivoConectadoSincronizado(){
		return Boolean.valueOf(sistemaDAO.getValor(Constantes.DISPOSITIVO_CONECTADO_SINCRONIZADO));
	}
}
