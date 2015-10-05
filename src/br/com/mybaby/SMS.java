package br.com.mybaby;

import java.util.ArrayList;
import java.util.List;

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

	public SMS(Context context){
		this.context = context;
		
	}
	
	public void enviarSMS(){
		//LOGAR
		
		//SETAR NA BASE QUE O SMS FOI ENVIADO
		//QUANDO O APP ABRIR NOVAMENTE INFORMAR Q O SMS FOI ENVIADO. HORARIO E TUDO MAIS
		
		//buscar lista de numeros
		numeros = new ArrayList<String>();
		numeros.add("5511982465760");
		numeros.add("5511982301919");
		
		//buscar a mensagem
		mensagem = "MyBaby! Mensagem automática de emergência! Entre em contato!";
		
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent pendingIntentEnviado = PendingIntent.getBroadcast(context, 0, new Intent(Constantes.SMS_ENVIADO), 0);
		PendingIntent pendingIntentEntregue = PendingIntent.getBroadcast(context, 0, new Intent(Constantes.SMS_ENTREGUE), 0);
		for(String numero : numeros){
			Log.d(TAG, "Enviando SMS para o número: " + numero);
			smsManager.sendTextMessage(numero, null, mensagem, pendingIntentEnviado, pendingIntentEntregue);
		}
	}
}
