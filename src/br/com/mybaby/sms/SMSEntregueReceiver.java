package br.com.mybaby.sms;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import br.com.mybaby.dao.SistemaDAO;
import br.com.mybaby.util.Constantes;


public class SMSEntregueReceiver extends BroadcastReceiver {
	private final String TAG = "SMSEntregueReceiver ";
	
	private SistemaDAO sistemaDAO;
	
	public SMSEntregueReceiver(Context context) {
		sistemaDAO = new SistemaDAO(context);
	}


	@Override
	public void onReceive(Context context, Intent intent) {

		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.e(TAG,"SMS Entregue");
			
//			Bundle bundle = intent.getExtras();
//			if (bundle != null) {
//				Object[] pdus = (Object[])bundle.get("pdus");
//				final SmsMessage[] messages = new SmsMessage[pdus.length];
//				for (int i = 0; i < pdus.length; i++) {
//					messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
//					
//				}
//				if (messages.length > -1) {
//					Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
//				}
//			}
			
			sistemaDAO.update(Constantes.SMS_ENTREGUE, Boolean.TRUE.toString());
			
			break;
		case Activity.RESULT_CANCELED:
			break;
		}
	}
}
