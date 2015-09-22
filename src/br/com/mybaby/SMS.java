package br.com.mybaby;

import java.util.ArrayList;
import java.util.List;

import android.telephony.SmsManager;

public class SMS {
	private List<String> numeros;
	private String mensagem;
	
	public void enviarSMS(){
		//LOGAR
		
		//SETAR NA BASE QUE O SMS FOI ENVIADO
		//QUANDO O APP ABRIR NOVAMENTE INFORMAR Q O SMS FOI ENVIADO. HORARIO E TUDO MAIS
		
		//buscar lista de numeros
		numeros = new ArrayList<String>();
		numeros.add("982465760");
		numeros.add("982301919");
		
		//buscar a mensagem
		mensagem = "MyBaby! Fora do alcance."
				+ "Nenhuma resposta foi obtida pelo aplicativo."
				+ "Entre em contato para saber se esta tudo bem.";
		
		SmsManager smsManager = SmsManager.getDefault();
		for(String numero : numeros){
			smsManager.sendTextMessage(numero, null, mensagem, null, null);
		}
	}
}
