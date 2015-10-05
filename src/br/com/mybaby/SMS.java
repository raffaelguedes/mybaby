package br.com.mybaby;

import java.util.ArrayList;
import java.util.List;

import br.com.mybaby.dao.SistemaDAO;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SMS {
	private final static String TAG = SMS.class.getSimpleName();
	
	private Context context;
	private List<String> numeros;
	private String mensagem;
	private SistemaDAO sistemaDAO;
	private boolean houveRespostaDoSMSEnviado;
	
	PendingIntent pendingIntentEnviado;
	PendingIntent pendingIntentEntregue;
	SmsManager smsManager;
	
	
	private final long DOIS_MINUTOS = 60000*2;
	private final long HUM_MINUTO = 60000;
	private final long TRINTA_SEGUNDOS = 1000*30;
	private final long QUINZE_SEGUNDOS = 1000*15;
	private final long CINCO_SEGUNDOS = 1000*5;
	private final int MAX_ENVIO = 8;

	public SMS(Context context){
		this.context = context;
		sistemaDAO = new SistemaDAO(context);
		
	}
	
	public boolean enviarSMS(){
		//LOGAR
		
		//SETAR NA BASE QUE O SMS FOI ENVIADO
		//QUANDO O APP ABRIR NOVAMENTE INFORMAR Q O SMS FOI ENVIADO. HORARIO E TUDO MAIS
		
		//buscar lista de numeros
		numeros = new ArrayList<String>();
		numeros.add("5511982465760");
		numeros.add("5511982301919");
		
		//buscar a mensagem
		mensagem = "MyBaby! Mensagem automática de emergência! Entre em contato!";
		
		smsManager = SmsManager.getDefault();
		pendingIntentEnviado = PendingIntent.getBroadcast(context, 0, new Intent(Constantes.SMS_ENVIADO), 0);
		pendingIntentEntregue = PendingIntent.getBroadcast(context, 0, new Intent(Constantes.SMS_ENTREGUE), 0);
		
		startTimer();
		
		return houveRespostaDoSMSEnviado;
	}
	
	// Starts the timer according to the number of seconds the user specified.
		private void startTimer() {
			Log.d(Constantes.DEBUG_TAG, "Inicio timer SMS");
			
			try {
				int count = 1;
				houveRespostaDoSMSEnviado = true;
				while (isContinuarEnviando() && count <= MAX_ENVIO && !isDispositivoConectadoSincronizado()) {

					for(String numero : numeros){
						Log.d(TAG, "Enviando SMS para o número: " + numero);
						smsManager.sendTextMessage(numero, null, mensagem, pendingIntentEnviado, pendingIntentEntregue);
					}

					Log.d(TAG, "SMS de número: " + count +" | "+ Util.getDataAtual());

					if(count == (MAX_ENVIO/2)){
						Thread.sleep(HUM_MINUTO);
					}else{
						Thread.sleep(TRINTA_SEGUNDOS);
					}
					count ++;
				}
				
				if((isContinuarEnviando() || count >= MAX_ENVIO) && !isDispositivoConectadoSincronizado()){
					houveRespostaDoSMSEnviado = false;
					//ESGOTOU OS ENVIOS E NÃO TEVE RESPOSTA
					//RETORNAR....
					
				}
				

			} catch (InterruptedException e) {
				Log.d(TAG, "Erro no timer de SMS");
			}

			Log.d(TAG, "Finalizaou o timer de SMS");
		}
		
		private boolean isContinuarEnviando(){
			return Boolean.valueOf(sistemaDAO.getValor(Constantes.KEEP_ENVIO_ALERTA));
		}
		
		private boolean isDispositivoConectadoSincronizado(){
			return Boolean.valueOf(sistemaDAO.getValor(Constantes.DISPOSITIVO_CONECTADO_SINCRONIZADO));
		}
}
